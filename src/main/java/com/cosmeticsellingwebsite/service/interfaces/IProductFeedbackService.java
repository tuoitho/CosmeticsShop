package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.ProductFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface IProductFeedbackService {

    boolean canUserReviewOrder(Long customerId, Long orderId);

    // Phương thức getAllFeedbacks trả về danh sách tất cả các feedback
    List<ProductFeedback> getAllFeedbacks();

    // Phương thức để lấy chi tiết feedback theo ID
    ProductFeedback getFeedbackById(Long id);

    Optional<ProductFeedback> findById(Long id);

    Page<ProductFeedback> getAllFeedbacks(Pageable pageable);

    Page<ProductFeedback> searchFeedbacks(String keyword, Pageable pageable);

    Page<ProductFeedback> getRespondedFeedbacks(String keyword, Pageable pageable);

    Page<ProductFeedback> getNotRespondedFeedbacks(String keyword, Pageable pageable);
}