package com.cosmeticsellingwebsite.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddAddressRequest {
    @NotNull(message = "userId is required")
    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String address;
    private String province;
    private String district;
    private String ward;
}
