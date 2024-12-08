package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.entity.Payment;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.enums.PaymentStatus;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.payload.response.PaymentResponse;
import com.cosmeticsellingwebsite.repository.OrderRepository;
import com.cosmeticsellingwebsite.repository.PaymentRepository;
import com.cosmeticsellingwebsite.service.interfaces.IPaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService implements IPaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    OrderRepository orderRepository;
    @Transactional
    @Override
    public PaymentResponse updatePaymentStatus(@Valid Long orderId, @Valid String status) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId).orElseThrow(() -> new CustomException("Order not found"));
        PaymentStatus paymentStatus = PaymentStatus.valueOf(status);
        payment.setPaymentStatus(paymentStatus);
        if (paymentStatus == PaymentStatus.PAID) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            payment.setPaymentDate(LocalDateTime.now());
//            đánh dấu order đã thanh toán
            Order order = payment.getOrder();
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }

        Payment savedPayment = paymentRepository.save(payment);
        PaymentResponse paymentResponse = new PaymentResponse();
        BeanUtils.copyProperties(savedPayment, paymentResponse);
        paymentResponse.setOrderId(payment.getOrder().getOrderId());
        return paymentResponse;
    }

    @Override
    public void updatePayment(String orderInfo, String paymentTime) {

    }

    @Override
    public PaymentMethod getPaymentMethodByOrder(Order order) {
        Payment payment = paymentRepository.findByOrder(order).orElseThrow(() -> new CustomException("Payment not found"));
        return payment.getPaymentMethod();
    }
}
