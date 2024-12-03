package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.repository.ProductFeedbackRepository;
import com.cosmeticsellingwebsite.service.interfaces.IProductFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductFeedbackService implements IProductFeedbackService {

    @Autowired
    ProductFeedbackRepository productFeedbackRepository;
    public boolean canUserReviewOrder(Long userId, Long orderId) {
        return !productFeedbackRepository.existsByUser_IdAndOrder_Id(userId, orderId);
    }

}
