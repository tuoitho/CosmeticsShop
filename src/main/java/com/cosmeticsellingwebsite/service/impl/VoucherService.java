package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Voucher;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    public Voucher getVoucherByVoucherCode(String voucherCode) {
        return voucherRepository.findFirstByVoucherCodeAndUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new CustomException("Voucher " + voucherCode + " is not available"));
    }
}
