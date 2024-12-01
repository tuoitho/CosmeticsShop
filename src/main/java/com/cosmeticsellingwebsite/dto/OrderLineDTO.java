package com.cosmeticsellingwebsite.dto;

import lombok.Data;

@Data
public class OrderLineDTO {
    private ProductSnapshotDTO productSnapshot;
    private Long quantity;
}
