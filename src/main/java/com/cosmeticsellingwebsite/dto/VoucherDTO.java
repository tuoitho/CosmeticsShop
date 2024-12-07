package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.Order;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
public class VoucherDTO {

    private Long voucherId;
    private String voucherCode;
    private Double voucherValue;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime endDate;

    private Long quantity;
    private Long quantityUsed;
    private Long quantityAvailable;

}
