package com.cosmeticsellingwebsite.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemForCheckoutDTO {
    private String productCode;
    private String productName;
    private double cost;
    private int quantity;
    private String image;
}