package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.ProductSummaryDTO;
import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.payload.response.CategoryProductPagingResponse;
import com.cosmeticsellingwebsite.payload.response.CategoryProductResponse;
import com.cosmeticsellingwebsite.payload.response.CategoryResponse;
import com.cosmeticsellingwebsite.payload.response.CategorySalesResp;
import com.cosmeticsellingwebsite.repository.CategoryRepository;
import com.cosmeticsellingwebsite.repository.OrderLineRepository;
import com.cosmeticsellingwebsite.repository.ProductFeedbackRepository;
import com.cosmeticsellingwebsite.repository.ProductRepository;
import com.cosmeticsellingwebsite.service.interfaces.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductFeedbackRepository productFeedbackRepository;
    @Autowired
    OrderLineRepository orderLineRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> new CategoryResponse(category.getCategoryId(), category.getCategoryName())).toList();
    }

    // lấy doanh số của từng category trong năm
    @Override
    public List<CategorySalesResp> getCategoryTotalSold() {
        // Lấy thời điểm đầu năm
        LocalDateTime startOfYear = LocalDateTime.now()
                .withDayOfYear(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // Lấy thời điểm cuối năm
        LocalDateTime endOfYear = startOfYear.plusYears(1)
                .minusDays(1)
                .withHour(23)
                .withMinute(59)
                .withSecond(59);


        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> {
            CategorySalesResp categoryResponse = new CategorySalesResp();
            categoryResponse.setCategoryName(category.getCategoryName());
            categoryResponse.setTotalSold(categoryRepository.findTotalSoldByCategoryId(category.getCategoryId(), startOfYear, endOfYear));
            return categoryResponse;
        }).toList();
    }


    @Override
    public List<Category> getAllCategoriess() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);

    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);

    }

    @Override
    public CategoryProductResponse getCategoryWithProducts(Long categoryId) {
        CategoryProductResponse categoryWithProducts = new CategoryProductResponse();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException("Category not found"));
        categoryWithProducts.setCategoryId(category.getCategoryId());
        categoryWithProducts.setCategoryName(category.getCategoryName());
        categoryWithProducts.setProducts(category.getProducts().stream().map(product -> {
            ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO();
            BeanUtils.copyProperties(product, productSummaryDTO);
            List<ProductFeedback> feedbacks = productFeedbackRepository.findByProduct(product);
            Double rating = feedbacks.stream().mapToDouble(ProductFeedback::getRating).average().orElse(0);
            productSummaryDTO.setRatingAverage(rating);
            Long numOfSold = orderLineRepository.sumQuantityByProduct(product).orElse(0L);
            productSummaryDTO.setSellCount(numOfSold);
            return productSummaryDTO;
        }).toList());
        return categoryWithProducts;
    }

    @Override
    public CategoryProductPagingResponse getCategoryWithProductsPaging(Long categoryId, Pageable pageable) {
        CategoryProductPagingResponse categoryWithProductsPaging = new CategoryProductPagingResponse();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException("Category not found"));
        categoryWithProductsPaging.setCategoryId(category.getCategoryId());
        categoryWithProductsPaging.setCategoryName(category.getCategoryName());
        categoryWithProductsPaging.setProducts(productRepository.findByCategory_CategoryId(categoryId,pageable).stream().map(product -> {
            ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO();
            BeanUtils.copyProperties(product, productSummaryDTO);
            List<ProductFeedback> feedbacks = productFeedbackRepository.findByProduct(product);
            Double rating = Optional.ofNullable(feedbacks)
                    .orElse(Collections.emptyList())
                    .stream()
                    .mapToDouble(ProductFeedback::getRating)
                    .average()
                    .orElse(0);
            productSummaryDTO.setRatingAverage(rating);
            Long numOfSold = orderLineRepository.sumQuantityByProduct(product).orElse(0L);
            productSummaryDTO.setSellCount(numOfSold);
            return productSummaryDTO;
        }).toList());
        categoryWithProductsPaging.setTotalProducts(productRepository.countByCategory_CategoryId(categoryId));
        categoryWithProductsPaging.setPageNumber(pageable.getPageNumber());
        categoryWithProductsPaging.setPageSize(pageable.getPageSize());
        categoryWithProductsPaging.setTotalPages((int) Math.ceil((double) categoryWithProductsPaging.getTotalProducts() / pageable.getPageSize()));
        return categoryWithProductsPaging;
    }

    @Override
    public Integer countProducts(Long categoryId) {
        return productRepository.countByCategory_CategoryId(categoryId);
    }


    @Override
    public void deleteCategory(Long id) {
        //neu ton tai sp thuoc category nay thi disable category
        if (productRepository.existsByCategory_CategoryId(id)) {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new CustomException("Category not found"));
            category.setActive(false);
            categoryRepository.save(category);
            return;
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<Category> searchCategory(String search, Pageable pageable) {
        return categoryRepository.findByCategoryNameContaining(search, pageable);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
}
