package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductStock;

import java.util.List;

public interface IProductStockService {

    List<ProductStock> getAllProductStocks();

    ProductStock updateProductStock(Long productId, Long addedQuantity);

    ProductStock addNewProductStock(Product product, Long initialQuantity);
}
