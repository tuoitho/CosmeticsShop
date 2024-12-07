package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.entity.Payment;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);

    Optional<Payment> findByOrder_OrderId(@Valid Long orderId);

}
