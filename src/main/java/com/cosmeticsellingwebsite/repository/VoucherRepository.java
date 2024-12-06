package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
            SELECT v FROM Voucher v WHERE v.voucherId IN (
                SELECT MIN(v1.voucherId) FROM Voucher v1 GROUP BY v1.voucherCode
            )
            """
    )
    Page<Voucher> findDistinctVouchers(Pageable pageable);

    Long countByUsedFalseAndVoucherCode(String voucherCode);

    Long countByUsedTrueAndVoucherCode(String voucherCode);

    Long countByVoucherCode(String voucherCode);

    Optional<Voucher> findFirstByVoucherCodeAndUsedFalse(String voucherCode);

}
