package com.cosmeticsellingwebsite.controller.Payment;


import com.cosmeticsellingwebsite.service.impl.OrderService;
import com.cosmeticsellingwebsite.service.paypal.PaypalService;
import com.cosmeticsellingwebsite.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.sdk.models.Order;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

// 4032031934855593
// 05/2026
// 760
@Controller
@RequestMapping("/customer/paypal")
public class PaypalController {
    @Autowired
    @Lazy
    PaypalService paypalService;

    @Autowired
    OrderService orderService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    //    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/checkout")
    public ModelAndView checkout(
//            @RequestParam("orderId") Long orderId,
//            HttpSession session, Model model
    ) {
        return new ModelAndView("user/paypalcheckout");
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> request, HttpSession session
//            ,
//                                             @RequestParam("amount") Double orderTotal,
//                                             @RequestParam("orderInfo") String orderInfo
    ) {
        try {
            String cart = objectMapper.writeValueAsString(request.get("cart"));
            // Lấy orderId từ redis (hoặc lưu trên session nhưng không an toàn vì seesion có thể bị xóa khi server restart, hoặc khi người dùng xóa cookie,...
            Integer orderId = redisTemplate.opsForValue().get("orderId") != null ? (Integer) redisTemplate.opsForValue().get("orderId") : null;
            Double orderTotal = orderService.getOrderTotal(Long.parseLong(orderId.toString()));
            Order response = paypalService.createOrder(cart, orderTotal);
            //lưu paypayOrderId vào redis
            redisTemplate.opsForValue().set("paypalOrderId", response.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{orderID}/capture")
    public ResponseEntity<?> captureOrder(@PathVariable String orderID) {
        try {
            //nếu client gửi không trùng với orderId lưu trong redis thì trả về lỗi do gian lận
            if (!orderID.equals(redisTemplate.opsForValue().get("paypalOrderId"))) {
                return new ResponseEntity<>("Đơn hàng không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            Order response = paypalService.captureOrders(orderID);
            Logger.log("capture" + response);
//            orderService.updateOrderStatus(orderID, "COMPLETED");
            Logger.log("status" + response.getStatus().toString());

            if (response.getStatus().toString().equals("COMPLETED")) {
//                return new ResponseEntity<Order>(response, HttpStatus.OK);
                Integer orderId = redisTemplate.opsForValue().get("orderId") != null ? (Integer) redisTemplate.opsForValue().get("orderId") : null;
                if (orderId == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                Logger.log("orderId" + orderId);
                Logger.log("formattedDate" + LocalDateTime.now().format(formatter));
                // Chuyển đổi sang String
                String formattedDate = LocalDateTime.now().format(formatter);
                orderService.updateOrderStatusPaymentTime(Long.valueOf(orderId.toString()), formattedDate);

            }
            return new ResponseEntity<Order>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}