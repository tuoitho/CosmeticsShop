package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Cart;
import com.cosmeticsellingwebsite.entity.CartItem;
import com.cosmeticsellingwebsite.entity.Customer;
import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
import com.cosmeticsellingwebsite.payload.request.UpdateCartReq;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ICartService {

    CartItem getCartItemById(Long id);

    void addProductToCart(AddProductToCartRequest addProductToCartRequest);

    Cart getCartByUserId(Long userId);

    void addToCart(Long userId, AddProductToCartRequest addProductToCartRequest);

    void updateProductQuantityInCart(Long userId, UpdateCartReq updateCartReq);

    void removeFromCart(Long userId, Long cartItemId);

    Long countProductInCart();

    Optional<Cart> findCartByCustomer(Customer customer);
}
