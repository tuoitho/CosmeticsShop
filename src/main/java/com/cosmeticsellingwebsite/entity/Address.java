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
    private Long addressId;

    @Column( columnDefinition = "text")
    private String receiverName;

    @Column(length = 255)
    private String receiverPhone;

    @Column(columnDefinition = "text")
    private String address;

    @Column(columnDefinition = "text")
    private String province;

    @Column( columnDefinition = "text")
    private String district;

    @Column(columnDefinition = "text")
    private String ward;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "customerId")
    private User customer;
}