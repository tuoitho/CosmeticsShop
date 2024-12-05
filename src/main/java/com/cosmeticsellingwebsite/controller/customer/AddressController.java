package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.service.impl.AddressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer/personal-info/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Hiển thị danh sách địa chỉ
    @GetMapping
    public String listAddresses(HttpSession session, Model model) {
        Long userID = 7L; // Lấy userID từ session, tạm thời cố định là 5L
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
    public String saveAddress(@ModelAttribute AddressForOrderDTO addressDTO, HttpSession session, Model model) {
        Long userID = 7L; // Lấy userID từ session
        boolean success = addressService.saveAddressForUser(addressDTO, userID);
        if (success) {
            return "redirect:/customer/personal-info";
        } else {
            model.addAttribute("error", "Có lỗi xảy ra khi lưu địa chỉ!");
            return "customer/edit-address";
        }
    }
}
