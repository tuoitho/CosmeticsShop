package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "productfeedback")
public class ProductFeedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productFeedbackId")
    private Long productFeedbackId;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

    @Column(name = "image")
    private String image;

    @Column(name = "feedbackDate")
    private LocalDateTime feedbackDate;

    @JoinColumn(name = "customerId", referencedColumnName = "userId")
    private Long customerId;

    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private Long orderId;

    @Column(name = "productSnapshotName")
    private String productSnapshotName;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "productFeedbackId", referencedColumnName = "productFeedbackId")
    private FeedbackResponse feedbackResponse;
}