package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.payload.request.AddProductRequest;
import com.cosmeticsellingwebsite.payload.response.ProductDetailResponse;
import com.cosmeticsellingwebsite.payload.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    List<ProductResponse> findAllProduct(Pageable page);
    Long getStockByProductCode(String productCode);
    Long count();
    ProductResponse addProduct(AddProductRequest createProductRequest);

    void deleteProduct(Long id);

    ProductResponse updateProduct(@Valid AddProductRequest addProductRequest);

    ProductDetailResponse getProductDetail(@Valid String productCdoe);
}
