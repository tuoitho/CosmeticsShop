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
    private Long shippingAddressId;

    @Column(columnDefinition = "text")
    private String receiverName;

    @Column(length = 255)
    private String receiverPhone;

    @Column( columnDefinition = "text")
    private String address;

    @Column(columnDefinition = "text")
    private String province;

    @Column( columnDefinition = "text")
    private String district;

    @Column(columnDefinition = "text")
    private String ward;

    @OneToOne
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn( unique = true, name = "orderId")
    private Order order;
}