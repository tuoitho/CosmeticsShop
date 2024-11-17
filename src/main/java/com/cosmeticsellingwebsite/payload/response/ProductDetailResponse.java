package com.cosmeticsellingwebsite.payload.response;

import com.cosmeticsellingwebsite.entity.ProductFeedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDetailResponse {
    private String productCode;
    private String productName;
    private String category;
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
    private Long stock;
    private Double averageRating;
    private Long totalFeedback;
    private Long totalSold;
    private Boolean active;
}
