package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.FeedbackResponse;

import java.util.Optional;

public interface IFeedbackResponse {
    void saveResponse(FeedbackResponse response);

    void save(FeedbackResponse feedbackResponse);

    void update(FeedbackResponse feedbackResponse);

    Optional<FeedbackResponse> findById(Long id);

    // Phương thức tìm phản hồi theo feedbackId
    FeedbackResponse findByFeedbackId(Long feedbackId);
}
