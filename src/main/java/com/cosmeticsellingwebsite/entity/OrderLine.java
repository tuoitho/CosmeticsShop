package com.cosmeticsellingwebsite.entity;

import com.cosmeticsellingwebsite.util.JsonToMapConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class OrderLine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineId;


    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    @JsonManagedReference
    private Product product;


    @Convert(converter = JsonToMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> productSnapshot;  // Metadata stored as JSON map


    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    @JsonBackReference
    private Order order;
}
