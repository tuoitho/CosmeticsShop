package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSnapshotDTO {
    private Long productId;
    private String productCode;
    private String productName;
    private Double cost;
    private String description;
    private String brand;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private String ingredient;
    private String how_to_use;
    private String volume;
    private String origin;
    private String image;
    private Boolean active;
}
