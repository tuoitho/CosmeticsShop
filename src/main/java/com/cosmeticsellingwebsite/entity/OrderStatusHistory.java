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
    private Long orderStatusHistoryId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime updateAt = LocalDateTime.now();

    private String description;

    @ToString.Exclude
    @ManyToOne
    @EqualsAndHashCode.Exclude

    @JsonBackReference
    @JoinColumn(name = "orderId")
    private Order order;
}