package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderLines_Product_ProductId(Long productId);
}
