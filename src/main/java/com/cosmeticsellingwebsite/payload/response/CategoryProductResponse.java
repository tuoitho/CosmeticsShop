package com.cosmeticsellingwebsite.payload.response;


import com.cosmeticsellingwebsite.dto.ProductSummaryDTO;
import com.cosmeticsellingwebsite.entity.Product;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryProductResponse {
    private Long categoryId;
    private String categoryName;
    private List<ProductSummaryDTO> products;
}
