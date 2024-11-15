package com.cosmeticsellingwebsite.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCategoryRequest {
    private String categoryName;
}
