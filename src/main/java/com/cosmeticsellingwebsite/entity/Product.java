package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private Long productId;

    @Column(name = "productCode", unique = true)
    private String productCode;

    @Column(name = "productName", columnDefinition = "text")
    private String productName;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "brand", columnDefinition = "text")
    private String brand;

    @Column(name = "manufactureDate")
    private LocalDate manufactureDate;

    @Column(name = "expirationDate")
    private LocalDate expirationDate;

    @Column(name = "ingredient", columnDefinition = "text")
    private String ingredient;

    @Column(name = "how_to_use", columnDefinition = "text")
    private String how_to_use;

    @Column(name = "volume")
    private String volume;

    @Column(name = "origin", columnDefinition = "text")
    private String origin;

    @Column(name = "image", columnDefinition = "text")
    private String image;

    @Column(name = "createdDate")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "active")
    private Boolean active = true;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
    private Category category;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private ProductStock productStock;
}