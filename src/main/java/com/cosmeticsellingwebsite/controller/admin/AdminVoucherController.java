package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.Voucher;
import com.cosmeticsellingwebsite.service.impl.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/vouchers")
public class AdminVoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public String listVouchers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<Voucher> voucherPage = voucherService.getVouchersWithPagination(page, size);
        model.addAttribute("voucherPage", voucherPage);
        return "admin/admin-voucher-list"; // View hiển thị danh sách mã giảm giá
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "admin/admin-voucher-add"; // Form tạo mã giảm giá
    }

    @PostMapping("/save")
    public String addVoucher(@ModelAttribute Voucher voucher, BindingResult result, Model model) {

        // Kiểm tra lỗi từ Hibernate Validator
        if (result.hasErrors()) {
            return "admin/admin-voucher-list"; // Trả về form với các lỗi hiển thị
        }

        // Kiểm tra nếu ngày hiệu lực >= ngày hết hạn
        if (voucher.getStartDate().isAfter(voucher.getEndDate()) || voucher.getStartDate().isEqual(voucher.getEndDate())) {
            model.addAttribute("errorMessage", "Ngày hiệu lực phải nhỏ hơn ngày hết hạn!");
            return "admin/admin-voucher-list"; // Quay lại form với thông báo lỗi
        }

        // Kiểm tra mã trùng lặp
        if (voucherService.existsByVoucherCode(voucher.getVoucherCode())) {
            model.addAttribute("errorMessage", "Mã giảm giá đã tồn tại!");
            return "admin/admin-voucher-list";
        }

        // Kiểm tra ngày hiệu lực và ngày hết hạn không bỏ trống
        if (voucher.getStartDate() == null || voucher.getEndDate() == null) {
            model.addAttribute("errorMessage", "Ngày hiệu lực và ngày hết hạn không được bỏ trống!");
            return "admin/admin-voucher-list";
        }

        voucher.setOrder(null);
        voucherService.saveVoucher(voucher);
        return "redirect:/admin/vouchers";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "admin/admin-voucher-edit"; // Form chỉnh sửa mã giảm giá
    }

    @PostMapping("/update/{id}")
    public String updateVoucher(@PathVariable("id") Long id, @ModelAttribute("voucher") @Valid Voucher voucher, BindingResult result) {
        if (result.hasErrors()) {
            return "voucher/edit";
        }
        voucherService.updateVoucher(id, voucher);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable("id") Long id) {
        voucherService.deleteVoucher(id);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestParam String code) {
        boolean exists = voucherService.existsByVoucherCode(code);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }


}
