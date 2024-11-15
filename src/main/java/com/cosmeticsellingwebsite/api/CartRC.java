package com.cosmeticsellingwebsite.api;

import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
import com.cosmeticsellingwebsite.payload.response.ApiResponse;
import com.cosmeticsellingwebsite.service.impl.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartRC {
    // TODO: mấy cái  @Autowired CartService cartService phải đổi thành @Autowired ICartService cartService, tại làm vậy là trong quá trình làm đồ án môn cho nhanh, làm xong 1 lượt mấy ấy.
    @Autowired
    CartService cartService;
    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(@RequestBody @Valid AddProductToCartRequest addProductToCartRequest) {
        cartService.addProductToCart(addProductToCartRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Add product to cart successfully", null));
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateProductQuantityInCart(@RequestBody @Valid AddProductToCartRequest addProductToCartRequest) {
        cartService.updateProductQuantityInCart(addProductToCartRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Update product quantity in cart successfully", null));
    }
    @PostMapping("/remove")
    public ResponseEntity<?> removeProductFromCart(@RequestParam @Valid Long userId, @RequestParam @Valid Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Remove product from cart successfully", null));
    }

}
