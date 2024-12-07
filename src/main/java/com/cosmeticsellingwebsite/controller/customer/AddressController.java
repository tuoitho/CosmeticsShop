package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.service.impl.AddressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customer/personal-info/address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthenticationHelper authenticationHelper;

    // Hiển thị danh sách địa chỉ
    @GetMapping
    public String listAddresses(HttpSession session, Model model) {
        Long userID = authenticationHelper.getUserId();
        List<AddressForOrderDTO> addresses = addressService.getAddressesForUser(userID);
        model.addAttribute("addresses", addresses);
        return "customer/personal-info"; // Trả về giao diện danh sách
    }

    // Trang chỉnh sửa địa chỉ
    @GetMapping("/edit/{addressId}")
    public String editAddress(@PathVariable Long addressId, Model model) {
        AddressForOrderDTO address = addressService.getAddressById(addressId);
        if (address == null) {
            return "redirect:/customer/personal-info/address?error=not-found";
        }
        model.addAttribute("address", address);
        return "customer/edit-address"; // Giao diện sửa địa chỉ
    }

    // Trang thêm địa chỉ mới
    @GetMapping("/new")
    public String newAddress(Model model) {
        model.addAttribute("address", new AddressForOrderDTO());
        return "customer/edit-address"; // Sử dụng chung giao diện với chỉnh sửa
    }

    // Lưu địa chỉ (thêm hoặc cập nhật)
    @PostMapping("/save")
    public String saveAddress(@ModelAttribute AddressForOrderDTO addressDTO, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long userID = authenticationHelper.getUserId();
        boolean success = addressService.saveAddressForUser(addressDTO, userID);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Lưu địa chỉ thành công!");
            return "redirect:/customer/personal-info";
        } else {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lưu địa chỉ!");
            return "customer/edit-address";
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            // Gọi service để xóa địa chỉ
            addressService.deleteAddressById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể xóa địa chỉ.");
        }
    }

}
