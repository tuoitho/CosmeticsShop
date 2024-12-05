package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.ProductSummaryDTO;
import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.payload.response.CategoryProductPagingResponse;
import com.cosmeticsellingwebsite.payload.response.CategoryProductResponse;
import com.cosmeticsellingwebsite.payload.response.CategoryResponse;
import com.cosmeticsellingwebsite.repository.CategoryRepository;
import com.cosmeticsellingwebsite.repository.OrderLineRepository;
import com.cosmeticsellingwebsite.repository.ProductFeedbackRepository;
import com.cosmeticsellingwebsite.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductFeedbackRepository productFeedbackRepository;
    @Autowired
    OrderLineRepository orderLineRepository;

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> new CategoryResponse(category.getCategoryId(), category.getCategoryName())).toList();
    }
    public CategoryProductResponse getCategoryWithProducts(Long categoryId) {
        CategoryProductResponse categoryWithProducts = new CategoryProductResponse();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
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

    public CategoryProductPagingResponse getCategoryWithProductsPaging(Long categoryId, Pageable pageable) {
        CategoryProductPagingResponse categoryWithProductsPaging = new CategoryProductPagingResponse();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryWithProductsPaging.setCategoryId(category.getCategoryId());
        categoryWithProductsPaging.setCategoryName(category.getCategoryName());
        categoryWithProductsPaging.setProducts(productRepository.findByCategory_CategoryId(categoryId,pageable).stream().map(product -> {
            ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO();
            BeanUtils.copyProperties(product, productSummaryDTO);
            List<ProductFeedback> feedbacks = productFeedbackRepository.findByProduct(product);
            Double rating = feedbacks.stream().mapToDouble(ProductFeedback::getRating).average().orElse(0);
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

    public Integer countProducts(Long categoryId) {
        return productRepository.countByCategory_CategoryId(categoryId);
    }


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
