package com.cosmeticsellingwebsite.dto;

import lombok.Data;

@Data
public class ShippingAddressDTO {
    private Long addressId;
    private String receiverName;
    private String receiverPhone;
    private String address;
    private String province;
    private String district;
    private String ward;
}