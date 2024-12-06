package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long> {
    FeedbackResponse findByProductFeedback_ProductFeedbackId(Long feedbackId);

}
