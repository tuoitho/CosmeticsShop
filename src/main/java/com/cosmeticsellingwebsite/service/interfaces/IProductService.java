package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.dto.FeedbackDTO;
import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.payload.request.AddProductFeedbackReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    String getExistingImage(Long productId);

    Optional<Product> findById(Long id);

    void addFeedback(Long customerId, AddProductFeedbackReq addProductFeedbackReq);

    FeedbackDTO getFeedback(Long customerId, Long orderId, Long productId);

    Page<Product> searchAndFilterProducts(String searchKeyword, Boolean active, Pageable pageable);
}
