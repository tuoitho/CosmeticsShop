package com.cosmeticsellingwebsite.payload.request;

import lombok.Data;

@Data
public class UpdateCartReq {
    private Long cartItemId;
    private int quantity;
}
