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
    private Long productFeedbackId;

    @Column(columnDefinition = "text")
    private String comment;

    private String image;

    private LocalDateTime feedbackDate;

    @JoinColumn(name = "userId")
    private Long customerId;

    @JoinColumn(name = "orderId")
    private Long orderId;

    private String productSnapshotName;

    private Double rating;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "productId")
    @ToString.Exclude     @EqualsAndHashCode.Exclude

    private Product product;

    @OneToOne(mappedBy = "productFeedback")
    @JsonManagedReference
//    @JoinColumn(name = "feedbackResponseId")
    @ToString.Exclude     @EqualsAndHashCode.Exclude

    private FeedbackResponse feedbackResponse;
}