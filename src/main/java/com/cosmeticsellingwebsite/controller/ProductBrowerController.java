package com.cosmeticsellingwebsite.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public class ProductBrowerController {

    @GetMapping("/products")
    public String browseProducts() {
        return "user/products";
    }
}
