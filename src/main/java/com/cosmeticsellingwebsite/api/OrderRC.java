package com.cosmeticsellingwebsite.api;

import com.cosmeticsellingwebsite.payload.request.CreateOrderRequest;
import com.cosmeticsellingwebsite.payload.request.UpdateOrderRequest;
import com.cosmeticsellingwebsite.payload.response.ApiResponse;
import com.cosmeticsellingwebsite.payload.response.OrderResponse;
import com.cosmeticsellingwebsite.service.impl.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderRC {
    @Autowired
    OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> addOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        OrderResponse createOrderResponse =orderService.createOrder(createOrderRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Add order successfully", createOrderResponse));
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody @Valid UpdateOrderRequest updateOrderRequest) {
        OrderResponse createOrderResponse =orderService.updateOrder(updateOrderRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Update order successfully", createOrderResponse));
    }
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam @Valid Long orderId) {
        OrderResponse createOrderResponse=orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cancel order successfully", createOrderResponse));
    }
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateOrderStatus(@RequestParam @Valid Long orderId, @RequestParam @Valid String status) {
        OrderResponse createOrderResponse=orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(new ApiResponse<>(true, "Update order status successfully", createOrderResponse));
    }

}

