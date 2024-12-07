package com.cosmeticsellingwebsite.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductSimpleDTO {
    private Long productId;
    private String productCode;
    private String productName;
    private Double cost;
    private String image;
    private Boolean active;
}
