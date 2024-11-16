package com.cosmeticsellingwebsite.api;

import com.cosmeticsellingwebsite.payload.response.ApiResponse;
import com.cosmeticsellingwebsite.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRC {
    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public Object getAllCategories() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Get all categories successfully", categoryService.getAllCategories()));
    }
    @GetMapping("/products")
    public Object getCategoryWithProducts(@RequestParam Long categoryId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Get category with products successfully", categoryService.getCategoryWithProducts(categoryId)));
    }
}
