package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingAddressId;
    @Column(columnDefinition = "text")
    private String receiverName;
    private String receiverPhone;
    @Column(columnDefinition = "text")
    private String address;
    @Column(columnDefinition = "text")
    private String province;
    @Column(columnDefinition = "text")
    private String district;
    @Column(columnDefinition = "text")
    private String ward;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private Order order;
}
