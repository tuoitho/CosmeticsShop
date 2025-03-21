package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "shippingaddress")
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shippingAddressId")
    private Long shippingAddressId;

    @Column(name = "receiverName", columnDefinition = "text")
    private String receiverName;

    @Column(name = "receiverPhone", length = 255)
    private String receiverPhone;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "province", columnDefinition = "text")
    private String province;

    @Column(name = "district", columnDefinition = "text")
    private String district;

    @Column(name = "ward", columnDefinition = "text")
    private String ward;

    @OneToOne
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "orderId", referencedColumnName = "orderId", unique = true)
    private Order order;
}