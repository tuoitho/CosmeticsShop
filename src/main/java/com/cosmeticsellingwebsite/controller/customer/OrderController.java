package com.cosmeticsellingwebsite.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer/order")
public class OrderController {
    @GetMapping("payment")
    public String payment() {
        return "customer/payment";
    }

}
