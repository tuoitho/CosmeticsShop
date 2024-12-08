package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.payload.response.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface IPaymentService {
    @Transactional
    PaymentResponse updatePaymentStatus(@Valid Long orderId, @Valid String status);

    void updatePayment(String orderInfo, String paymentTime);

    PaymentMethod getPaymentMethodByOrder(Order order);
}
