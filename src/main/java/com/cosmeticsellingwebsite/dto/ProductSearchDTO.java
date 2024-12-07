package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.Product;
import lombok.Data;

@Data
public class ProductSearchDTO {
    private Long productId;
    private String productCode;
    private String productName;
    private Double cost;
    private String description;
    private String brand;
    private String origin;
    private String image;
    private Boolean active=true;


    private Double ratingAverage;
    private Long sellCount;
}
