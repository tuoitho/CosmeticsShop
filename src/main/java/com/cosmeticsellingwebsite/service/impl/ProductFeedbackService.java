package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.repository.ProductFeedbackRepository;
import com.cosmeticsellingwebsite.service.interfaces.IProductFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductFeedbackService implements IProductFeedbackService {

    @Autowired
    ProductFeedbackRepository productFeedbackRepository;
    public boolean canUserReviewOrder(Long customerId, Long orderId) {
        return !productFeedbackRepository.existsByCustomerIdAndOrderId(customerId, orderId);
    }
//    public boolean canUserReviewProduct(Long customerId, Long orderId, Long productId) {
//        return !productFeedbackRepository.existsByCustomerIdAndOrderIdAndProduct_ProductId(customerId, orderId, productId);
//    }

}
