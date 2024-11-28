//package com.cosmeticsellingwebsite.apidabo;
//
//import com.cosmeticsellingwebsite.payload.request.AddProductRequest;
//import com.cosmeticsellingwebsite.payload.response.ApiResponse;
//import com.cosmeticsellingwebsite.payload.response.ProductDetailResponse;
//import com.cosmeticsellingwebsite.payload.response.ProductPagingResponse;
//import com.cosmeticsellingwebsite.payload.response.ProductResponse;
//import com.cosmeticsellingwebsite.service.impl.ProductService;
//import com.cosmeticsellingwebsite.service.ImageService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/product")
//public class ProductRC {
//    // TODO: bị nhầm productCode vs productId, sẽ sửa sau
//    @Autowired
//    ProductService productService;
//    @Autowired
//    ImageService imageService;
//    @GetMapping("/list")
//    public ResponseEntity<?> list(@RequestParam(defaultValue = "0") Integer page) {
//        if (page == null) {
//            page = 0;
//        }
//
//        int totalProducts = productService.count().intValue();
//        int pageSize = 5;
//        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
//        int pageNumber = page; // trang đầu tiên
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("productCode").ascending());
//        List<ProductResponse> productPage = productService.findAllProduct(pageable);
//        ApiResponse<?> response = new ApiResponse<>( true, "Get product list successfully", new ProductPagingResponse(productPage, totalPages, pageNumber, pageSize, totalProducts));
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/delete")
//    public ResponseEntity<?> delete(@RequestParam @Valid Long id) {
//        productService.deleteProduct(id);
//        // Trả về response
//        ApiResponse<Void> response = new ApiResponse<>( true, "Delete product successfully", null);
//        return ResponseEntity.ok(response);
//    }
//    @PostMapping(value = "/add",consumes = {"multipart/form-data"})
//    public ResponseEntity<?> add(@RequestPart("product") @Valid AddProductRequest addProductRequest, @RequestPart("image") MultipartFile imageFile) throws IOException {
//        // Lưu file ảnh và lấy tên hoặc URL của ảnh
//        String imageUrl = imageService.saveImage(imageFile);  // Giả sử bạn có dịch vụ lưu trữ ảnh
//        addProductRequest.setImage(imageUrl);
//
//        ProductResponse productResponse=productService.addProduct(addProductRequest);
//        ApiResponse<?> response = new ApiResponse<>( true, "Add product successfully", productResponse);
//        return ResponseEntity.ok(response);
//    }
////    @PostMapping("/update")
////    public ResponseEntity<?> update(@RequestBody @Valid AddProductRequest addProductRequest) {
////        var o=productService.updateProduct(addProductRequest);
////        // Trả về response
////        ApiResponse<?> response = new ApiResponse<>( true, "Update product successfully", o);
////        return ResponseEntity.ok(response);
////    }
//    @PostMapping(value = "/update",consumes = {"multipart/form-data"})
//    public ResponseEntity<?> update(@RequestPart("product") @Valid AddProductRequest addProductRequest, @RequestPart("image") MultipartFile imageFile) throws IOException {
//        // Lưu file ảnh và lấy tên hoặc URL của ảnh
//        String imageUrl = imageService.saveImage(imageFile);  // Giả sử bạn có dịch vụ lưu trữ ảnh
//        addProductRequest.setImage(imageUrl);
//
//        ProductResponse productResponse=productService.updateProduct(addProductRequest);
//        ApiResponse<?> response = new ApiResponse<>( true, "Add product successfully", productResponse);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/detail")
//    public ResponseEntity<?> detail(@RequestParam @Valid String productCode) {
//        ProductDetailResponse productResponse=productService.getProductDetail(productCode);
//        // Trả về response
//        ApiResponse<?> response = new ApiResponse<>( true, "Get product detail successfully", productResponse);
//        return ResponseEntity.ok(response);
//    }
//}
