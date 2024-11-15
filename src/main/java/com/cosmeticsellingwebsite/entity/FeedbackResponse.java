package com.cosmeticsellingwebsite.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FeedbackResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackResponseId;
    @Column(columnDefinition = "text")
    private String comment;
    private LocalDateTime responseDate;

    @OneToOne
    @JoinColumn(name = "productFeedbackId", referencedColumnName = "productFeedbackId")
    private ProductFeedback productFeedback;
}
