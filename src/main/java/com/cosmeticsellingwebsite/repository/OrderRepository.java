package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderLines_Product_ProductId(Long productId);

    boolean existsByOrderIdAndOrderLines_Product_ProductId(Long orderId, Long orderLinesProductProductId);

    Set<Order> findAllByCustomerId(Long customerId);

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.customerId = :customerId
    """)
    Page<Order> findAllPaginated(Long customerId, Pageable pageable);

    @Query("""
    SELECT o
    FROM Order o
    JOIN o.orderLines ol
    JOIN ol.product p 
    WHERE o.customerId = :customerId
      AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY o.orderDate DESC
""")
    Page<Order> searchOrdersByCustomerIdAndProductName(
            @Param("customerId") Long customerId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query(value = """
                    SELECT sum(ol.quantity)
                    FROM `Order` o
                    JOIN `OrderLine` ol ON o.orderId = ol.orderId
                    JOIN `Product` p ON ol.productId = p.productId
                    WHERE o.orderStatus = 'COMPLETED'
                    AND p.productId = :productId
            """, nativeQuery = true)
    Optional<Long> findTotalQuantitySoldByProductId(Long productId);

    Set<Order> findAllByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    List<Order> findByOrderStatus(OrderStatus status);
    List<Order> findByOrderStatusOrderByOrderDateDesc(OrderStatus status);


    @Query("SELECT o FROM Order o WHERE CAST(o.orderId AS string) LIKE CONCAT('%', :searchKeyword, '%')")
    Page<Order> findByOrderIdContaining(@Param("searchKeyword") String searchKeyword, Pageable pageable);
    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByOrderStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE CAST(o.orderId AS string) LIKE CONCAT('%', :searchKeyword, '%') AND o.orderStatus = :status order by o.orderDate desc")
    Page<Order> findByOrderIdContainingAndOrderStatus(@Param("searchKeyword") String searchKeyword,
                                                      @Param("status") OrderStatus status,
                                                      Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'COMPLETED' AND o.payment.paymentDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersWithShippingStatusAndReceiveDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Order> findByCustomerId(Long customerId);



    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC limit 5")
    List<Order> findTop5ByOrderByOrderDateDesc();

    Long countByOrderStatus(OrderStatus orderStatus);
    @Query("""
        SELECT o
        FROM Order o
        WHERE o.customerId = :customerId
            and o.orderStatus = :orderStatus
               ORDER BY o.orderDate
    """)
    Page<Order> findAllPaginatedByOrderStatus(Long customerId, OrderStatus orderStatus, Pageable pageable);

    @Query("""
        SELECT o
        FROM Order o
        ORDER BY o.orderDate DESC
    """)
    Page<Order> findAllDesc(Pageable pageable);


}
