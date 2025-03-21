package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "productstock")
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productStockId")
    private Long productStockId;

    @Column(name = "quantity")
    private Long quantity;

    @OneToOne
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "productId", referencedColumnName = "productId", unique = true)
    private Product product;
}