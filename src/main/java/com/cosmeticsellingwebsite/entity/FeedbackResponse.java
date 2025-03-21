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
    @Column(name = "feedbackResponseId")
    private Long feedbackResponseId;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

    @Column(name = "responseDate")
    private LocalDateTime responseDate;

    @OneToOne(mappedBy = "feedbackResponse")
    @JsonManagedReference
    private ProductFeedback productFeedback;
}