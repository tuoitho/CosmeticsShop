package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.CartItemDTO;
import com.cosmeticsellingwebsite.dto.ProductSimpleDTO;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
import com.cosmeticsellingwebsite.payload.request.UpdateCartReq;
import com.cosmeticsellingwebsite.service.impl.CartService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer/cart")
public class CartController {

    @Autowired
    AuthenticationHelper authenticationHelper;
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @GetMapping("")
    public String showCart(Model model) {
        List<CartItemDTO> cartItems = cartService.getCartByUserId(authenticationHelper.getUserId()).getCartItems().stream().map(cartItem -> {
            Logger.log(cartItem);
            CartItemDTO cartItemDTO = new CartItemDTO();
            Product product = cartItem.getProduct();
            ProductSimpleDTO productSimpleDTO = new ProductSimpleDTO();
            BeanUtils.copyProperties(product, productSimpleDTO);
            cartItemDTO.setProduct(productSimpleDTO);
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setCartItemId(cartItem.getCartItemId());
            return cartItemDTO;
        }).toList();
        Logger.log(cartItems);
        model.addAttribute("cartItems", cartItems);
        double total = 0d;
        for (CartItemDTO c : cartItems) {
            total += c.getQuantity() * c.getProduct().getCost();
        }
        Logger.log(total);
        model.addAttribute("total", total);
        return "customer/cart";
    }


    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductToCartRequest addProductToCartRequest) {
        Long userId = authenticationHelper.getUserId();
        cartService.addToCart(userId, addProductToCartRequest);
        return ResponseEntity.ok("Added to cart");
    }
    @PostMapping("/remove-from-cart")
    public ResponseEntity<?> removeFromCart(@RequestParam Long cartItemId) {
        Long userId = authenticationHelper.getUserId();
        cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.ok("Removed from cart");
    }
    @PostMapping("/update-quantity")
    public ResponseEntity<?> updateCart(@RequestBody UpdateCartReq updateCartReq) {
        Long userId = authenticationHelper.getUserId();
        cartService.updateProductQuantityInCart(userId, updateCartReq);
        return ResponseEntity.ok().build();
    }

}
