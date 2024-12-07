package com.cosmeticsellingwebsite.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductHomeDTO {
    private Long productId;
    private String productCode;
    private String productName;
    private Double cost;
    private String image;
    private Integer totalSoldLast30Days;
    private LocalDateTime createdDate;
    private Double averageRating;
}
