package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/browser")
public class ProductBrowerController {

    @GetMapping("/products")
    public String browseProducts() {
        Logger.log("Browsing products");
        return "user/products";
    }
    @GetMapping("/product/{productCode}")
    public String productDetail(@PathVariable("productCode") String productCode, ModelMap model) {
        model.addAttribute("productCode", productCode); // Thêm sản phẩm vào model
        return "user/product"; // Trả về view product-detail
    }
}
