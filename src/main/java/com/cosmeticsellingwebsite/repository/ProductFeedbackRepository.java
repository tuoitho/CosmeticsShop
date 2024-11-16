package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductFeedbackRepository extends JpaRepository<ProductFeedback, Long> {

    List<ProductFeedback> findAllByProduct_productCode(String productCode);
    Set<ProductFeedback> findAllByProduct_ProductId(Long productId);

    Set<ProductFeedback> findAllByProduct(Product product);

    List<ProductFeedback> findByProduct(Product product);
}
