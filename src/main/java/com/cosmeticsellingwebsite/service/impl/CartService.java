package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.exception.EntityNotFoundException;
import com.cosmeticsellingwebsite.payload.request.AddProductToCartRequest;
import com.cosmeticsellingwebsite.payload.request.UpdateCartReq;
import com.cosmeticsellingwebsite.repository.CartItemRepository;
import com.cosmeticsellingwebsite.repository.CartRepository;
import com.cosmeticsellingwebsite.repository.ProductRepository;
import com.cosmeticsellingwebsite.repository.UserRepository;
import com.cosmeticsellingwebsite.service.interfaces.ICartService;
import com.cosmeticsellingwebsite.util.Logger;
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
    ProductService productService;
    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart item not found"));
    }
    @Override
    public void addProductToCart(AddProductToCartRequest addProductToCartRequest) {
        Customer customer = (Customer) userRepository.findById(authenticationHelper.getUserId()).orElseThrow(() -> new CustomException("User not found"));

        Product product = productRepository.findByProductCode(addProductToCartRequest.getProductCode()).orElseThrow(() -> new CustomException("Product Code " + addProductToCartRequest.getProductCode() + " not found"));
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

    @Override
    public Cart getCartByUserId(Long userId) {
        Logger.log(userId);
        Logger.log(cartRepository.findByCustomer_UserId(userId).toString());
        return cartRepository.findByCustomer_UserId(userId).orElseGet(() -> {
            Customer customer = (Customer) userRepository.findById(userId).orElseThrow(() -> new CustomException("User " + userId + " not found"));
            Cart newCart = new Cart();
            newCart.setCustomer(customer);
            return cartRepository.save(newCart);
        });
    }

    @Override
    public void addToCart(Long userId, AddProductToCartRequest addProductToCartRequest) {
        Product product = productRepository.findByProductCode(addProductToCartRequest.getProductCode()).orElseThrow(() -> new RuntimeException("Product not found"));
        //check if product is active
        if (!product.getActive()) {
            throw new CustomException("Product is not active");
        }
        Logger.log(addProductToCartRequest.toString());
        Cart cart = cartRepository.findByCustomer_UserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomer((Customer) userRepository.findById(authenticationHelper.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
            return cartRepository.save(newCart);
        });
        Logger.log(cart.toString());
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = Set.of();
        }
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductCode().equals(addProductToCartRequest.getProductCode()))
                .findFirst();
        if (existingCartItem.isPresent()) {
            Long productStock = existingCartItem.get().getProduct().getProductStock().getQuantity();
            if (productStock < existingCartItem.get().getQuantity() + addProductToCartRequest.getQuantity()) {
                throw new CustomException("Không đủ số lượng sản phẩm trong kho");
            }
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + addProductToCartRequest.getQuantity());
        } else {
            Long productStock = product.getProductStock().getQuantity();
            if (productStock < addProductToCartRequest.getQuantity()) {
                throw new CustomException("Không đủ số lượng sản phẩm trong kho");
            }
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(addProductToCartRequest.getQuantity());
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }


    @Override
    public void updateProductQuantityInCart(Long userId, UpdateCartReq updateCartReq) {
        Cart cart = cartRepository.findByCustomer_UserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomer((Customer) userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            return cartRepository.save(newCart);
        });
        CartItem cartItem = cartItemRepository.findById(updateCartReq.getCartItemId()).orElseThrow(() -> new RuntimeException("Cart item not found"));
        Set<CartItem> cartItems = cart.getCartItems();
        //update quantity
        Long quantityInStock = cartItem.getProduct().getProductStock().getQuantity();
        if (quantityInStock < updateCartReq.getQuantity()) {
            throw new CustomException("Không đủ số lượng sản phẩm trong kho");
        }
        cartItem.setQuantity((long) updateCartReq.getQuantity());
        cart.setCartItems(cartItems);

        cartRepository.save(cart);
    }

    @Override
    public void removeFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByCustomer_UserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomer((Customer) userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found")));
            return cartRepository.save(newCart);
        });
        Set<CartItem> cartItems = cart.getCartItems();
        cartItems.removeIf(cartItem -> cartItem.getCartItemId().equals(cartItemId));
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }


    @Override
    public Long countProductInCart(){
        return cartItemRepository.count();
    }

    @Override
    public Optional<Cart> findCartByCustomer(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }



}
