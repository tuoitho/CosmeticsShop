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
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "productId")
    @ToString.Exclude     @EqualsAndHashCode.Exclude

    private Product product;

    private Long quantity;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude     @EqualsAndHashCode.Exclude

    @JoinColumn(name = "cartId")
    private Cart cart;
}