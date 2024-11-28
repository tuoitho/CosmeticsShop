package com.cosmeticsellingwebsite.payload.response;

import com.cosmeticsellingwebsite.dto.ProductSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryProductPagingResponse {
    private Long categoryId;
    private String categoryName;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private int totalProducts;
    private List<ProductSummaryDTO> products;
}
