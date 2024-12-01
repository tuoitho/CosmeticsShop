package com.cosmeticsellingwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDTO {
    private Long cartItemId;
    private ProductSimpleDTO product;
    private Long quantity;
}
