package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.repository.ProductFeedbackRepository;
import com.cosmeticsellingwebsite.repository.ProductRepository;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/")
public class Test {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductFeedbackRepository productFeedbackRepository;
    @GetMapping("/test")
    public Product test() {
//        Product product = productRepository.findById(1L).get();
//        return product;
        Optional<Product> productOptional = productRepository.findById(1L);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
//            Logger.log(product.toString());
//            Set<ProductFeedback> productFeedbacks = product.getProductFeedbacks();
            return product;
        }
        Logger.log("Product not found");
        return null;
    }
    @GetMapping("/addFb")
    public String addFb() {
//        Product product = productRepository.findById(1L).get();
//        Logger.log(product.getProductFeedbacks().toString());
        Set<ProductFeedback> productFeedbacks = productFeedbackRepository.findAllByProduct_ProductId(1L);
        Logger.log(productFeedbacks.toString());
        Product product = productRepository.findById(1L).get();
        Set<ProductFeedback> productFeedbacks2 = productFeedbackRepository.findAllByProduct(product);
        Logger.log(productFeedbacks2.toString());
//        ProductFeedback productFeedback = new ProductFeedback();
//        productFeedback.setProduct(product);
//        productFeedback.setComment("This is a feedback");
//        Set<ProductFeedback> productFeedbacks = new LinkedHashSet<>();
//        productFeedbacks.add(productFeedback);
//        product.setProductFeedbacks(productFeedbacks);
////        Logger.log(product.toString());
////        System.out.println(product.toString());
//        productRepository.save(product);
        return "success";
    }
}
