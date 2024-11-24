package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

//    những cartitem không còn trong cart nữa, dưới db vẫn còn tham chiếu, dùng orphanRemoval = true để xoá dưới db
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Set<CartItem> cartItems;


    @OneToOne
    @JoinColumn(name = "customerId", referencedColumnName = "userId")
    private Customer customer;
}
