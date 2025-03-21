package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cartitem")
public class CartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cartId", referencedColumnName = "cartId")
    private Cart cart;
}