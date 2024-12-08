package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.dto.OrderHistoryDetailDTO;
import com.cosmeticsellingwebsite.dto.ProductSnapshotDTO;
import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.payload.request.CreateOrderRequest;
import com.cosmeticsellingwebsite.payload.response.OrderResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


public interface IOrderService{
    @Transactional
    OrderResponse createOrderForSingleProduct(Long userId, CreateOrderRequest createOrderRequest);

    void cancelOrder(Long orderId);

    OrderResponse updateOrderStatus(@Valid Long orderId, @Valid String status);

    void updateOrderStatus(Long orderId);

    Double getOrderTotal(Long orderId);

    void updateOrderStatusPaymentTime(Long orderId, String paymentTime);

    void updateOrderPaymentCOD(Long orderId);

    Order getOrderById(Long orderId);

    Set<Order> getAllOrders(Long customerId);

    Page<Order> getAllOrders(Long customerId, Pageable pageable);

    Set<Order> getOrdersByOrderStatus(Long customerId, OrderStatus orderStatus);

    List<Order> searchOrders(Long customerId, String keyword, Pageable pageable);

    OrderHistoryDetailDTO getOrderHistoryDetailById(Long orderId);

    ProductSnapshotDTO getProductSnapshot(Long orderId, Long productId);

    List<Order> getTop5OrdersRecently();

    Long countPendingOrders();

    List<Order> findByCustomerId(Long customerId);

    List<Order> getOrdersByStatus(OrderStatus status);

    Order updateOrderStatus(Long orderId, OrderStatus newStatus);

    Page<Order> getPaginatedOrders(int page, int size, String searchKeyword, OrderStatus selectedStatus);

    Page<Order> getOrdersByOrderStatus(Long customerId, OrderStatus orderStatus, Pageable pageable);

    void updateOrderStatusWithContent(Long id, OrderStatus newStatus, String content);

    // Lấy danh sách đơn hàng với phân trang
}
