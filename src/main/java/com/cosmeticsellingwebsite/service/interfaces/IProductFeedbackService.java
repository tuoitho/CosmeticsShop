package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.ProductFeedback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IProductFeedbackService {

    // Phương thức getAllFeedbacks trả về danh sách tất cả các feedback
    List<ProductFeedback> getAllFeedbacks();

    // Phương thức để lấy chi tiết feedback theo ID
    ProductFeedback getFeedbackById(Long id);

    Optional<ProductFeedback> findById(Long id);

}