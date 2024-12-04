package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Voucher;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.repository.VoucherRepository;
import com.cosmeticsellingwebsite.service.interfaces.IVoucherService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoucherService implements IVoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    public Voucher getVoucherByVoucherCode(String voucherCode) {
        return voucherRepository.findFirstByVoucherCodeAndUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new CustomException("Voucher " + voucherCode + " is not available"));
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher updateVoucher(Long id, Voucher updatedVoucher) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy voucher"));

        if (voucher.getUsed()) {
            throw new IllegalStateException("Không thể chỉnh sửa voucher do mã đang được sử dụng cho đơn hàng hệ thống.");
        }

        voucher.setVoucherCode(updatedVoucher.getVoucherCode());
        voucher.setVoucherValue(updatedVoucher.getVoucherValue());
        voucher.setStartDate(updatedVoucher.getStartDate());
        voucher.setEndDate(updatedVoucher.getEndDate());
        return voucherRepository.save(voucher);
    }

    @Override
    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy voucher"));

        if (voucher.getUsed()) {
            throw new IllegalStateException("Không thể xoá voucher do đã được áp dụng cho đơn hàng hệ thống.");
        }

        voucherRepository.delete(voucher);
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voucher ID " + id + " không tồn tại"));
    }

    @Override
    public boolean existsByVoucherCode(String voucherCode) {
        return voucherRepository.existsByVoucherCode(voucherCode);
    }

    @Override
    public Page<Voucher> getVouchersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
        return voucherRepository.findAll(pageable);
    }

}
