package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Cart;
import com.cosmeticsellingwebsite.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ICartService {

    Optional<Cart> findCartByCustomer(Customer customer);
}
