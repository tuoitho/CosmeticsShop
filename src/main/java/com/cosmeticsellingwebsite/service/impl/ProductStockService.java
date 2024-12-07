package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductStock;
import com.cosmeticsellingwebsite.repository.ProductStockRepository;
import com.cosmeticsellingwebsite.service.interfaces.IProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductStockService implements IProductStockService {
    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<ProductStock> getAllProductStocks() {
        return productStockRepository.findAll();
    }

    @Override
    public ProductStock updateProductStock(Long productId, Long addedQuantity) {
        ProductStock productStock = productStockRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productStock.setQuantity(productStock.getQuantity() + addedQuantity);
        return productStockRepository.save(productStock);
    }

    @Override
    public ProductStock addNewProductStock(Product product, Long initialQuantity) {
        ProductStock productStock = new ProductStock();
        productStock.setProduct(product);
        productStock.setQuantity(initialQuantity);
        return productStockRepository.save(productStock);
    }

    public Page<ProductStock> getPaginatedProductStocks(Pageable pageable) {
        return productStockRepository.findAll(pageable);
    }

    public Page<ProductStock> searchProductStocks(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm, trả về toàn bộ danh sách có phân trang
            return productStockRepository.findAll(pageable);
        }
        // Nếu có từ khóa tìm kiếm, thực hiện tìm kiếm
        return productStockRepository.searchByProductNameOrCode(searchTerm, pageable);
    }
}
