package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.*;
import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import com.cosmeticsellingwebsite.enums.PaymentStatus;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.exception.EntityNotFoundException;
import com.cosmeticsellingwebsite.payload.request.CreateOrderRequest;
import com.cosmeticsellingwebsite.payload.response.OrderResponse;
import com.cosmeticsellingwebsite.repository.*;
import com.cosmeticsellingwebsite.service.interfaces.IOrderService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    // TODO: Cần trừ giảm số lượng sp khi order
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductStockRepository productStockRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    private static final String STOCK_KEY_PREFIX = "stock:product:";
    private static final String LOCK_PREFIX = "lock:product:";
    @Transactional
    @Override
    public OrderResponse createOrder(Long customerId, CreateOrderRequest createOrderRequest) {
        // TODO: xử lý trường hợp tranh nhau đặt hàng

        // Tìm người dùng
        User user = userRepository.findById(customerId).orElseThrow(() -> new CustomException("User not found"));
        // Khởi tạo đơn hàng và các dòng đơn hàng
        Order order = new Order();
        Set<CartItemForOrderDTO> cartItemForOrderDTOS = createOrderRequest.getCartItemForOrderDTOS();
        Set<OrderLine> orderLines = new LinkedHashSet<>();
        // Lấy danh sách mã sản phẩm và tạo bản đồ các sản phẩm
        Set<String> productCodes = cartItemForOrderDTOS.stream()
                .map(CartItemForOrderDTO::getProductCode)
                .collect(Collectors.toSet());
        Map<String, Product> productMap = productRepository.findAllByProductCodeIn(productCodes).stream()
                .collect(Collectors.toMap(Product::getProductCode, x -> x));

        // Lặp qua các mặt hàng trong giỏ hàng và tạo OrderLine
        double total = 0.0;
        for (var cartItem : cartItemForOrderDTOS) {
            Product product = Optional.ofNullable(productMap.get(cartItem.getProductCode()))
                    .orElseThrow(() -> new CustomException("Product " + cartItem.getProductCode() + " not found"));
            //only active product can be ordered
            if (!product.getActive()) {
                throw new CustomException("Product " + product.getProductCode() + " is not active");
            }
//check quantity
//            if (productService.getStockByProductCode(product.getProductCode()) < cartItem.getQuantity()) {
//                throw new CustomException("Product " + product.getProductCode() + " out of stock");
//            }
            String stockKey = STOCK_KEY_PREFIX + product.getProductCode();
            String lockKey = LOCK_PREFIX + product.getProductCode();
            RLock lock = redissonClient.getLock(lockKey);
            try {
                // Thử lấy khóa trong 10 giây, giữ khóa tối đa 30 giây
                if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                    try {
                        // Kiểm tra và cập nhật tồn kho trong Redis
//                        Integer currentStock = (Integer) redisTemplate.opsForValue().get(stockKey);
                        Long currentStock = (Long) redisTemplate.opsForValue().get(stockKey);
                        if (currentStock == null) {
                            // Nếu không có trong Redis, lấy từ DB và lưu vào Redis
                            Product product1 = productRepository.findById(product.getProductId())
                                    .orElseThrow(() -> new RuntimeException("Product not found"));
                            currentStock =product1.getProductStock().getQuantity();
                            redisTemplate.opsForValue().set(stockKey, currentStock);
                        }

                        if (currentStock >=cartItem.getQuantity()) {
                            // Giảm tồn kho trong Redis
                            Long newStock = redisTemplate.opsForValue().increment(stockKey, -cartItem.getQuantity());
                            if (newStock < 0) {
                                // Hoàn tác nếu tồn kho âm
                                redisTemplate.opsForValue().increment(stockKey, cartItem.getQuantity());
//                                createFailedOrder(request);
                                throw new CustomException("Product is out of stock");
                            }

                            ProductStock productStock = productStockRepository.findByProduct_ProductCode(product.getProductCode()).orElseThrow(() -> new CustomException("Product stock not found"));
                            productStock.setQuantity(productStock.getQuantity() - cartItem.getQuantity());
                            productStockRepository.save(productStock);

                            OrderLine orderLine = new OrderLine();
                            orderLine.setProduct(product);
                            orderLine.setQuantity(cartItem.getQuantity());

                            // Tạo snapshot cho sản phẩm
                            Map<String, Object> productSnapshot = new HashMap<>();
                            productSnapshot.put("productId", product.getProductId());
                            productSnapshot.put("productCode", product.getProductCode());
                            productSnapshot.put("productName", product.getProductName());
                            productSnapshot.put("cost", product.getCost());
                            productSnapshot.put("description", product.getDescription());
                            productSnapshot.put("brand", product.getBrand());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            productSnapshot.put("expirationDate", product.getExpirationDate() != null ? product.getExpirationDate().format(formatter) : null);
                            productSnapshot.put("manufactureDate", product.getManufactureDate() != null ? product.getManufactureDate().format(formatter) : null);
                            productSnapshot.put("ingredient", product.getIngredient());
                            productSnapshot.put("how_to_use", product.getHow_to_use());
                            productSnapshot.put("volume", product.getVolume());
                            productSnapshot.put("origin", product.getOrigin());
                            productSnapshot.put("image", product.getImage());
                            orderLine.setProductSnapshot(productSnapshot);
                            orderLine.setOrder(order);
                            orderLines.add(orderLine);

                            // Cộng dồn tổng giá trị
                            total += product.getCost() * cartItem.getQuantity();
                        } else {
                            throw new CustomException("Product " + product.getProductCode() + " out of stock");
                        }
                    } finally {
                        lock.unlock(); // Giải phóng khóa
                    }
                } else {
                    throw new CustomException("Could not acquire lock for product: " + product.getProductCode());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (createOrderRequest.getVoucherCodes() != null) {
            for (String voucherCode : createOrderRequest.getVoucherCodes()) {
                Voucher voucher = voucherRepository.findFirstByVoucherCodeAndUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now())
                        .orElseThrow(() -> new CustomException("Voucher " + voucherCode + " is not available"));
                voucher.setUsed(true);
                voucher.setOrder(order);
                voucherRepository.save(voucher);
                total -= voucher.getVoucherValue();
            }
        }
        // Thiết lập các thuộc tính cho đơn hàng
        order.setOrderLines(orderLines);
        order.setTotal(total);
        order.setCustomerId(user.getUserId());
        order.setOrderDate(LocalDateTime.ofInstant(new Date().toInstant(), TimeZone.getDefault().toZoneId()));
        order.setOrderStatus(OrderStatus.PENDING);

        //thêm lịch sử đơn hàng
        List<OrderStatusHistory> orderStatusHistories = new ArrayList<>();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setStatus(OrderStatus.PENDING);
        orderStatusHistory.setDescription("Đơn hàng đã được tạo");
        orderStatusHistories.add(orderStatusHistory);
        order.setOrderStatusHistories(orderStatusHistories);


        // Cập nhật địa chỉ giao hàng
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setOrder(order);
        BeanUtils.copyProperties(createOrderRequest.getAddress(), shippingAddress);
        order.setShippingAddress(shippingAddress);

        // Lưu đơn hàng và tạo phản hồi
        Order savedOrder = orderRepository.save(order);
        OrderResponse createOrderResponse = new OrderResponse();
        BeanUtils.copyProperties(savedOrder, createOrderResponse);


        // xoa cartitem
        Cart cart = cartRepository.findByCustomer_UserId(customerId).orElseThrow(() -> new CustomException("Cart not found at OrderService"));
        Set<CartItem> cartItems = cart.getCartItems();
        for (CartItemForOrderDTO cartItemForOrderDTO : cartItemForOrderDTOS) {
            cartItems.removeIf(
                    cartItem1 -> cartItem1.getProduct().getProductCode().equals(cartItemForOrderDTO.getProductCode()));
        }
        cart.setCartItems(cartItems);
        cartRepository.save(cart);


//        xử lý OrderLineForOrderDTO cho phan hoi
        Set<OrderLineForOrderDTO> orderLineForOrderDTOS = savedOrder.getOrderLines().stream().map(x -> {
            OrderLineForOrderDTO dto = new OrderLineForOrderDTO();
            dto.setProductCode(x.getProduct().getProductCode());
            dto.setProductSnapshot(x.getProductSnapshot());
            dto.setQuantity(x.getQuantity());
            return dto;
        }).collect(Collectors.toSet());
        createOrderResponse.setOrderLines(orderLineForOrderDTOS);

        // Lưu Payment và thiết lập phương thức thanh toán cho phản hồi
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setTotal(total);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod(createOrderRequest.getPaymentMethod());
        paymentRepository.save(payment);
        createOrderResponse.setPaymentMethod(payment.getPaymentMethod());

        return createOrderResponse;
    }




    @Transactional
    @Override
    public OrderResponse createOrderForSingleProduct(Long userId, CreateOrderRequest createOrderRequest) {
        // TODO: chưa xử lý trường hợp tranh nhau đặt hàng
        // Tìm người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Khởi tạo đơn hàng và các dòng đơn hàng
        Order order = new Order();
        Set<CartItemForOrderDTO> cartItemForOrderDTOS = createOrderRequest.getCartItemForOrderDTOS();
        Set<OrderLine> orderLines = new LinkedHashSet<>();
        double total = 0.0;
        for (var cartItem : cartItemForOrderDTOS) {
            Product product = productRepository.findByProductCode(cartItem.getProductCode())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            // check quantity
            if (productService.getStockByProductCode(product.getProductCode()) < cartItem.getQuantity()) {
                throw new RuntimeException("Product " + product.getProductCode() + " out of stock");
            }
//         tru so luong san pham
            ProductStock productStock = productStockRepository.findByProduct_ProductCode(product.getProductCode())
                    .orElseThrow(() -> new RuntimeException("Product stock not found"));
            productStock.setQuantity(productStock.getQuantity() - cartItem.getQuantity());
            productStockRepository.save(productStock);
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantity((long) Math.toIntExact(cartItem.getQuantity()));

            // Tạo snapshot cho sản phẩm
            Map<String, Object> productSnapshot = new HashMap<>();
            productSnapshot.put("productId", product.getProductId());
            productSnapshot.put("productCode", product.getProductCode());
            productSnapshot.put("productName", product.getProductName());
            productSnapshot.put("cost", product.getCost());
            productSnapshot.put("description", product.getDescription());
            productSnapshot.put("brand", product.getBrand());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            productSnapshot.put("expirationDate", product.getExpirationDate() != null ? product.getExpirationDate().format(formatter) : null);
            productSnapshot.put("manufactureDate", product.getManufactureDate() != null ? product.getManufactureDate().format(formatter) : null);
            productSnapshot.put("ingredient", product.getIngredient());
            productSnapshot.put("how_to_use", product.getHow_to_use());
            productSnapshot.put("volume", product.getVolume());
            productSnapshot.put("origin", product.getOrigin());
            productSnapshot.put("image", product.getImage());
            orderLine.setProductSnapshot(productSnapshot);
            orderLine.setOrder(order);
            orderLines.add(orderLine);
            // Cộng dồn tổng giá trị
            total += product.getCost() * cartItem.getQuantity();
        }
//        apply discount
        if (createOrderRequest.getVoucherCodes() != null) {
            for (String voucherCode : createOrderRequest.getVoucherCodes()) {
                Voucher voucher = voucherRepository
                        .findFirstByVoucherCodeAndUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode,
                                LocalDateTime.now(), LocalDateTime.now())
                        .orElseThrow(() -> new RuntimeException("Voucher " + voucherCode + " is not available"));
                voucher.setUsed(true);
                voucher.setOrder(order);
                voucherRepository.save(voucher);
                total -= voucher.getVoucherValue();
            }
        }
        // Thiết lập các thuộc tính cho đơn hàng
        order.setOrderLines(orderLines);
        order.setTotal(total < 0 ? 0 : total);
        order.setCustomerId(user.getUserId());
        order.setOrderDate(LocalDateTime.ofInstant(new Date().toInstant(), TimeZone.getDefault().toZoneId()));
        order.setOrderStatus(OrderStatus.PENDING);


        //thêm lịch sử đơn hàng
        List<OrderStatusHistory> orderStatusHistories = new ArrayList<>();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setStatus(OrderStatus.PENDING);
        orderStatusHistory.setDescription("Đơn hàng đã được tạo");
        orderStatusHistories.add(orderStatusHistory);
        order.setOrderStatusHistories(orderStatusHistories);


        // Cập nhật địa chỉ giao hàng
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setOrder(order);
        BeanUtils.copyProperties(createOrderRequest.getAddress(), shippingAddress);
        order.setShippingAddress(shippingAddress);

        // Lưu đơn hàng và tạo phản hồi
        Order savedOrder = orderRepository.save(order);
        OrderResponse createOrderResponse = new OrderResponse();
        BeanUtils.copyProperties(savedOrder, createOrderResponse);


        // Lưu Payment và thiết lập phương thức thanh toán cho phản hồi
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setTotal(total);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod(createOrderRequest.getPaymentMethod());
        paymentRepository.save(payment);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(savedOrder.getOrderId());
        orderResponse.setTotal( savedOrder.getTotal());
        orderResponse.setPaymentMethod(payment.getPaymentMethod());
        return orderResponse;
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found"));
//        if order is not pending, throw exception
        if ((order.getOrderStatus() != OrderStatus.PENDING) && (order.getOrderStatus() != OrderStatus.CONFIRMED))
            throw new CustomException("Order không thể hủy vì đã được xác nhận hoặc đang vận chuyển");
        order.setOrderStatus(OrderStatus.CANCELLED);
//        hoàn lại số lượng sản phẩm
        order.getOrderLines().forEach(x -> {
            ProductStock productStock = productStockRepository.findByProduct_ProductCode(x.getProduct().getProductCode()).orElseThrow(() -> new CustomException("Product stock not found"));
            productStock.setQuantity(productStock.getQuantity() + x.getQuantity());
            productStockRepository.save(productStock);
        });
        orderRepository.save(order);
    }

    @Override
    public OrderResponse updateOrderStatus(@Valid Long orderId, @Valid String status) {
//        TODO: khi thanh toan truoc va khi nhan hang can xu ly khac nhau
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found"));
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        order.setOrderStatus(orderStatus);
        Order savedOrder = orderRepository.save(order);
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(savedOrder, orderResponse);
        //        xử lý OrderLineForOrderDTO cho phan hoi
        Set<OrderLineForOrderDTO> orderLineForOrderDTOS = savedOrder.getOrderLines().stream().map(x -> {
            OrderLineForOrderDTO dto = new OrderLineForOrderDTO();
            dto.setProductCode(x.getProduct().getProductCode());
            dto.setProductSnapshot(x.getProductSnapshot());
            dto.setQuantity(x.getQuantity());
            return dto;
        }).collect(Collectors.toSet());
        orderResponse.setOrderLines(orderLineForOrderDTOS);
//        SET payment
        Payment payment = paymentRepository.findByOrder(order).orElseThrow(() -> new CustomException("Payment not found"));
        orderResponse.setPaymentMethod(payment.getPaymentMethod());
        return orderResponse;
    }


    @Override
    public void updateOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(order.getOrderDate());
        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    @Override
    public Double getOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found"));
        return order.getTotal();
    }

    @Override
    public void updateOrderStatusPaymentTime(Long orderId, String paymentTime) {
        Logger.log("updating order status");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found"));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new CustomException("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.parse(paymentTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        paymentRepository.save(payment);
        orderRepository.save(order);
        Logger.log("updated order status");

    }

    @Override
    public void updateOrderPaymentCOD(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found"));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new CustomException("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(order.getOrderDate());
        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElse(null);
    }

    @Override
    public Set<Order> getAllOrders(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public Page<Order> getAllOrders(Long customerId, Pageable pageable) {

        return orderRepository.findAllPaginated(customerId,pageable);
    }

    @Override
    public Set<Order> getOrdersByOrderStatus(Long customerId, OrderStatus orderStatus) {
        return orderRepository.findAllByCustomerIdAndOrderStatus(customerId, orderStatus);
    }


    @Override
    public List<Order> searchOrders(Long customerId, String keyword, Pageable pageable) {
        Page<Order> page=orderRepository.searchOrdersByCustomerIdAndProductName(customerId, keyword, pageable);
        if (page != null) {
            return page.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    public OrderHistoryDetailDTO getOrderHistoryDetailById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found"));
        OrderHistoryDetailDTO orderHistoryDetailDTO = new OrderHistoryDetailDTO();
        BeanUtils.copyProperties(order, orderHistoryDetailDTO);
        ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO();
        BeanUtils.copyProperties(order.getShippingAddress(), shippingAddressDTO);
        orderHistoryDetailDTO.setShippingAddress(shippingAddressDTO);
        orderHistoryDetailDTO.setOrderLines(order.getOrderLines().stream().map(x -> {
            OrderLineDTO orderLineDTO = new OrderLineDTO();
            ProductSnapshotDTO productSnapshotDTO = new ProductSnapshotDTO();
            Map<String, Object> productSnapshot = x.getProductSnapshot();
            productSnapshotDTO.setProductId(Long.parseLong(productSnapshot.get("productId").toString()));
            productSnapshotDTO.setProductCode((String) productSnapshot.get("productCode"));
            productSnapshotDTO.setProductName((String) productSnapshot.get("productName"));
            productSnapshotDTO.setCost((Double) productSnapshot.get("cost"));
            productSnapshotDTO.setDescription((String) productSnapshot.get("description"));
            productSnapshotDTO.setBrand((String) productSnapshot.get("brand"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            productSnapshotDTO.setExpirationDate(productSnapshot.get("expirationDate") != null ? LocalDate.parse((String) productSnapshot.get("expirationDate"), formatter) : null);
            productSnapshotDTO.setManufactureDate(productSnapshot.get("manufactureDate") != null ? LocalDate.parse((String) productSnapshot.get("manufactureDate"), formatter) : null);
            productSnapshotDTO.setIngredient((String) productSnapshot.get("ingredient"));
            productSnapshotDTO.setHow_to_use((String) productSnapshot.get("how_to_use"));
            productSnapshotDTO.setVolume((String) productSnapshot.get("volume"));
            productSnapshotDTO.setOrigin((String) productSnapshot.get("origin"));
            productSnapshotDTO.setImage((String) productSnapshot.get("image"));
            orderLineDTO.setQuantity(x.getQuantity());
            orderLineDTO.setProductSnapshot(productSnapshotDTO);
            return orderLineDTO;
        }).collect(Collectors.toSet()));
        Payment payment = paymentRepository.findByOrder(order).orElseThrow(() -> new CustomException("Payment not found"));
        PaymentDTO paymentDTO = new PaymentDTO();
        BeanUtils.copyProperties(payment, paymentDTO);
        orderHistoryDetailDTO.setPayment(paymentDTO);
        orderHistoryDetailDTO.setOrderStatusHistories(order.getOrderStatusHistories().stream().map(x -> {
            OrderStatusHistoryDTO orderStatusHistoryDTO = new OrderStatusHistoryDTO();
            BeanUtils.copyProperties(x, orderStatusHistoryDTO);
            return orderStatusHistoryDTO;
        }).collect(Collectors.toList()));
        return orderHistoryDetailDTO;
    }

    @Override
    public ProductSnapshotDTO getProductSnapshot(Long orderId, Long productId) {
        OrderLine orderLine = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found"))
                .getOrderLines().stream().filter(x -> x.getProduct().getProductId().equals(productId)).findFirst()
                .orElseThrow(() -> new CustomException("Product not found"));
        ProductSnapshotDTO productSnapshotDTO = new ProductSnapshotDTO();
        Map<String, Object> productSnapshot = orderLine.getProductSnapshot();
        productSnapshotDTO.setProductId(Long.parseLong(productSnapshot.get("productId").toString()));
        productSnapshotDTO.setProductCode((String) productSnapshot.get("productCode"));
        productSnapshotDTO.setProductName((String) productSnapshot.get("productName"));
        productSnapshotDTO.setCost((Double) productSnapshot.get("cost"));
        productSnapshotDTO.setDescription((String) productSnapshot.get("description"));
        productSnapshotDTO.setBrand((String) productSnapshot.get("brand"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        productSnapshotDTO.setExpirationDate(productSnapshot.get("expirationDate") != null ? LocalDate.parse((String) productSnapshot.get("expirationDate"), formatter) : null);
        productSnapshotDTO.setManufactureDate(productSnapshot.get("manufactureDate") != null ? LocalDate.parse((String) productSnapshot.get("manufactureDate"), formatter) : null);
        productSnapshotDTO.setIngredient((String) productSnapshot.get("ingredient"));
        productSnapshotDTO.setHow_to_use((String) productSnapshot.get("how_to_use"));
        productSnapshotDTO.setVolume((String) productSnapshot.get("volume"));
        productSnapshotDTO.setOrigin((String) productSnapshot.get("origin"));
        productSnapshotDTO.setImage((String) productSnapshot.get("image"));
        return productSnapshotDTO;
    }

    @Override
    public List<Order> getTop5OrdersRecently() {
        return orderRepository.findTop5ByOrderByOrderDateDesc();
    }

    @Override
    public Long countPendingOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.PENDING);
    }
    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        if (status == null) {
            return orderRepository.findAll();
        }
        return orderRepository.findByOrderStatus(status);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> getPaginatedOrders(int page, int size, String searchKeyword, OrderStatus selectedStatus) {
        Pageable pageable = PageRequest.of(page, size);

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            if (selectedStatus != null) {
                return orderRepository.findByOrderIdContainingAndOrderStatus(searchKeyword, selectedStatus, pageable);
            }
            return orderRepository.findByOrderIdContaining(searchKeyword, pageable);
        } else if (selectedStatus != null) {
            return orderRepository.findByOrderStatusOrderByOrderDateDesc(selectedStatus, pageable);
        }
//        return orderRepository.findAll(pageable);
        return orderRepository.findAllDesc(pageable);
    }


    @Override
    public Page<Order> getOrdersByOrderStatus(Long customerId, OrderStatus orderStatus, Pageable pageable) {
        return orderRepository.findAllPaginatedByOrderStatus(customerId, orderStatus, pageable);
    }

    @Override
    public void updateOrderStatusWithContent(Long id, OrderStatus newStatus, String content) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
        order.setOrderStatus(newStatus);
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setStatus(newStatus);
        orderStatusHistory.setDescription(content);
        if (order.getOrderStatusHistories() == null) {
            order.setOrderStatusHistories(new ArrayList<>());
        }
        order.getOrderStatusHistories().add(orderStatusHistory);
        if (newStatus == OrderStatus.COMPLETED) {
            //update ngay hoan thanh
            order.setDeliveryDate(LocalDateTime.now());
            if (order.getPayment().getPaymentMethod() == PaymentMethod.COD)
                order.getPayment().setPaymentStatus(PaymentStatus.PAID);
        }
        orderRepository.save(order);
    }

    public void deleteOrderById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Đơn hàng không tồn tại " + orderId);
        }
        orderRepository.deleteById(orderId);
    }
}
