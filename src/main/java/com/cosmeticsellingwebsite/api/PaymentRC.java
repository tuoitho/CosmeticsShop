package com.cosmeticsellingwebsite.api;

import com.cosmeticsellingwebsite.payload.response.ApiResponse;
import com.cosmeticsellingwebsite.payload.response.PaymentResponse;
import com.cosmeticsellingwebsite.service.impl.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: mấy cái  @Autowired   PaymentService paymentService phải đổi thành @Autowired   IPaymentService paymentService, tại làm vậy là trong quá trình làm đồ án môn cho nhanh, phải sữa lại cho đúng thực tế.

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentRC {
    //update payment status
    //update payment method
    @Autowired
    PaymentService paymentService;

    @PostMapping("/updateStatus")
    public ResponseEntity<?> updatePaymentStatus(@RequestParam @Valid Long orderId, @RequestParam @Valid String status) {
        PaymentResponse createPaymentResponse=paymentService.updatePaymentStatus(orderId, status);
        return ResponseEntity.ok(new ApiResponse<>(true, "Update payment status successfully", createPaymentResponse));
    }

}
