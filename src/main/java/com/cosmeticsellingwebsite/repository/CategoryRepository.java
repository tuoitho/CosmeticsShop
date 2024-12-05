package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);
    @Query("SELECT p.category FROM Product p WHERE p.productCode = :productCode")
    Category findCategoryByProductCode(@Param("productCode") String productCode);


}
