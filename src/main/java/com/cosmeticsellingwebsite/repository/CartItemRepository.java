package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.CartItem;
import com.cosmeticsellingwebsite.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByProduct(Product product);
}
