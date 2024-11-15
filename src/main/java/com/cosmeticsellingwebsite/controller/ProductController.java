package com.cosmeticsellingwebsite.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("")
    public String index() {
        return "admin/products";
    }


}
