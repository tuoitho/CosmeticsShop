package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //top 20 sản phẩm mới nhất
    List<Product> findTop20ByOrderByCreatedDateDesc();

    //top 20 sản phẩm bán chạy nhất ( lấy giảm dần theo tỉ lệ, số lượng đã bán trong tháng)
    @Query(value = """
            
            SELECT p.*
            FROM orderline ol
            JOIN `order` o ON ol.orderId = o.orderId
            JOIN product p ON ol.productId = p.productId
            WHERE o.orderDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            AND o.orderstatus='COMPLETED'
            GROUP BY p.productId , p.productName
            ORDER BY SUM(ol.quantity) DESC
            LIMIT 20
            """,nativeQuery = true)
    List<Product> findTop20BestSellingProducts();

    //lấy ra số lượng sản phẩm đã bán trong 30 ngày qua bằng productId
    @Query(value = """
            SELECT SUM(ol.quantity) AS total_quantity_sold
            FROM orderline ol
            JOIN `order` o ON ol.orderId = o.orderId
            JOIN product p ON ol.productId = p.productId and p.productId = :productId
            WHERE o.orderDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            and o.orderstatus='COMPLETED'
            GROUP BY p.productId , p.productName
            """,nativeQuery = true)
    Integer countSoldLast30DaysByProductId(Long productId);

    Optional<Product> findByProductCode(String productCode);

    boolean existsByProductCode(String productCode);

    List<Product> findAllByProductCodeIn(Set<String> productCodes);

    Integer countByCategory_CategoryId(Long categoryId);

    List<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    List<Product> findByActiveTrue();



}
