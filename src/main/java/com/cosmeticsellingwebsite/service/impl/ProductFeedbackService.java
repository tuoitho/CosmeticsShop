package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.repository.ProductFeedbackRepository;
import com.cosmeticsellingwebsite.service.interfaces.IProductFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<ProductFeedback> getAllFeedbacks(Pageable pageable) {
        return productFeedbackRepository.findAll(pageable);
    }

    public Page<ProductFeedback> searchFeedbacks(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return productFeedbackRepository.findAll(pageable);
        }
        return productFeedbackRepository.searchFeedbacks(keyword, pageable);
    }

    public Page<ProductFeedback> getRespondedFeedbacks(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return productFeedbackRepository.findByFeedbackResponseIsNotNull(pageable);
        }
        return productFeedbackRepository.findByFeedbackResponseIsNotNullAndKeyword(keyword, pageable);
    }

    public Page<ProductFeedback> getNotRespondedFeedbacks(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return productFeedbackRepository.findByFeedbackResponseIsNull(pageable);
        }
        return productFeedbackRepository.findByFeedbackResponseIsNullAndKeyword(keyword, pageable);
    }

}
