package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Category;
import org.codehaus.groovy.transform.LogASTTransformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);
    @Query("SELECT p.category FROM Product p WHERE p.productCode = :productCode")
    Category findCategoryByProductCode(@Param("productCode") String productCode);

    Page<Category> findByCategoryNameContaining(String categoryName, Pageable pageable);


    @Query("""
            SELECT count(p) FROM Order o join OrderLine ol on o.orderId = ol.order.orderId
            join Product p on ol.product.productId = p.productId 
            where p.category.categoryId = :categoryId
            and o.orderDate BETWEEN :startDate AND :endDate
            """)
    Long findTotalSoldByCategoryId(@Param("categoryId") Long categoryId,
                       @Param("startDate") LocalDateTime startDate,
                       @Param("endDate") LocalDateTime endDate);
}
