package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductFeedbackRepository extends JpaRepository<ProductFeedback, Long> {

    @Query("SELECT AVG(pf.rating) FROM ProductFeedback pf WHERE pf.product.productId = :productId")
    Double findAverageRatingByProduct_ProductId(Long productId);

    List<ProductFeedback> findAllByProduct_productCode(String productCode);
    Set<ProductFeedback> findAllByProduct_ProductId(Long productId);

    Set<ProductFeedback> findAllByProduct(Product product);

    List<ProductFeedback> findByProduct(Product product);

    Long countByProduct(Product product);

    boolean existsByCustomerIdAndOrderId(Long userId, Long orderId);

    boolean existsByCustomerIdAndOrderIdAndProduct_ProductId(Long userId, Long orderId, Long productId);

    Optional<ProductFeedback> findByCustomerIdAndOrderIdAndProduct_ProductId(Long customerId, Long orderId, Long productId);

    // Tìm kiếm theo orderId và fullname (từ Customer)
    @Query("SELECT pf FROM ProductFeedback pf " +
            "JOIN Customer c ON pf.customerId = c.userId " +
            "WHERE c.fullname LIKE %:keyword%")
    Page<ProductFeedback> searchFeedbacks(@Param("keyword") String keyword, Pageable pageable);

    // Lọc feedback đã phản hồi
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.feedbackResponse IS NOT NULL")
    Page<ProductFeedback> findByFeedbackResponseIsNotNull(Pageable pageable);

    // Lọc feedback chưa phản hồi
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.feedbackResponse IS NULL")
    Page<ProductFeedback> findByFeedbackResponseIsNull(Pageable pageable);

    // Lọc feedback đã phản hồi và tìm kiếm
    @Query("SELECT pf FROM ProductFeedback pf " +
            "JOIN Customer c ON pf.customerId = c.userId " +
            "WHERE pf.feedbackResponse IS NOT NULL " +
            "AND (c.fullname LIKE %:keyword%)")
    Page<ProductFeedback> findByFeedbackResponseIsNotNullAndKeyword(
            @Param("keyword") String keyword, Pageable pageable);

    // Lọc feedback chưa phản hồi và tìm kiếm
    @Query("SELECT pf FROM ProductFeedback pf " +
            "JOIN Customer c ON pf.customerId = c.userId " +
            "WHERE pf.feedbackResponse IS NULL " +
            "AND (c.fullname LIKE %:keyword%)")
    Page<ProductFeedback> findByFeedbackResponseIsNullAndKeyword(
            @Param("keyword") String keyword, Pageable pageable);
}
