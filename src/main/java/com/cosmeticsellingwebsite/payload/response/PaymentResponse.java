package com.cosmeticsellingwebsite.payload.response;

import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
// TODO: thêm validation cho các field cần thiết ở các class


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentResponse {
    private Long orderId;

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;
    private Double total;

}
