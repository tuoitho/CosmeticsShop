package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.payload.request.AddProductFeedbackReq;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer/product")
public class ProductController {
    @Autowired
    AuthenticationHelper authenticationHelper;
    @Autowired
    ProductService productService;
    //add review
    @PostMapping("/addReview")
    @ResponseBody
    public ResponseEntity<?> addReview(@RequestBody @Valid AddProductFeedbackReq addProductFeedbackReq) {
        Long customerId = authenticationHelper.getUserId();
        //kiem tra xem khach hang co order san pham nay khong
        Logger.log("addReview",addProductFeedbackReq);
        productService.addFeedback(customerId, addProductFeedbackReq);
        return ResponseEntity.ok("Review added successfully");
    }
    @GetMapping("/review")
    @ResponseBody
    public ResponseEntity<?> getReview(@RequestParam Long productId, @RequestParam Long orderId) {
        Long customerId = authenticationHelper.getUserId();
        Logger.log(orderId,productId);
        return ResponseEntity.ok(productService.getFeedback(customerId,orderId,productId));
    }
}
