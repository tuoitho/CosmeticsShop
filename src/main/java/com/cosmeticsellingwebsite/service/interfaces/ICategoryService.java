package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.payload.response.CategoryProductPagingResponse;
import com.cosmeticsellingwebsite.payload.response.CategoryProductResponse;
import com.cosmeticsellingwebsite.payload.response.CategoryResponse;
import com.cosmeticsellingwebsite.payload.response.CategorySalesResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getAllCategories();

    // lấy doanh số của từng category trong năm
    List<CategorySalesResp> getCategoryTotalSold();

    List<Category> getAllCategoriess();
    Category getCategoryById(Long id); // Phương thức lấy danh mục theo id
    void saveCategory(Category category); // Phương thức lưu danh mục

    CategoryProductResponse getCategoryWithProducts(Long categoryId);

    CategoryProductPagingResponse getCategoryWithProductsPaging(Long categoryId, Pageable pageable);

    Integer countProducts(Long categoryId);

    void deleteCategory(Long id);

    Page<Category> searchCategory(String search, Pageable pageable);

    Page<Category> findAll(Pageable pageable);
}
