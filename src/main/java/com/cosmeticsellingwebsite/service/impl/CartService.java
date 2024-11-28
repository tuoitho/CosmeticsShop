package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
import com.cosmeticsellingwebsite.repository.CartRepository;
import com.cosmeticsellingwebsite.repository.ProductRepository;
import com.cosmeticsellingwebsite.repository.UserRepository;
import com.cosmeticsellingwebsite.service.interfaces.ICartService;
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
    AuthenticationHelper authenticationHelper;
    @Autowired
    private ProductService productService;

    public void addProductToCart(AddProductToCartRequest addProductToCartRequest) {
        Customer customer = (Customer) userRepository.findById(authenticationHelper.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findByProductCode(addProductToCartRequest.getProductCode()).orElseThrow(() -> new RuntimeException("Product Code " + addProductToCartRequest.getProductCode() + " not found"));
//        neu chua co cart thi tao moi
        Cart cart = cartRepository.findByCustomer(customer).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomer(customer);
            return cartRepository.save(newCart);
        });
//        add product to cart
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = Set.of();
        }
        Optional<CartItem> cartItem = cartItems.stream().filter(item -> Objects.equals(item.getProduct().getProductCode(), addProductToCartRequest.getProductCode())).findFirst();
//        kieemr tra xem product da co trong cart chua
        if (cartItem.isPresent()) {
            //neu co roi thi:
            //kiem tra so luong san pham trong kho co du de them vao gio hang khong
            Long quantityInStock = productService.getStockByProductCode(product.getProductCode());
            Long quantityInCart = cartItem.get().getQuantity();
            if (quantityInStock < quantityInCart + addProductToCartRequest.getQuantity()) {
                throw new RuntimeException("Số lượng sản phẩm trong giỏ hàng sau khi thêm vượt quá số lượng sản phẩm trong kho");
            }
            cartItem.get().setQuantity(cartItem.get().getQuantity() + addProductToCartRequest.getQuantity());
        } else {
            //neu chua co thi:
            Long quantityInStock = productService.getStockByProductCode(product.getProductCode());
            if (quantityInStock < addProductToCartRequest.getQuantity()) {
                throw new RuntimeException("Không đủ số lượng sản phẩm trong kho");
            }
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(addProductToCartRequest.getQuantity());
            cartItems.add(newCartItem);
        }
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByCustomer_UserId(userId).orElseGet(() -> {
            Customer customer = (Customer) userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User " + userId + " not found"));
            Cart newCart = new Cart();
            newCart.setCustomer(customer);
            return cartRepository.save(newCart);
        });
    }

    public void addToCart(Long userId, AddProductToCartRequest addProductToCartRequest) {
        Product product = productRepository.findByProductCode(addProductToCartRequest.getProductCode()).orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = cartRepository.findByCustomer_UserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomer((Customer) userRepository.findById(authenticationHelper.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
            return cartRepository.save(newCart);
        });
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = Set.of();
        }
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductCode().equals(addProductToCartRequest.getProductCode()))
                .findFirst();
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + addProductToCartRequest.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(addProductToCartRequest.getQuantity());
            cartItems.add(cartItem);
        }
        cartRepository.save(cart);
    }

//    public void updateProductQuantityInCart(@Valid AddProductToCartRequest addProductToCartRequest) {
//        Optional<User> user = userRepository.findById(addProductToCartRequest.getUserId());
//        if (user.isEmpty()) {
//            throw new RuntimeException("User not found");
//        }
//        Optional<Product> product = productRepository.findById(addProductToCartRequest.getProductId());
//        if (product.isEmpty()) {
//            throw new RuntimeException("Product not found");
//        }
//        Optional<Cart> cartOpt = Optional.ofNullable(cartRepository.findByCustomer(user.get()));
//        if (cartOpt.isEmpty()) {
//            throw new RuntimeException("Cart not found");
//        }
//        Cart cart = cartOpt.get();
//        Set<CartItem> cartItems = cart.getCartItems();
//        //update quantity
//        Optional<CartItem> cartItem = cartItems.stream().filter(item -> Objects.equals(item.getProduct().getProductId(), addProductToCartRequest.getProductId())).findFirst();
//        if (cartItem.isEmpty()) {
//            throw new RuntimeException("Product not found in cart");
//        }
//        Long quantityInStock = productService.getStockByProductCode(product.get().getProductCode());
//        if (quantityInStock < addProductToCartRequest.getQuantity()) {
//            throw new RuntimeException("Không đủ số lượng sản phẩm trong kho");
//        }
//        cartItem.get().setQuantity(addProductToCartRequest.getQuantity());
////        Logger.log(addProductToCartRequest.getQuantity().toString());
////        Logger.log(cartItem.get().getQuantity().toString());
////
//        cart.setCartItems(cartItems);
//
//        cartRepository.save(cart);
//    }

//    public void removeProductFromCart(Long userId, Long productId) {
//        Optional<User> user = userRepository.findById(userId);
//        if (user.isEmpty()) {
//            throw new RuntimeException("User not found");
//        }
//        Optional<Product> product = productRepository.findById(productId);
//        if (product.isEmpty()) {
//            throw new RuntimeException("Product not found");
//        }
//        Optional<Cart> cartOpt = Optional.ofNullable(cartRepository.findByCustomer(user.get()));
//        if (cartOpt.isEmpty()) {
//            throw new RuntimeException("Cart not found");
//        }
//        Cart cart = cartOpt.get();
//        Logger.log(cart.toString());
//        Set<CartItem> cartItems = cart.getCartItems();
//        Logger.log(cartItems.toString());
//        Optional<CartItem> cartItem = cartItems.stream().filter(item -> Objects.equals(item.getProduct().getProductId(), productId)).findFirst();
//        if (cartItem.isEmpty()) {
//            throw new RuntimeException("Product not found in cart");
//        }
//        cartItems.remove(cartItem.get());
//        Logger.log(cartItems.toString());
//        cart.setCartItems(cartItems);
////        cartRepository.save(cart);
//        Logger.log( cartRepository.save(cart).toString());
//        Logger.log(cartRepository.findByCustomer(userRepository.findById(userId).get()).toString());
//    }
}
