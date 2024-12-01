package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    boolean existsByProductCode(@NotNull(message = "Product code cannot be null") String productCode);

    List<Product> findAllByProductCodeIn(Set<String> productCodes);

    Integer countByCategory_CategoryId(Long categoryId);

    List<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    List<Product> findByActiveTrue();



}
