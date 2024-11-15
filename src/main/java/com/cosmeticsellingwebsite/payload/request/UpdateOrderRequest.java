package com.cosmeticsellingwebsite.payload.request;

import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderRequest {
    private Long orderId;
    private AddressForOrderDTO address;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}
