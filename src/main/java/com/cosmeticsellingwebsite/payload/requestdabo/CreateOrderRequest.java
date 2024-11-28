//package com.cosmeticsellingwebsite.payload.requestdabo;
//
//import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
//import com.cosmeticsellingwebsite.dto.CartItemForOrderDTO;
//import com.cosmeticsellingwebsite.enums.PaymentMethod;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Set;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//public class CreateOrderRequest {
//    @NotNull(message = "User ID is required")
//    private Long userId; // ID khách hàng (có thể sử dụng trong việc lấy thông tin khách hàng từ DB)
//    @NotNull(message = "Address ID is required")
//    private AddressForOrderDTO addressForOrderDTO; // Địa chỉ giao hàng
//    @NotNull(message = "Payment method is required")
//    @Enumerated(EnumType.STRING)
//    private PaymentMethod paymentMethod; // Phương thức thanh toán (có thể là enum hoặc một đối tượng khác)
//    @NotNull(message = "Cart item list is required")
//    private Set<CartItemForOrderDTO> cartItemForOrderDTOS; // Danh sách sản phẩm và số lượng sản phẩm mà khách hàng muốn mua
//}
