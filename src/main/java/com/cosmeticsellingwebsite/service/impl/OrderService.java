package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.CartItemForOrderDTO;
import com.cosmeticsellingwebsite.dto.OrderLineForOrderDTO;
import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.enums.PaymentStatus;
import com.cosmeticsellingwebsite.payload.response.OrderResponse;
import com.cosmeticsellingwebsite.repository.*;
import com.cosmeticsellingwebsite.service.interfaces.IOrderService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    // TODO: Cần trừ giảm số lượng sp khi order
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductStockRepository productStockRepository;

//    @Transactional
//    public OrderResponse createOrder(@Valid CreateOrderRequest createOrderRequest) {
//        // TODO: chưa xử lý trường hợp tranh nhau đặt hàng
//
//        // TODO: cân nhắc orderresponse nên trả về chi tiết payment thay vì chỉ tên phương thức payment
//
//        // Tìm người dùng
//        User user = userRepository.findById(createOrderRequest.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Khởi tạo đơn hàng và các dòng đơn hàng
//        Order order = new Order();
//        Set<CartItemForOrderDTO> cartItemForOrderDTOS = createOrderRequest.getCartItemForOrderDTOS();
//        Set<OrderLine> orderLines = new LinkedHashSet<>();
//
//        // Lấy danh sách mã sản phẩm và tạo bản đồ các sản phẩm
//        Set<String> productCodes = cartItemForOrderDTOS.stream()
//                .map(CartItemForOrderDTO::getProductCode)
//                .collect(Collectors.toSet());
//        Map<String, Product> productMap = productRepository.findAllByProductCodeIn(productCodes).stream()
//                .collect(Collectors.toMap(Product::getProductCode, Function.identity()));
//
//        // Lặp qua các mặt hàng trong giỏ hàng và tạo OrderLine
//        double total = 0.0;
//        for (var cartItem : cartItemForOrderDTOS) {
//            Product product = Optional.ofNullable(productMap.get(cartItem.getProductCode()))
//                    .orElseThrow(() -> new RuntimeException("Product " + cartItem.getProductCode() + " not found"));
////check quantity
//            if (productService.getStockByProductCode(product.getProductCode()) < cartItem.getQuantity()) {
//                throw new RuntimeException("Product " + product.getProductCode() + " out of stock");
//            }
////            tru so luong san pham
//            ProductStock productStock = productStockRepository.findByProduct_ProductCode(product.getProductCode()).orElseThrow(() -> new RuntimeException("Product stock not found"));
//            productStock.setQuantity(productStock.getQuantity() - cartItem.getQuantity());
//            productStockRepository.save(productStock);
//
//            OrderLine orderLine = new OrderLine();
//            orderLine.setProduct(product);
//            orderLine.setQuantity(cartItem.getQuantity());
//
//            // Tạo snapshot cho sản phẩm
//            Map<String, Object> productSnapshot = new HashMap<>();
//            productSnapshot.put("productId", product.getProductId());
//            productSnapshot.put("productCode", product.getProductCode());
//            productSnapshot.put("productName", product.getProductName());
//            productSnapshot.put("cost", product.getCost());
//            productSnapshot.put("description", product.getDescription());
//            productSnapshot.put("brand", product.getBrand());
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            productSnapshot.put("expirationDate", product.getExpirationDate() != null ? product.getExpirationDate().format(formatter) : null);
//            productSnapshot.put("manufactureDate", product.getManufactureDate() != null ? product.getManufactureDate().format(formatter) : null);
//            productSnapshot.put("ingredient", product.getIngredient());
//            productSnapshot.put("how_to_use", product.getHow_to_use());
//            productSnapshot.put("volume", product.getVolume());
//            productSnapshot.put("origin", product.getOrigin());
//            productSnapshot.put("image", product.getImage());
//            orderLine.setProductSnapshot(productSnapshot);
//            orderLine.setOrder(order);
//            orderLines.add(orderLine);
//
//            // Cộng dồn tổng giá trị
//            total += product.getCost() * cartItem.getQuantity();
//        }
//        // Thiết lập các thuộc tính cho đơn hàng
//        order.setOrderLines(orderLines);
//        order.setTotal(total);
//        order.setCustomerId(user.getUserId());
//        order.setOrderDate(LocalDateTime.ofInstant(new Date().toInstant(), TimeZone.getDefault().toZoneId()));
//        order.setOrderStatus(OrderStatus.PENDING);
//
//        // Cập nhật địa chỉ giao hàng
//        ShippingAddress shippingAddress = new ShippingAddress();
//        shippingAddress.setOrder(order);
//        BeanUtils.copyProperties(createOrderRequest.getAddressForOrderDTO(), shippingAddress);
//        order.setShippingAddress(shippingAddress);
//
//        // Lưu đơn hàng và tạo phản hồi
//        Order savedOrder = orderRepository.save(order);
//        OrderResponse createOrderResponse = new OrderResponse();
//        BeanUtils.copyProperties(savedOrder, createOrderResponse);
//
////        xử lý OrderLineForOrderDTO cho phan hoi
//        Set<OrderLineForOrderDTO> orderLineForOrderDTOS = savedOrder.getOrderLines().stream().map(x -> {
//            OrderLineForOrderDTO dto = new OrderLineForOrderDTO();
//            dto.setProductCode(x.getProduct().getProductCode());
//            dto.setProductSnapshot(x.getProductSnapshot());
//            dto.setQuantity(x.getQuantity());
//            return dto;
//        }).collect(Collectors.toSet());
//        createOrderResponse.setOrderLines(orderLineForOrderDTOS);
//
//        // Lưu Payment và thiết lập phương thức thanh toán cho phản hồi
//        Payment payment = new Payment();
//        payment.setOrder(savedOrder);
//        payment.setTotal(total);
//        payment.setPaymentStatus(PaymentStatus.PENDING);
//        payment.setPaymentMethod(createOrderRequest.getPaymentMethod());
//        paymentRepository.save(payment);
//        createOrderResponse.setPaymentMethod(payment.getPaymentMethod());
//
//        return createOrderResponse;
//    }
//
//    @Transactional
//    public OrderResponse updateOrder(@Valid UpdateOrderRequest updateOrderRequest) {
//        Order order = orderRepository.findById(updateOrderRequest.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
//        Logger.log("Order found: " + order.getOrderLines());
//        ShippingAddress shippingAddress = new ShippingAddress();
//        BeanUtils.copyProperties(updateOrderRequest.getAddress(), shippingAddress);
//        order.setShippingAddress(shippingAddress);
//        Order savedOrder = orderRepository.save(order);
//
//        Payment payment = paymentRepository.findByOrder(order).orElseThrow(() -> new RuntimeException("Payment not found"));
//        payment.setPaymentMethod(updateOrderRequest.getPaymentMethod());
//        paymentRepository.save(payment);
//
//        OrderResponse createOrderResponse = new OrderResponse();
//        BeanUtils.copyProperties(savedOrder, createOrderResponse);
//        //        xử lý OrderLineForOrderDTO cho phan hoi
//        Set<OrderLineForOrderDTO> orderLineForOrderDTOS = savedOrder.getOrderLines().stream().map(x -> {
//            OrderLineForOrderDTO dto = new OrderLineForOrderDTO();
//            dto.setProductCode(x.getProduct().getProductCode());
//            dto.setProductSnapshot(x.getProductSnapshot());
//            dto.setQuantity(x.getQuantity());
//            return dto;
//        }).collect(Collectors.toSet());
//        createOrderResponse.setOrderLines(orderLineForOrderDTOS);
//        createOrderResponse.setPaymentMethod(payment.getPaymentMethod());
//        return createOrderResponse;
//    }

    public OrderResponse cancelOrder(@Valid Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
//        if order is not pending, throw exception
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order is not pending, cannot cancel");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
//        hoàn lại số lượng sản phẩm
        order.getOrderLines().forEach(x -> {
            ProductStock productStock = productStockRepository.findByProduct_ProductCode(x.getProduct().getProductCode()).orElseThrow(() -> new RuntimeException("Product stock not found"));
            productStock.setQuantity(productStock.getQuantity() + x.getQuantity());
            productStockRepository.save(productStock);
        });

        Order savedOrder = orderRepository.save(order);
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(savedOrder, orderResponse);
        //        xử lý OrderLineForOrderDTO cho phan hoi
        Set<OrderLineForOrderDTO> orderLineForOrderDTOS = savedOrder.getOrderLines().stream().map(x -> {
            OrderLineForOrderDTO dto = new OrderLineForOrderDTO();
            dto.setProductCode(x.getProduct().getProductCode());
            dto.setProductSnapshot(x.getProductSnapshot());
            dto.setQuantity(x.getQuantity());
            return dto;
        }).collect(Collectors.toSet());
        orderResponse.setOrderLines(orderLineForOrderDTOS);

//        xử lý payment
        Payment payment = paymentRepository.findByOrder(order).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
        orderResponse.setPaymentMethod(payment.getPaymentMethod());
        return orderResponse;
    }

    public OrderResponse updateOrderStatus(@Valid Long orderId, @Valid String status) {
//        TODO: khi thanh toan truoc va khi nhan hang can xu ly khac nhau
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        order.setOrderStatus(orderStatus);
        Order savedOrder = orderRepository.save(order);
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(savedOrder, orderResponse);
        //        xử lý OrderLineForOrderDTO cho phan hoi
        Set<OrderLineForOrderDTO> orderLineForOrderDTOS = savedOrder.getOrderLines().stream().map(x -> {
            OrderLineForOrderDTO dto = new OrderLineForOrderDTO();
            dto.setProductCode(x.getProduct().getProductCode());
            dto.setProductSnapshot(x.getProductSnapshot());
            dto.setQuantity(x.getQuantity());
            return dto;
        }).collect(Collectors.toSet());
        orderResponse.setOrderLines(orderLineForOrderDTOS);
//        SET payment
        Payment payment = paymentRepository.findByOrder(order).orElseThrow(() -> new RuntimeException("Payment not found"));
        orderResponse.setPaymentMethod(payment.getPaymentMethod());
        return orderResponse;
    }
}
