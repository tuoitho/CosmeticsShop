package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Cart;
import com.cosmeticsellingwebsite.entity.CartItem;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
import com.cosmeticsellingwebsite.repository.CartRepository;
import com.cosmeticsellingwebsite.repository.ProductRepository;
import com.cosmeticsellingwebsite.repository.UserRepository;
import com.cosmeticsellingwebsite.service.interfaces.ICartService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService implements ICartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    public void addProductToCart(AddProductToCartRequest addProductToCartRequest) {
        Optional<User> user = userRepository.findById(addProductToCartRequest.getUserId());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<Product> product = productRepository.findById(addProductToCartRequest.getProductId());
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        //neu chua co cart thi tao moi
        Cart cart = cartRepository.findByUser(user.get());
        if (cart == null) {
//            Logger.log("Cart not found, create new cart");
            cart = new Cart();
            cart.setUser(user.get());
        }
        //add product to cart
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = Set.of();
        }
        Optional<CartItem> cartItem = cartItems.stream().filter(item -> Objects.equals(item.getProduct().getProductId(), addProductToCartRequest.getProductId())).findFirst();
//        kieemr tra xem product da co trong cart chua
        if (cartItem.isPresent()) {
            //neu co roi thi:
            //kiem tra so luong san pham trong kho co du de them vao gio hang khong
            Long quantityInStock = productService.getStockByProductCode(product.get().getProductCode());
            Long quantityInCart = cartItem.get().getQuantity();
            if (quantityInStock < quantityInCart+addProductToCartRequest.getQuantity()) {
                throw new RuntimeException("Số lượng sản phẩm trong giỏ hàng sau khi thêm vượt quá số lượng sản phẩm trong kho");
            }
            cartItem.get().setQuantity(cartItem.get().getQuantity() + addProductToCartRequest.getQuantity());
        } else {
            //neu chua co thi:
            Long quantityInStock = productService.getStockByProductCode(product.get().getProductCode());
            if (quantityInStock < addProductToCartRequest.getQuantity()) {
                throw new RuntimeException("Không đủ số lượng sản phẩm trong kho");
            }
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product.get());
            newCartItem.setQuantity(addProductToCartRequest.getQuantity());
            cartItems.add(newCartItem);
        }
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }

    public void updateProductQuantityInCart(@Valid AddProductToCartRequest addProductToCartRequest) {
        Optional<User> user = userRepository.findById(addProductToCartRequest.getUserId());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<Product> product = productRepository.findById(addProductToCartRequest.getProductId());
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        Optional<Cart> cartOpt = Optional.ofNullable(cartRepository.findByUser(user.get()));
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found");
        }
        Cart cart = cartOpt.get();
        Set<CartItem> cartItems = cart.getCartItems();
        //update quantity
        Optional<CartItem> cartItem = cartItems.stream().filter(item -> Objects.equals(item.getProduct().getProductId(), addProductToCartRequest.getProductId())).findFirst();
        if (cartItem.isEmpty()) {
            throw new RuntimeException("Product not found in cart");
        }
        Long quantityInStock = productService.getStockByProductCode(product.get().getProductCode());
        if (quantityInStock < addProductToCartRequest.getQuantity()) {
            throw new RuntimeException("Không đủ số lượng sản phẩm trong kho");
        }
        cartItem.get().setQuantity(addProductToCartRequest.getQuantity());
//        Logger.log(addProductToCartRequest.getQuantity().toString());
//        Logger.log(cartItem.get().getQuantity().toString());
//
        cart.setCartItems(cartItems);

        cartRepository.save(cart);
    }

    public void removeProductFromCart(Long userId, Long productId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        Optional<Cart> cartOpt = Optional.ofNullable(cartRepository.findByUser(user.get()));
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found");
        }
        Cart cart = cartOpt.get();
        Logger.log(cart.toString());
        Set<CartItem> cartItems = cart.getCartItems();
        Logger.log(cartItems.toString());
        Optional<CartItem> cartItem = cartItems.stream().filter(item -> Objects.equals(item.getProduct().getProductId(), productId)).findFirst();
        if (cartItem.isEmpty()) {
            throw new RuntimeException("Product not found in cart");
        }
        cartItems.remove(cartItem.get());
        Logger.log(cartItems.toString());
        cart.setCartItems(cartItems);
//        cartRepository.save(cart);
        Logger.log( cartRepository.save(cart).toString());
        Logger.log(cartRepository.findByUser(userRepository.findById(userId).get()).toString());
    }
}
