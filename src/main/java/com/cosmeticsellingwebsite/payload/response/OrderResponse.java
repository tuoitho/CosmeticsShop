package com.cosmeticsellingwebsite.payload.response;

import com.cosmeticsellingwebsite.dto.OrderLineForOrderDTO;
import com.cosmeticsellingwebsite.entity.ShippingAddress;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderResponse {

    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private Double total;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime deliveryDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderLineForOrderDTO> orderLines;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ShippingAddress shippingAddress;

    private PaymentMethod paymentMethod;




}
