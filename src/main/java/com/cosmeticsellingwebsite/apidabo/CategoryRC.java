//package com.cosmeticsellingwebsite.apidabo;
//
//import com.cosmeticsellingwebsite.payload.response.ApiResponse;
//import com.cosmeticsellingwebsite.payload.response.CategoryProductPagingResponse;
//import com.cosmeticsellingwebsite.service.impl.CategoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/categories")
//public class CategoryRC {
//    @Autowired
//    CategoryService categoryService;
//
//    @GetMapping("")
//    public Object getAllCategories() {
//        return ResponseEntity.ok(new ApiResponse<>(true, "Get all categories successfully", categoryService.getAllCategories()));
//    }
//    @GetMapping("/products")
//    public Object getCategoryWithProducts(@RequestParam Long categoryId,@RequestParam(required = false) Integer page) {
//        if (page == null) {
//            return ResponseEntity.ok(new ApiResponse<>(true, "Get category with products successfully", categoryService.getCategoryWithProducts(categoryId)));
//        }
//        int totalProducts = categoryService.countProducts(categoryId);
//        int pageSize = 10;
//        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
//        int pageNumber = page; // trang đầu tiên
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("productCode").ascending());
//        CategoryProductPagingResponse categoryProductPagingResponse = categoryService.getCategoryWithProductsPaging(categoryId, pageable);
//        return ResponseEntity.ok(new ApiResponse<>(true, "Get category with products successfully", categoryService.getCategoryWithProductsPaging(categoryId, pageable)));
//    }
//}
