package com.cosmeticsellingwebsite.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressForOrderDTO {
    private Long addressId;
    private String receiverName;
    private String receiverPhone;
    private String address;
    private String province;
    private String district;
    private String ward;
}
