package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDTO {
    private Long paymentId;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private Double total;
}
