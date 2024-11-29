package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.CartItemDTO;
import com.cosmeticsellingwebsite.dto.ProductSimpleDTO;
import com.cosmeticsellingwebsite.entity.CartItem;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
import com.cosmeticsellingwebsite.service.impl.CartService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        return "user/cart";
    }


    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductToCartRequest addProductToCartRequest) {
        Long userId = authenticationHelper.getUserId();
        cartService.addToCart(userId, addProductToCartRequest);
        return ResponseEntity.ok("Added to cart");
    }

}
