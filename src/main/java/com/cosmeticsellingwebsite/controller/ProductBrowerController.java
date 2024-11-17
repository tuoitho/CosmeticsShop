package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ProductBrowerController {

    @GetMapping("/products")
    public String browseProducts() {
        Logger.log("Browsing products");
        return "user/products";
    }
}
