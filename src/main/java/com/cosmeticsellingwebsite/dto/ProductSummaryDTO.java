package com.cosmeticsellingwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductSummaryDTO {
    private String productCode;
    private String productName;
    private Double ratingAverage;
    private Long sellCount;
    private String image;
    private String origin;
}
