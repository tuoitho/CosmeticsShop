package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    public void deleteProduct(Long productId);
    public void disableProduct(Long productId);
    public void activateProduct(Long productId);
    Page<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long productId);
    void addProduct(Product product);
    public void updateProduct(Product product);
    public boolean existsByProductCode(String productCode);
    List<Category> getAllCategories();

}
