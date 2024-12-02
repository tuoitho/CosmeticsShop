package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.FeedbackResponse;
import com.cosmeticsellingwebsite.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDTO {
    private Long productFeedbackId;
    private String customerName;
    private String comment;
    private LocalDateTime feedbackDate;
    private Long customerId;
    private Double rating;
    private FeedbackResponse feedbackResponse;
}
