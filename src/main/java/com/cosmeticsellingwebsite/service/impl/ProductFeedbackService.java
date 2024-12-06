package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.repository.ProductFeedbackRepository;
import com.cosmeticsellingwebsite.service.interfaces.IProductFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // Phương thức getAllFeedbacks trả về danh sách tất cả các feedback
    @Override
    public List<ProductFeedback> getAllFeedbacks() {
        return productFeedbackRepository.findAll();
    }

    // Phương thức để lấy chi tiết feedback theo ID
    @Override
    public ProductFeedback getFeedbackById(Long id) {
        return productFeedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
    }

    @Override
    public Optional<ProductFeedback> findById(Long id) {
        return productFeedbackRepository.findById(id);
    }

}
