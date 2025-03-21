package com.cosmeticsellingwebsite.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "voucher")
public class Voucher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucherId")
    private Long voucherId;

    @Column(name = "voucherCode")
    private String voucherCode;

    @Column(name = "voucherValue")
    private Double voucherValue;

    @Column(name = "startDate")
    private LocalDateTime startDate;

    @Column(name = "endDate")
    private LocalDateTime endDate;

    @Column(name = "used")
    private Boolean used = false;

    @OneToOne
    @JoinColumn(name = "orderId", referencedColumnName = "orderId", unique = true)
    private Order order;
}