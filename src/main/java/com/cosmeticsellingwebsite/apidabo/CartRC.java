//package com.cosmeticsellingwebsite.apidabo;
//
//import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
//import com.cosmeticsellingwebsite.payload.response.ApiResponse;
//import com.cosmeticsellingwebsite.service.impl.CartService;
//import com.cosmeticsellingwebsite.util.Logger;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/cart")
//public class CartRC {
//    @Autowired
//    CartService cartService;
//    @PostMapping("/add")
//    public ResponseEntity<?> addProductToCart(@RequestHeader Map<String, String> headers, @RequestBody @Valid AddProductToCartRequest addProductToCartRequest) {
//        System.out.println("Add product to cart");
//        Logger.log("Authorization: " + headers);
//        Logger.log("Authorization: " + headers.get("authorization"));
//        String token = headers.get("authorization").substring(7);
//        Logger.log("Token: " + token);
//        cartService.addProductToCart(addProductToCartRequest);
//        return ResponseEntity.ok(new ApiResponse<>(true, "Add product to cart successfully", null));
//    }
////    @PostMapping("/update")
////    public ResponseEntity<?> updateProductQuantityInCart(@RequestBody @Valid AddProductToCartRequest addProductToCartRequest) {
////        cartService.updateProductQuantityInCart(addProductToCartRequest);
////        return ResponseEntity.ok(new ApiResponse<>(true, "Update product quantity in cart successfully", null));
////    }
////    @PostMapping("/remove")
////    public ResponseEntity<?> removeProductFromCart(@RequestParam @Valid Long userId, @RequestParam @Valid Long productId) {
////        cartService.removeProductFromCart(userId, productId);
////        return ResponseEntity.ok(new ApiResponse<>(true, "Remove product from cart successfully", null));
////    }
//
//}
