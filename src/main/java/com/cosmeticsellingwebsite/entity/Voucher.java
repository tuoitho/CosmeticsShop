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
    private Long voucherId;

    private String voucherCode;

    private Double voucherValue;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean used = false;

    @OneToOne
    @JoinColumn(unique = true, name = "orderId")
    @ToString.Exclude     @EqualsAndHashCode.Exclude

    private Order order;
}