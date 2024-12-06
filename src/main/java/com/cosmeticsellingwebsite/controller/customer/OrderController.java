package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.CartItemForCheckoutDTO;
import com.cosmeticsellingwebsite.dto.OrderHistoryDetailDTO;
import com.cosmeticsellingwebsite.dto.ProductSnapshotDTO;
import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.CartItem;
import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.payload.request.CreateOrderRequest;
import com.cosmeticsellingwebsite.payload.response.OrderResponse;
import com.cosmeticsellingwebsite.service.impl.CartService;
import com.cosmeticsellingwebsite.service.impl.OrderService;
import com.cosmeticsellingwebsite.service.impl.PaymentService;
import com.cosmeticsellingwebsite.service.impl.UserService;
import com.cosmeticsellingwebsite.service.vnpay.VNPAYService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

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
    @Autowired
    private PaymentService paymentService;


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
        return "customer/checkout";
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateOrderRequest createOrderRequest, HttpSession session, HttpServletRequest request) {
        try {
            // Lấy thông tin khách hàng
            Long customerId = authenticationHelper.getUserId();
            OrderResponse orderResponse = orderService.createOrder(customerId,createOrderRequest);
            Long orderId = orderResponse.getOrderId();
            String redirectUrl=null;
            // Tạo token riêng cho đơn hàng
//            String orderToken = UUID.randomUUID().toString();

            // Kiểm tra phương thức thanh toán
            if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.COD)) {
                // Cập nhật trạng thái đơn hàng
                orderService.updateOrderPaymentCOD(orderResponse.getOrderId());
//                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                redirectUrl ="/customer/order/payment-return?orderId=" + orderId;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
                // Tạo liên kết thanh toán VNPay và trả về
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnpayService.createOrder(request, orderResponse.getTotal().intValue(), orderResponse.getOrderId().toString(), baseUrl);
                redirectUrl = vnpayUrl;
            } else if (createOrderRequest.getPaymentMethod().equals(PaymentMethod.PAYPAL)) {
                // Tạo liên kết thanh toán Paypal
//                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                redirectUrl = "/customer/paypal/checkout?orderId=" + orderId;
            }
            else{
                return ResponseEntity.badRequest().body("Invalid payment method");
            }

//            String redisKey = "order:" + orderToken;
//            Map<String, Object> orderData = new HashMap<>();
//            orderData.put("orderId", orderResponse.getOrderId());
//            orderData.put("paymentMethod", createOrderRequest.getPaymentMethod());
//            orderData.put("processUrl", processUrl);
//            // Lưu thông tin đơn hàng vào Redis
//            redisTemplate.opsForHash().putAll(redisKey, orderData);
//            redisTemplate.expire(redisKey, Duration.ofMinutes(30)); // Set TTL cho dữ liệu

            //trả về trạng thái 200 và redirectUrl
            return ResponseEntity.ok().body(Map.of("redirectUrl", redirectUrl));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Controller này xử lý cho phương thức thanh toán COD
    @GetMapping( "/payment-return")
    public String paymentProcess(@RequestParam("orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        //Lấy ra người dùng đã đăng nhập
        Long userId = authenticationHelper.getUserId();
        // Kiểm tra xem đơn hàng có tồn tại và có thuộc về người dùng đó không, nếu không trả về trang lỗi
        if (order == null || !order.getCustomerId().equals(userId)) {
            return "err/error";
        }
        model.addAttribute("order", order);
        return "customer/ordersuccess";
    }


    @GetMapping("/order-history")
    public String getOrderHistory(@RequestParam(value = "tab", required = false, defaultValue = "tat-ca-don-hang") String tab,Model model) {
        // Lấy thông tin người dùng từ session
        Long customerId = authenticationHelper.getUserId();
        Logger.log("customerId: " + customerId);
//        Set<PurchaseHistoryDTO> orders;
        Set<Order> orders = switch (tab) {
            case "tat-ca-don-hang" -> orderService.getAllOrders(customerId); // Lấy tất cả đơn hàng của người dùng
            case "don-cho-xac-nhan" ->
                    orderService.getOrdersByOrderStatus(customerId, OrderStatus.PENDING); // Đơn chờ xác nhận
            case "don-da-xac-nhan" ->
                    orderService.getOrdersByOrderStatus(customerId, OrderStatus.CONFIRMED); // Đơn đã xác nhận
            case "don-dang-van-chuyen" ->
                    orderService.getOrdersByOrderStatus(customerId, OrderStatus.SHIPPING); // Đơn đang vận chuyển
            case "don-da-giao" -> orderService.getOrdersByOrderStatus(customerId, OrderStatus.COMPLETED); // Đơn đã giao
            case "don-huy" -> orderService.getOrdersByOrderStatus(customerId, OrderStatus.CANCELLED);
            default -> orderService.getAllOrders(customerId); // Mặc định là tất cả đơn hàng của người dùng
        };
        // Ánh xạ trực tiếp từ "tab" sang trạng thái đơn hàng

        Logger.log(orders);
        // Gửi thông tin đến view
        model.addAttribute("orders", orders);
        model.addAttribute("tab", tab);
        return "customer/order-history";
    }

    @GetMapping("/order-history-search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam String keyword, int page) {
        Long customerId = authenticationHelper.getUserId();
        Pageable pageable = PageRequest.of(page, 3);
        List<Order> orders = orderService.searchOrders(customerId, keyword, pageable);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/search")
    public String searchOrderHistory(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        return "customer/order-history-search";
    }

    @GetMapping("/followOrder/{orderId}")
    public ModelAndView followOrder(@PathVariable("orderId") Long orderId,ModelMap model){
        Long userId = authenticationHelper.getUserId();
        //kiem tra orderId co thuoc ve userId hay khong
        if (orderService.getOrderById(orderId) == null || !orderService.getOrderById(orderId).getCustomerId().equals(userId)) {
            return new ModelAndView("err/error");
        }
        OrderHistoryDetailDTO order = orderService.getOrderHistoryDetailById(orderId);
        model.addAttribute("orderDetail", order);
        Logger.log(order.getOrderLines());
        return new ModelAndView("customer/order-history-detail",model);
    }
    @GetMapping("/{orderId}/product-detail-snapshot/{productId}")
    public String productDetailSnapshot(@PathVariable("orderId") Long orderId, @PathVariable("productId") Long productId, Model model) {
        Long userId = authenticationHelper.getUserId();
        //kiem tra orderId co thuoc ve userId hay khong
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getCustomerId().equals(userId)) {
            return "err/error";
        }
        ProductSnapshotDTO productSnapshot = orderService.getProductSnapshot(orderId, productId);
        model.addAttribute("productSnapshot", productSnapshot);
        return "customer/product-snapshot";
    }


    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam("orderId") @NotNull Long orderId){
        Long customerId = authenticationHelper.getUserId();
        //kiem tra orderId co thuoc ve userId hay khong
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getCustomerId().equals(customerId)) {
            return ResponseEntity.badRequest().body("Đơn hàng không tồn tại hoặc không thuộc về bạn");
        }
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().body("Đã hủy đơn hàng thành công");
    }

    @GetMapping("/success")
    public String success() {
        return "customer/ordersuccess";
    }


}
