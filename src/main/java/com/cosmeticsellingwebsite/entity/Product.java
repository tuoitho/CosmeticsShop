package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(unique = true)
    private String productCode;

    @Column(columnDefinition = "text")
    private String productName;

    @Column(name = "cost")
    private Double cost;

    @Column(columnDefinition = "text")
    private String description;

    @Column( columnDefinition = "text")
    private String brand;

    private LocalDate manufactureDate;

    private LocalDate expirationDate;

    @Column( columnDefinition = "text")
    private String ingredient;

    @Column(columnDefinition = "text")
    private String how_to_use;

    private String volume;

    @Column( columnDefinition = "text")
    private String origin;

    @Column(columnDefinition = "text")
    private String image;

    private LocalDateTime createdDate = LocalDateTime.now();

    private Boolean active = true;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "categoryId")
    @ToString.Exclude  @EqualsAndHashCode.Exclude
    private Category category;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonManagedReference     @EqualsAndHashCode.Exclude

    private ProductStock productStock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ProductFeedback> productFeedbacks= Set.of();
}