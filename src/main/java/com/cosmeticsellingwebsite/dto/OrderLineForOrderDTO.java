package com.cosmeticsellingwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderLineForOrderDTO {
//    chứa productCode nhằm tracking sản phẩm
    private String productCode;
    private Map<String, Object> productSnapshot;
    private Long quantity;
}