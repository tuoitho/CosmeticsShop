package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ProductFeedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productFeedbackId;
    @Column(columnDefinition = "text")
    private String comment;
    private LocalDateTime feedbackDate;
    // đây là thuộc tính của ProductFeedback, không phải của Customer
    private Long customerId;
    // đây là thuộc tính của ProductFeedback, không phải của Order
    private Long orderId;
    private Double rating;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;

    @OneToOne(mappedBy = "productFeedback")
    @JsonManagedReference
    private FeedbackResponse feedbackResponse;


}
