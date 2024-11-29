package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.CartItemDTO;
import com.cosmeticsellingwebsite.dto.CartItemForCheckoutDTO;
import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.Cart;
import com.cosmeticsellingwebsite.entity.CartItem;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.payload.request.CreateOrderRequest;
import com.cosmeticsellingwebsite.payload.response.OrderResponse;
import com.cosmeticsellingwebsite.service.impl.CartService;
import com.cosmeticsellingwebsite.service.impl.OrderService;
import com.cosmeticsellingwebsite.service.impl.UserService;
import com.cosmeticsellingwebsite.service.vnpay.VNPAYService;
import com.cosmeticsellingwebsite.util.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
// VNPAY
//Ngân hàng: NCB
//Số thẻ: 9704198526191432198
//Tên chủ thẻ:NGUYEN VAN A
//Ngày phát hành:07/15
//Mật khẩu OTP:123456
@Controller
@RequestMapping("/customer/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    AuthenticationHelper authenticationHelper;
    @Autowired
    CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    VNPAYService vnpayService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @PostMapping("/preview-checkout")
    public String checkout(@RequestParam("cartItemIds") List<Long> cartItemIds, ModelMap model) {

        //    example data
//        List<CartItemDTO> cartItems = List.of(
//                new CartItemDTO("Body_16", "Dầu gội thông ninh", 100000, 1, "https://static.thcdn.com/images/large/origen//productimg/1600/1600/10364465-1064965873801360.jpg"),
//                new CartItemDTO("Body_41", "Body 02", 20000, 1, "https://static.thcdn.com/images/large/origen//productimg/1600/1600/10364465-1064965873801360.jpg"),
//                new CartItemDTO("Body_63", "Dầu gội thông ninh2", 300000, 1, "https://via.placeholder.com/150")
//        );
//        Logger.log("cartItemIds: " + cartItemIds);
        Long userId= authenticationHelper.getUserId();
        //anh xa tung cartiemid thanh cartItemDTO
        List<CartItemForCheckoutDTO> cartItems = cartItemIds.stream().map(cartItemId -> {
            CartItem cartItem = cartService.getCartItemById(cartItemId);
            CartItemForCheckoutDTO cartItemDTO = new CartItemForCheckoutDTO();
            cartItemDTO.setProductCode(cartItem.getProduct().getProductCode());
            cartItemDTO.setProductName(cartItem.getProduct().getProductName());
            cartItemDTO.setCost(cartItem.getProduct().getCost());
            cartItemDTO.setQuantity(Math.toIntExact(cartItem.getQuantity()));
            cartItemDTO.setImage(cartItem.getProduct().getImage());
            return cartItemDTO;
        }).toList();

        double subtotal = cartItems.stream().mapToDouble(item -> item.getQuantity() * item.getCost()) // Replace 100 with the actual price of the product
                .sum();
        double total = subtotal; // You can include discount logic here if needed
        // Add calculated data to the model to be displayed in the view
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total);
//        model.addAttribute("voucherCode", voucherCode);
        model.addAttribute("userId", userId);

        List<Address> addresses = userService.getAddresses(userId);
        Logger.log(addresses);
        model.addAttribute("addresses", addresses);
//        return "customer/checkout";
        return "user/checkout";
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateOrderRequest createOrderRequest, HttpSession session, HttpServletRequest request) {
        try {
            // Lấy thông tin khách hàng
            Long customerId = authenticationHelper.getUserId();
            OrderResponse orderResponse = orderService.createOrder(customerId,createOrderRequest);
            //lưu orderid vào redis
            redisTemplate.opsForValue().set("orderId", orderResponse.getOrderId());
            // lưu phuương thức thanh toán vào redis
            redisTemplate.opsForValue().set("paymentMethod", createOrderRequest.getPaymentMethod());
            // Kiểm tra phương thức thanh toán
            if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.COD)) {
                // Cập nhật trạng thái đơn hàng
                orderService.updateOrderPaymentCOD(orderResponse.getOrderId());
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
                // Tạo liên kết thanh toán VNPay và trả về
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnpayService.createOrder(request, orderResponse.getTotal().intValue(), orderResponse.getOrderId().toString(), baseUrl);
                //lưu liên kết thanh toán vào redis
                redisTemplate.opsForValue().set("vnpayUrl", vnpayUrl);
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.PAYPAL)) {
                // Tạo liên kết thanh toán Paypal
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                //lưu liên kết thanh toán vào redis
                redisTemplate.opsForValue().set("paypalUrl", "/customer/paypal/checkout");
            }
            else{
                return ResponseEntity.badRequest().body("Invalid payment method");
            }
            //trả về trạng thái 200 và liên kết chuyển hướng
            return ResponseEntity.ok().body(Map.of("redirectUrl", "/customer/order/payment-return"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/payment-return",method = {RequestMethod.GET, RequestMethod.POST})
    public String paymentProcess(){
        //lấy ra phương thức thanh toán
        PaymentMethod paymentMethod = PaymentMethod.valueOf(redisTemplate.opsForValue().get("paymentMethod").toString());
        if (paymentMethod.equals(PaymentMethod.COD)) {
            // Cập nhật trạng thái đơn hàng
            return "user/ordersuccess";
        }else if (paymentMethod.equals(PaymentMethod.VNPAY)) {
            // Lấy liên kết thanh toán VNPay
            String vnpayUrl = (String) redisTemplate.opsForValue().get("vnpayUrl");
            // Xóa liên kết thanh toán VNPay
            redisTemplate.delete("vnpayUrl");
            // Chuyển hướng đến liên kết thanh toán VNPay
            return "redirect:" + vnpayUrl;
        } else if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
            // Lấy liên kết thanh toán Paypal
            String paypalUrl = (String) redisTemplate.opsForValue().get("paypalUrl");
            // Xóa liên kết thanh toán Paypal
            redisTemplate.delete("paypalUrl");
            // Chuyển hướng đến liên kết thanh toán Paypal
            return "redirect:" + paypalUrl;
        }
        return "err/error";
    }

}
