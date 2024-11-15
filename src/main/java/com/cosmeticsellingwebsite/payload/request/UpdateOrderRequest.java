package com.cosmeticsellingwebsite.payload.request;

import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderRequest {
    @NotNull(message = "Order ID is required")
    private Long orderId;
    @NotNull(message = "Address is required")
    private AddressForOrderDTO address;
    @NotNull(message = "Payment method is required")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}
