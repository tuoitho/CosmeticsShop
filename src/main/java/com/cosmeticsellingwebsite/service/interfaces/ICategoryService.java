package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategoriess();
    Category getCategoryById(Long id); // Phương thức lấy danh mục theo id
    void saveCategory(Category category); // Phương thức lưu danh mục
}
