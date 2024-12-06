package com.cosmeticsellingwebsite.controller.Payment;


import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.service.impl.OrderService;
import com.cosmeticsellingwebsite.service.paypal.PaypalService;
import com.cosmeticsellingwebsite.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.sdk.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    AuthenticationHelper authenticationHelper;
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
    public ModelAndView checkout(@RequestParam("orderId") Long orderId, ModelMap modelMap) {
        orderService.getOrderById(orderId);
        //Lấy ra người dùng đã đăng nhập
        Long userId = authenticationHelper.getUserId();
        // Kiểm tra xem đơn hàng có thuộc về người dùng đó không, nếu không trả về trang lỗi
        if (!orderService.getOrderById(orderId).getCustomerId().equals(userId)) {
            return new ModelAndView("err/500");
        }
        modelMap.addAttribute("orderId", orderId);
        return new ModelAndView("user/paypalcheckout");
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> request
//            ,
//                                             @RequestParam("amount") Double orderTotal,
//                                             @RequestParam("orderInfo") String orderInfo
    ) {
        try {
            String cart = objectMapper.writeValueAsString(request.get("cart"));
            // Lấy orderId từ redis (hoặc lưu trên session nhưng không an toàn vì seesion có thể bị xóa khi server restart, hoặc khi người dùng xóa cookie,...
//            Integer orderId = redisTemplate.opsForValue().get("orderId") != null ? (Integer) redisTemplate.opsForValue().get("orderId") : null;
//            String orderIdStr=objectMapper.writeValueAsString(request.get("orderId"));
//            Integer orderId = Integer.parseInt(objectMapper.writeValueAsString(request.get("orderId")));
            Integer orderId=objectMapper.convertValue(request.get("orderId"), Integer.class);

            // Tương tự, kiêm tra orderId có thuộc về người dùng hiện tại không
            Long userId = authenticationHelper.getUserId();
            if (!orderService.getOrderById(Long.parseLong(orderId.toString())).getCustomerId().equals(userId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Double orderTotal = orderService.getOrderTotal(Long.parseLong(orderId.toString()));
            Order response = paypalService.createOrder(cart, orderTotal);
            //lưu paypayOrderId vào redis, key là paypalOrderId, value là orderId của hệ thống
            redisTemplate.opsForValue().set("paypalOrderId"+response.getId(), orderId);
            //set expire time cho key
            redisTemplate.expire("paypalOrderId", 30, TimeUnit.MINUTES);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{orderID}/capture")
    public ResponseEntity<?> captureOrder(@PathVariable String orderID) {
        // String orderID là của paypal
        try {
            //Lấy orderId từ redis
            Long orderId = Long.parseLong(redisTemplate.opsForValue().get("paypalOrderId" + orderID).toString());
            // Kiểm tra xem orderId có thuộc về người dùng hiện tại không
            Long userId = authenticationHelper.getUserId();
            if (!orderService.getOrderById(orderId).getCustomerId().equals(userId)) {
                return new ResponseEntity<>("Đơn hàng không thuộc về bạn", HttpStatus.BAD_REQUEST);
            }
            Order response = paypalService.captureOrders(orderID);
            Logger.log("capture" + response);
//            orderService.updateOrderStatus(orderID, "COMPLETED");
            Logger.log("status" + response.getStatus().toString());

            if (response.getStatus().toString().equals("COMPLETED")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                Logger.log("orderId" + orderId);
                Logger.log("formattedDate" + LocalDateTime.now().format(formatter));
                // Chuyển đổi sang String
                String formattedDate = LocalDateTime.now().format(formatter);
                orderService.updateOrderStatusPaymentTime(orderId, formattedDate);

            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}