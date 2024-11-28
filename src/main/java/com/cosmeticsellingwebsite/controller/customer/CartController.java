package com.cosmeticsellingwebsite.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer/cart")
public class CartController {

    @GetMapping("") // Hiển thị giỏ hàng
    public String viewCart() {
        return "customer/cart";
    }
}
