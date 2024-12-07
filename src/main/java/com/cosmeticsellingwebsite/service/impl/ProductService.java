package com.cosmeticsellingwebsite.service.impl;


import com.cosmeticsellingwebsite.dto.FeedbackDTO;
import com.cosmeticsellingwebsite.dto.ProductHomeDTO;
import com.cosmeticsellingwebsite.dto.ProductSearchDTO;
import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.payload.request.AddProductFeedbackReq;
import com.cosmeticsellingwebsite.payload.response.ProductDetailResponse;
import com.cosmeticsellingwebsite.payload.response.ProductResponse;
import com.cosmeticsellingwebsite.repository.*;
import com.cosmeticsellingwebsite.service.interfaces.IProductService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.persistence.EntityNotFoundException;
import com.cosmeticsellingwebsite.service.image.ImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Autowired
    private UserRepository userRepository;

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

    public List<ProductHomeDTO> getTop20NewestProducts() {
        return productRepository.findTop20ByOrderByCreatedDateDesc().stream()
                .map(product -> {
                    ProductHomeDTO productHomeDTO = new ProductHomeDTO();
                    BeanUtils.copyProperties(product, productHomeDTO);
                    return productHomeDTO;
                })
                .collect(Collectors.toList());
    }
    public List<ProductHomeDTO> getTop20BestSellingProducts() {
        return productRepository.findTop20BestSellingProducts().stream()
                .map(product -> {
                    ProductHomeDTO productHomeDTO = new ProductHomeDTO();
                    BeanUtils.copyProperties(product, productHomeDTO);
                    productHomeDTO.setTotalSoldLast30Days(productRepository.countSoldLast30DaysByProductId(product.getProductId()));
                    return productHomeDTO;
                })
                .collect(Collectors.toList());
    }
    public List<ProductHomeDTO> getTop20HighestRatedProducts() {
        return productRepository.findTop20HighestRatedProducts().stream()
                .map(product -> {
                    ProductHomeDTO productHomeDTO = new ProductHomeDTO();
                    BeanUtils.copyProperties(product, productHomeDTO);
                    productHomeDTO.setAverageRating(productFeedbackRepository.findAverageRatingByProduct_ProductId(product.getProductId()));
                    return productHomeDTO;
                })
                .collect(Collectors.toList());
    }
    public ProductDetailResponse getProductDetail(String productCdoe) {
        Product product = productRepository.findByProductCode(productCdoe)
                .orElseThrow(() -> new CustomException("Product not found"));

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
        Long totalSold = orderRepository.findTotalQuantitySoldByProductId(product.getProductId()).orElse(0L);
        //get so luong da ban, don hang da hoan thanh
        productDetailResponse.setTotalSold(totalSold);
        return productDetailResponse;
    }

    public ProductDetailResponse getProductDetailById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found"));

        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        BeanUtils.copyProperties(product, productDetailResponse);
        productDetailResponse.setStock(product.getProductStock().getQuantity());
        String category = product.getCategory().getCategoryName();
        productDetailResponse.setCategory(category);

        productDetailResponse.setTotalFeedback(productFeedbackRepository.countByProduct(product));
        productDetailResponse.setAverageRating(productFeedbackRepository.findByProduct(product).stream()
                .mapToDouble(ProductFeedback::getRating)
                .average()
                .orElse(0.0));
        Long totalSold = orderRepository.findTotalQuantitySoldByProductId(product.getProductId()).orElse(0L);
        //get so luong da ban, don hang da hoan thanh
        productDetailResponse.setTotalSold(totalSold);
        List<ProductFeedback> productFeedbacks = productFeedbackRepository.findByProduct(product);
        Set<FeedbackDTO> feedbackDTOS = productFeedbacks.stream()
                .map(productFeedback -> {
                    FeedbackDTO feedbackDTO = new FeedbackDTO();
                    BeanUtils.copyProperties(productFeedback, feedbackDTO);
                    User customer = userRepository.findById(productFeedback.getCustomerId()).orElseThrow(() -> new CustomException("Customer not found at ProductService"));
                    feedbackDTO.setCustomerName(customer.getFullname());
                    return feedbackDTO;
                })
                .collect(Collectors.toSet());
        productDetailResponse.setProductFeedbacks(feedbackDTOS);

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
    public void disableProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setActive(true);
        productRepository.save(product);
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
                .orElseThrow(() -> new CustomException("Sản phẩm không tồn tại"));

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
        return categoryRepository.findAll();
    }

    @Override
    public String getExistingImage(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"))
                .getImage();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void addFeedback(Long customerId, AddProductFeedbackReq addProductFeedbackReq) {
        Long orderId = addProductFeedbackReq.getOrderId();
        Long productId = addProductFeedbackReq.getProductId();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found"));
        //check status of order
        if (!order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
            throw new CustomException("You can only review products in delivered orders");
        }
        if (!orderRepository.existsByOrderIdAndOrderLines_Product_ProductId(orderId, productId)) {
            throw new CustomException("You have not ordered this product on this order");
        }
        // chỉ cho phép review 1 lần
        if (productFeedbackRepository.existsByCustomerIdAndOrderIdAndProduct_ProductId(customerId, orderId, productId)) {
            throw new CustomException("You have reviewed this product");
        }
        ProductFeedback productFeedback = new ProductFeedback();
        productFeedback.setCustomerId(customerId);
        productFeedback.setOrderId(orderId);
        productFeedback.setProduct(productRepository.findById(productId).get());
        productFeedback.setRating(addProductFeedbackReq.getRating());
        productFeedback.setComment(addProductFeedbackReq.getComment());
        productFeedback.setFeedbackDate(LocalDateTime.now());
        productFeedback.setImage(addProductFeedbackReq.getImage());
        String productSnapshotName = orderRepository.findById(orderId).get().getOrderLines().stream()
                .filter(orderLine -> orderLine.getProduct().getProductId().equals(productId))
                .findFirst()
                .get()
                .getProductSnapshot().get("productName").toString();
        productFeedback.setProductSnapshotName(productSnapshotName);
        productFeedbackRepository.save(productFeedback);
    }

    @Override
    public FeedbackDTO getFeedback(Long customerId, Long orderId, Long productId) {
//        Logger.log(customerId, orderId, productId);
//        Logger.log(productFeedbackRepository.findByCustomerIdAndOrderIdAndProduct_ProductId(customerId, orderId, productId));
        ProductFeedback productFeedback = productFeedbackRepository.findByCustomerIdAndOrderIdAndProduct_ProductId(customerId, orderId, productId)
                .orElse(null);
        Logger.log(productFeedback);
        if (productFeedback == null) {
            return null;
        }
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        BeanUtils.copyProperties(productFeedback, feedbackDTO);
        return feedbackDTO;
    }

    public Product getProductByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new CustomException("Product not found"));
    }

    @Override
    public Page<Product> searchAndFilterProducts(String searchKeyword, Boolean active, Pageable pageable) {
        if (active != null) {
            return productRepository.findByProductNameContainingAndActive(searchKeyword, active, pageable);
        } else {
            return productRepository.findByProductNameContaining(searchKeyword, pageable);
        }
    }


    public Page<ProductSearchDTO> searchProducts(String keywords, Double minPrice, Double maxPrice, String brand, String origin, String category, Pageable pageable) {
        Page<Product> products= productRepository.searchProductsWithFilter(keywords, minPrice, maxPrice, brand, origin, category, pageable);
        //anh xa tu product sang productSearchDTO
        return products.map(product -> {
            ProductSearchDTO productSearchDTO = new ProductSearchDTO();
            BeanUtils.copyProperties(product, productSearchDTO);
            productSearchDTO.setRatingAverage(productFeedbackRepository.findAverageRatingByProduct_ProductId(product.getProductId()));
            productSearchDTO.setSellCount(orderRepository.findTotalQuantitySoldByProductId(product.getProductId()).orElse(0L));
            return productSearchDTO;
        });
    }
}
