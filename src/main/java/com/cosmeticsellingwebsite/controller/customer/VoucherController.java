package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.entity.Voucher;
import com.cosmeticsellingwebsite.service.impl.VoucherService;
import com.cosmeticsellingwebsite.service.interfaces.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/customer/voucher")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<?> getVoucher(@RequestParam("voucherCode") String voucherCode) {
        Voucher voucher = voucherService.getVoucherByVoucherCode(voucherCode);
        return new ResponseEntity<>(voucher, HttpStatus.OK);
    }
}
