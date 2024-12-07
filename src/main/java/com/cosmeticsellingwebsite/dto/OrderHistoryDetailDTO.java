package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.OrderLine;
import com.cosmeticsellingwebsite.entity.Payment;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderHistoryDetailDTO {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private Double total;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime deliveryDate;

    private Set<OrderLineDTO> orderLines;

    private ShippingAddressDTO shippingAddress;
    private PaymentDTO payment;
    private List<OrderStatusHistoryDTO> orderStatusHistories;
}
