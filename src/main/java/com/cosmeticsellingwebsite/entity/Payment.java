package com.cosmeticsellingwebsite.entity;

import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "payment")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

    private Double total;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "orderId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;
}