package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
