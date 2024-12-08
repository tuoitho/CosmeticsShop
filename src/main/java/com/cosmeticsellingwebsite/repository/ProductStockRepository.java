package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductStock;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    Optional<ProductStock> findByProduct_ProductCode(String productCode);

    void deleteByProduct(Product product);

    Optional<ProductStock> findByProduct_ProductId(Long productId);

    @Query("""
        SELECT ps FROM ProductStock ps 
        WHERE LOWER(ps.product.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(ps.product.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        """)
    Page<ProductStock> searchByProductNameOrCode(@Param("searchTerm") String searchTerm, Pageable pageable);

}
