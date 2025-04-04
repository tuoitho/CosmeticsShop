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
    private Long orderLineId;

    @ManyToOne
    @JoinColumn(name = "productId")
    @ToString.Exclude
    private Product product;

    @Convert(converter = JsonToMapConverter.class)
    @ToString.Exclude
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> productSnapshot;

    private Long quantity;

    @ManyToOne
    @ToString.Exclude
    @JsonBackReference
    @JoinColumn(name = "orderId")
    @EqualsAndHashCode.Exclude
    private Order order;
}