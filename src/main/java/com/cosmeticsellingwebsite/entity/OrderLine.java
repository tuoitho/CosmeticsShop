package com.cosmeticsellingwebsite.entity;

import com.cosmeticsellingwebsite.util.JsonToMapConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orderline")
public class OrderLine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderLineId")
    private Long orderLineId;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;

    @Convert(converter = JsonToMapConverter.class)
    @Column(name = "productSnapshot", columnDefinition = "TEXT")
    private Map<String, Object> productSnapshot;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private Order order;
}