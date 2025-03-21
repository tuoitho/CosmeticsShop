package com.cosmeticsellingwebsite.entity;

import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orderstatushistory")
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderStatusHistoryId")
    private Long orderStatusHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "updateAt")
    private LocalDateTime updateAt = LocalDateTime.now();

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private Order order;
}