package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderLines_Product_ProductId(Long productId);

    Set<Order> findAllByCustomerId(Long customerId);

    Set<Order> findAllByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    @Query(value = """
                    SELECT sum(ol.quantity)
                    FROM `Order` o
                    JOIN `OrderLine` ol ON o.orderId = ol.orderId
                    JOIN `Product` p ON ol.productId = p.productId
                    WHERE o.orderStatus = 'COMPLETED'
                    AND p.productId = :productId
            """, nativeQuery = true)
    Optional<Long> findTotalQuantitySoldByProductId(Long productId);
}
