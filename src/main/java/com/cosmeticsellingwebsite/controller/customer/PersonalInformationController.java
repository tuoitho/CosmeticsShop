package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.dto.UserDTO;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.service.impl.AddressService;
import com.cosmeticsellingwebsite.service.impl.PersonalInformationService;
import com.cosmeticsellingwebsite.service.interfaces.IAddressService;
import com.cosmeticsellingwebsite.service.interfaces.IPersonalInformationService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer/personal-info")
public class PersonalInformationController {

    @Autowired
    private IPersonalInformationService service;
    @Autowired
    private AddressService addressServiceImpl ;
    @Autowired
    private PersonalInformationService personalInfoServiceImpl;
    @Autowired
    private AuthenticationHelper authenticationHelper;

    // Lấy thông tin cá nhân và địa chỉ
    @GetMapping
    public String getPersonalInfo(Model model) {
        //lấy thông tin ng dùng đã đăng nhập
        Long customerID = authenticationHelper.getUserId();
        UserDTO user = service.fetchPersonalInfo(customerID); // Gọi Service để lấy thông tin người dùng
        Logger.log("User: " + user);
        if (user != null) {
            model.addAttribute("user", user); // Thêm thông tin người dùng vào model
        }
        return "customer/personal-info"; // Hiển thị trang personal-info
    }

    // Cập nhật thông tin cá nhân
    @PostMapping("/profile")
    public String updatePersonalInfo(UserDTO userModel, Model model) {
        Long userID = authenticationHelper.getUserId();
        userModel.setUserId(userID);
        // Lưu thông tin mới
        if (service.savePersonalInfo(userModel, userModel.getUserId())) {
            model.addAttribute("message", "Cập nhật thông tin cá nhân thành công!");
            return "redirect:/customer/personal-info?success"; // Chuyển hướng với trạng thái thành công
        } else {
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin cá nhân!");
            return "redirect:/customer/personal-info?error"; // Chuyển hướng với trạng thái thất bại
        }
    }


    @PostMapping("/address")
    public String updateAddress(HttpSession session, @ModelAttribute AddressForOrderDTO addressModel, Model model) {
        Long userID = authenticationHelper.getUserId();
        UserDTO user = service.fetchPersonalInfo(userID);

        AddressForOrderDTO existingAddress = user.getAddress();
        if (existingAddress != null) {
            addressModel.setAddressId(existingAddress.getAddressId());
        }

        boolean status = addressServiceImpl.updateAddressForUser(addressModel, addressModel.getAddressId());
        if (status) {
            model.addAttribute("message", "Cập nhật địa chỉ thành công!");
            return "redirect:/customer/personal-info?address-success";
        } else {
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật địa chỉ!");
            return "redirect:/customer/personal-info?address-error";
        }
    }


    @PostMapping("/password")
    public String changePassword(HttpSession session, String currentPassword, String newPassword, String confirmNewPassword, Model model) {
        Long userID = authenticationHelper.getUserId();
        // Lấy thông tin người dùng từ database
        UserDTO user = service.fetchPersonalInfo(userID);

        // Kiểm tra nếu không tìm thấy người dùng
        if (user == null) {
            model.addAttribute("error", "Người dùng không tồn tại!");
            return "redirect:/customer/personal-info";
        }

        // Kiểm tra mật khẩu hiện tại
        if (!currentPassword.equals(user.getPassword())) {
            model.addAttribute("error", "Mật khẩu hiện tại không đúng!");
            return "redirect:/customer/personal-info";
        }

        // Kiểm tra mật khẩu mới có khớp với xác nhận mật khẩu không
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
            return "redirect:/customer/personal-info";
        }

        // Cập nhật mật khẩu mới
        user.setPassword(newPassword);
        boolean status = service.savePersonalInfo(user, user.getUserId());

        if (status) {
            model.addAttribute("message", "Thay đổi mật khẩu thành công!");
            return "redirect:/customer/personal-info?success";
        } else {
            model.addAttribute("error", "Có lỗi xảy ra khi thay đổi mật khẩu!");
            return "customer/personal-info";
        }
    }
}