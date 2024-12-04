package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Voucher;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IVoucherService {
    List<Voucher> getAllVouchers();

    Voucher saveVoucher(Voucher voucher);

    Voucher updateVoucher(Long id, Voucher updatedVoucher);

    void deleteVoucher(Long id);

    Voucher getVoucherById(Long id);

    boolean existsByVoucherCode(String voucherCode);

    Page<Voucher> getVouchersWithPagination(int page, int size);
}