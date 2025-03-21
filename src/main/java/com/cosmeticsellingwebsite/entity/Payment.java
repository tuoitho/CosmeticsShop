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
    @Column(name = "paymentId")
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus")
    private PaymentStatus paymentStatus;

    @Column(name = "paymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "total")
    private Double total;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private Order order;
}