package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductStockService {

    List<ProductStock> getAllProductStocks();

    ProductStock updateProductStock(Long productId, Long addedQuantity);

    ProductStock addNewProductStock(Product product, Long initialQuantity);

    Page<ProductStock> getPaginatedProductStocks(Pageable pageable);

    Page<ProductStock> searchProductStocks(String searchTerm, Pageable pageable);
}
