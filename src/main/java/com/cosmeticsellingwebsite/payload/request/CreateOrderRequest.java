package com.cosmeticsellingwebsite.payload.request;

import com.cosmeticsellingwebsite.dto.CartItemForOrderDTO;
import com.cosmeticsellingwebsite.dto.ShippingAddressDTO;
import com.cosmeticsellingwebsite.entity.ShippingAddress;
import com.cosmeticsellingwebsite.enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequest {

    @NotNull(message = "Address is required")
    private ShippingAddressDTO address; // Địa chỉ giao hàng

    @NotNull(message = "Payment method is required")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // Phương thức thanh toán (có thể là enum hoặc một đối tượng khác)

    @NotNull(message = "Cart item list is required")
    private Set<CartItemForOrderDTO> cartItemForOrderDTOS; // Danh sách sản phẩm và số lượng sản phẩm mà khách hàng muốn mua

    private Set<String> voucherCodes; // Danh sách mã voucher mà khách hàng muốn sử dụng
}