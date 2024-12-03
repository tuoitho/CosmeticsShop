package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
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
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

}
