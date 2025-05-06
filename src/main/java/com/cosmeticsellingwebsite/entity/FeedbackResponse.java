package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "feedbackresponse")
public class FeedbackResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackResponseId;

    @Column( columnDefinition = "text")
    private String comment;

    private LocalDateTime responseDate;

    @OneToOne()
    @JoinColumn(name = "productFeedbackId")
    @JsonManagedReference
    @ToString.Exclude
    private ProductFeedback productFeedback;
}