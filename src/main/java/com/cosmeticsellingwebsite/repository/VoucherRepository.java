package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findFirstByVoucherCodeAndUsedFalseAndStartDateBeforeAndEndDateAfter(String voucherCode, LocalDateTime startDate, LocalDateTime endDate);
}
