package com.cosmeticsellingwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemForOrderDTO {
    private String productCode;
    private Long quantity;
}
