package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.FeedbackResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFeedbackDTO {
    private Long productFeedbackId;
    private Long orderId;
    private String customerName;
    private LocalDateTime feedbackDate;
    private FeedbackResponse feedbackResponse;

}
