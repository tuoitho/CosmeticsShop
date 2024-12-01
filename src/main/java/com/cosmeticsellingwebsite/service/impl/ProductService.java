package com.cosmeticsellingwebsite.service.impl;


import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.payload.response.ProductDetailResponse;
import com.cosmeticsellingwebsite.payload.response.ProductResponse;
import com.cosmeticsellingwebsite.repository.*;
import com.cosmeticsellingwebsite.service.interfaces.IProductService;
import jakarta.persistence.EntityNotFoundException;
import com.cosmeticsellingwebsite.service.image.ImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    ImageService imageService;

    @Autowired
    private ProductFeedbackRepository productFeedbackRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductStockRepository productStockRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    public List<ProductResponse> findAllProduct(Pageable page) {
        List<Product> products = productRepository.findAll(page).getContent();
        return products.stream()
                .map(product -> {
                    ProductResponse productResponse = new ProductResponse();
                    BeanUtils.copyProperties(product, productResponse);
                    productResponse.setStock(getStockByProductCode(product.getProductCode()));
                    productResponse.setCategory(product.getCategory().getCategoryName());
                    return productResponse;
                })
                .collect(Collectors.toList());

    }

    public Long getStockByProductCode(String productCode) {
        Optional<ProductStock> pro = productStockRepository.findByProduct_ProductCode(productCode);
        return pro.map(productStock -> Long.parseLong(String.valueOf(productStock.getQuantity()))).orElse(0L);
    }

    public Long count() {
        return productRepository.count();
    }


//    public ProductResponse addProduct(AddProductRequest createProductRequest) {
//        if (productRepository.existsByProductCode(createProductRequest.getProductCode())) {
//            throw new RuntimeException("Product code already exists");
//        }
//        Product product = new Product();
//        Long quantity = createProductRequest.getStock();
//        BeanUtils.copyProperties(createProductRequest, product);
//        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//        product.setCategory(category);
//
//
//        Product product1=productRepository.save(product);
//        ProductStock productStock = new ProductStock();
//        productStock.setQuantity(quantity);
//        productStock.setProduct(product1);
//        productStockRepository.save(productStock);
//        ProductResponse productResponse = new ProductResponse();
//        BeanUtils.copyProperties(product, productResponse);
//        productResponse.setStock(quantity);
//        productResponse.setCategory(categoryRepository.findById(createProductRequest.getCategoryId()).get().getCategoryName());
//        return productResponse;
//    }
//    @Transactional
//    public void deleteProduct(Long id) {
//        Optional<Product> productOptional = productRepository.findById(id);
//        if (productOptional.isEmpty()) {
//            throw new RuntimeException("Product not found");
//        }
//        Product product = productOptional.get();
////        kiem tra xem product co trong order hay cart hay khong
//        if (orderLineRepository.existsByProduct(product) || cartItemRepository.existsByProduct(product)) {
//            product.setActive(false);
//            productRepository.save(product);
//            return;
//        }
////        delete stock
//        productStockRepository.deleteByProduct(product);
//        productRepository.delete(product);
//    }
//
//    public ProductResponse updateProduct(AddProductRequest addProductRequest) {
//        Product product = productRepository.findByProductCode(addProductRequest.getProductCode())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        Long quantity = addProductRequest.getStock();
//        BeanUtils.copyProperties(addProductRequest, product);
//        Category category = categoryRepository.findById(addProductRequest.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//        product.setCategory(category);
//        Product product1=productRepository.save(product);
//        ProductStock productStock = productStockRepository.findByProduct_ProductCode(product1.getProductCode())
//                .orElseThrow(() -> new RuntimeException("ProductStock not found"));
//        productStock.setQuantity(quantity);
//        productStockRepository.save(productStock);
//        ProductResponse productResponse = new ProductResponse();
//        BeanUtils.copyProperties(product, productResponse);
//        productResponse.setStock(quantity);
//        productResponse.setCategory(categoryRepository.findById(addProductRequest.getCategoryId()).get().getCategoryName());
//        return productResponse;
//    }

    public ProductDetailResponse getProductDetail(String productCdoe) {
        Product product = productRepository.findByProductCode(productCdoe)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        BeanUtils.copyProperties(product, productDetailResponse);
        productDetailResponse.setStock(getStockByProductCode(productCdoe));
        String category = categoryRepository.findCategoryByProductCode(productCdoe).getCategoryName();
        productDetailResponse.setCategory(category);

        productDetailResponse.setTotalFeedback(productFeedbackRepository.countByProduct(product));
        productDetailResponse.setAverageRating(productFeedbackRepository.findByProduct(product).stream()
                .mapToDouble(ProductFeedback::getRating)
                .average()
                .orElse(0.0));
//        TODO: cần check order đã xong hay bị hủy
        Long totalSold = orderLineRepository.sumQuantityByProduct(product).orElse(0L);
        productDetailResponse.setTotalSold(totalSold);
        return productDetailResponse;
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (orderRepository.existsByOrderLines_Product_ProductId(productId) || cartRepository.existsByCartItems_Product_ProductId(productId)) {
            product.setActive(false);
            productRepository.save(product);
        } else {
            productRepository.delete(product);
        }

    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);

    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        existingProduct.setProductName(product.getProductName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setCost(product.getCost());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setManufactureDate(product.getManufactureDate());
        existingProduct.setExpirationDate(product.getExpirationDate());
        existingProduct.setIngredient(product.getIngredient());
        existingProduct.setHow_to_use(product.getHow_to_use());
        existingProduct.setVolume(product.getVolume());
        existingProduct.setOrigin(product.getOrigin());
        existingProduct.setImage(product.getImage());

        productRepository.save(existingProduct); // Lưu sản phẩm
    }

    @Override
    public boolean existsByProductCode(String productCode) {
        return productRepository.existsByProductCode(productCode);
    }

    @Override
    public List<Category> getAllCategories() {
        return List.of();
    }
}
