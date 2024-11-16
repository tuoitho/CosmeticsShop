package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.OrderLine;
import com.cosmeticsellingwebsite.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {


    boolean existsByProduct(Product product);
    @Query("SELECT SUM(ol.quantity) FROM OrderLine ol WHERE ol.product = :product")
    Long sumQuantityByProduct(Product product);

}
