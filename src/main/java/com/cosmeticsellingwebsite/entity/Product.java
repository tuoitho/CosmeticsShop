package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(unique = true)
    private String productCode;
    @Column(columnDefinition = "text")
    private String productName;
    private Double cost;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String brand;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    @Column(columnDefinition = "text")
    private String ingredient;
    @Column(columnDefinition = "text")
    private String how_to_use;
    private String volume;
    @Column(columnDefinition = "text")
    private String origin;
    @Column(columnDefinition = "text")
    private String image;
    private Boolean active=true;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
    private Category category;

}
