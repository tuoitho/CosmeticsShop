package com.cosmeticsellingwebsite.controller.Payment;


import com.cosmeticsellingwebsite.service.impl.OrderService;
import com.cosmeticsellingwebsite.service.impl.PaymentService;
import com.cosmeticsellingwebsite.service.vnpay.VNPAYService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


//Ngân hàng: NCB
//Số thẻ: 9704198526191432198
//Tên chủ thẻ:NGUYEN VAN A
//Ngày phát hành:07/15
//Mật khẩu OTP:123456
@Controller
public class VNPayController {
    @Autowired
    private VNPAYService vnPayService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        Long orderId = Long.parseLong(request.getParameter("vnp_TxnRef"));
        model.addAttribute("orderId", orderId);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
        if (paymentStatus == 1) {
            Logger.log(orderId.toString());
            orderService.updateOrderStatusPaymentTime(orderId, paymentTime);
            return "user/ordersuccess";
        } else {
            return "user/orderfail";
        }
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }


}