package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'COMPLETED' AND o.payment.paymentDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersWithShippingStatusAndReceiveDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
