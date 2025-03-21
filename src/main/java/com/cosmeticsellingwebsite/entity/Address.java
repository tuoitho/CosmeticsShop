package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId")
    private Long addressId;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "customerId", referencedColumnName = "userId")
    private User customer;
}