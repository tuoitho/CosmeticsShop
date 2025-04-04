package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.dto.ProductSearchDTO;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //top 20 sản phẩm mới nhất
    List<Product> findTop20ByOrderByCreatedDateDesc();

    //top 20 sản phẩm bán chạy nhất ( lấy giảm dần theo tỉ lệ, số lượng đã bán trong tháng)
//    @Query(value = """
//
//            SELECT p.*
//            FROM orderline ol
//            JOIN `order` o ON ol.orderId = o.orderId
//            JOIN product p ON ol.productId = p.productId
//            WHERE o.orderDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
//            AND o.orderstatus='COMPLETED'
//            GROUP BY p.productId , p.productName
//            ORDER BY SUM(ol.quantity) DESC
//            LIMIT 20
//            """,nativeQuery = true)
    @Query(value = """
            
            SELECT p.*
            FROM orderline ol
            JOIN `order` o ON ol.orderId = o.orderId
            JOIN product p ON ol.productId = p.productId
            WHERE o.orderDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY p.productId , p.productName
            ORDER BY SUM(ol.quantity) DESC
            LIMIT 20
            """,nativeQuery = true)
    List<Product> findTop20BestSellingProducts();

    //lấy ra số lượng sản phẩm đã bán trong 30 ngày qua bằng productId
//    @Query(value = """
//            SELECT SUM(ol.quantity) AS total_quantity_sold
//            FROM orderline ol
//            JOIN `order` o ON ol.orderId = o.orderId
//            JOIN product p ON ol.productId = p.productId and p.productId = :productId
//            WHERE o.orderDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
//            and o.orderstatus='COMPLETED'
//            GROUP BY p.productId , p.productName
//            """,nativeQuery = true)
    @Query(value = """
            SELECT SUM(ol.quantity) AS total_quantity_sold
            FROM orderline ol
            JOIN `order` o ON ol.orderId = o.orderId
            JOIN product p ON ol.productId = p.productId and p.productId = :productId
            WHERE o.orderDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY p.productId , p.productName
            """,nativeQuery = true)
    Integer countSoldLast30DaysByProductId(Long productId);

    //lay top 20 san pham rating cao nhat
    @Query(value = """
            SELECT p.*
            FROM product p
            JOIN productfeedback pf ON p.productId = pf.productId
            GROUP BY p.productId , p.productName
            ORDER BY AVG(pf.rating) DESC
            LIMIT 20
            """,nativeQuery = true)
    List<Product> findTop20HighestRatedProducts();


    Optional<Product> findByProductCode(String productCode);

    boolean existsByProductCode(String productCode);

    List<Product> findAllByProductCodeIn(Set<String> productCodes);

    Integer countByCategory_CategoryId(Long categoryId);

    List<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    List<Product> findByActiveTrue();


    boolean existsByCategory_CategoryId(Long id);

    Page<Product> findByProductNameContaining(String productName, Pageable pageable);
    Page<Product> findByProductNameContainingAndActive(String productName, Boolean active, Pageable pageable);


    @Query("""
            SELECT p
            FROM Product p
            WHERE p.productName LIKE %:keywords%
            AND p.active = true
            and p.cost >= :minPrice
            and p.cost <= :maxPrice
            and p.brand LIKE %:brand%
            and p.origin LIKE %:origin%
            and p.category.categoryName LIKE %:category%
    """)
    Page<Product> searchProductsWithFilter(String keywords, Double minPrice, Double maxPrice, String brand, String origin, String category, Pageable pageable);

    @Query("""
            SELECT distinct p.origin
            FROM Product p
    """)
    List<String> findAllOrigins();


    @Query("""
            SELECT distinct p.brand
            FROM Product p
    """)
    List<String> findAllBrands();


    @Query(value = """
             SELECT p
              FROM OrderLine ol
              JOIN Order o ON ol.order.orderId = o.orderId
              JOIN Product p ON ol.product.productId = p.productId
              WHERE o.orderDate >= :startDate
              GROUP BY p.productId , p.productName
              ORDER BY SUM(ol.quantity) DESC
            """)
    Page<Product> findTop20BestSellingProducts(Pageable pageable, @NotNull LocalDateTime startDate);

    //top 20 sản phẩm mới nhất

    @Query(value = """
            SELECT *
            FROM product p
            ORDER BY p.createdDate DESC
            """,nativeQuery = true)
    Page<Product> findAllNewest(Pageable pageable);

    @Query(value = """
            SELECT product.*
            FROM product
            JOIN productfeedback pf ON product.productId = pf.productId
            GROUP BY product.productId , product.productName
            ORDER BY AVG(pf.rating) DESC
            """,nativeQuery = true)
    Page<Product> findTop20HighestRatedProducts(Pageable pageable);
}
