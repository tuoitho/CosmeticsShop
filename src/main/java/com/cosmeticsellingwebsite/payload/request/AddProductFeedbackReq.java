package com.cosmeticsellingwebsite.payload.request;

import com.cosmeticsellingwebsite.entity.ProductFeedback;
import lombok.Data;

@Data
public class AddProductFeedbackReq {
    private Long orderId;
    private Long productId;
    private String comment;
    private Double rating;
    private String image;
}
