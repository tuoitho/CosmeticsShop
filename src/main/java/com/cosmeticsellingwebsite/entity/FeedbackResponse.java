package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    @JoinColumn(name = "productFeedbackId", referencedColumnName = "productFeedbackId")
    private ProductFeedback productFeedback;
}
