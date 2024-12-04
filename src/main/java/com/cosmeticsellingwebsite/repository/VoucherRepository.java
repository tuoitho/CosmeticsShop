package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findFirstByVoucherCodeAndUsedFalseAndStartDateBeforeAndEndDateAfter(String voucherCode, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Voucher> findByVoucherCode(String voucherCode);
    boolean existsByVoucherCode(String voucherCode);

}
