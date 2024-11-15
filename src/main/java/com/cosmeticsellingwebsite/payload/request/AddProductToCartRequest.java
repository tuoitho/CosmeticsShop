package com.cosmeticsellingwebsite.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddProductToCartRequest {
    @NotNull(message = "userId is required")
    private Long userId;
    @NotNull(message = "productId is required")
    private Long productId;
    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Long quantity;
}
