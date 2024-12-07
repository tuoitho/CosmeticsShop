package com.cosmeticsellingwebsite.controller.customer;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.dto.UserDTO;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.service.image.ImageService;
import com.cosmeticsellingwebsite.service.impl.AddressService;
import com.cosmeticsellingwebsite.service.impl.PersonalInformationService;
import com.cosmeticsellingwebsite.service.interfaces.IAddressService;
import com.cosmeticsellingwebsite.service.interfaces.IPersonalInformationService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

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
    @Autowired
    ImageService imageService;

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

    @PostMapping("/profile")
    public String updatePersonalInfo(@RequestParam("imagePath") MultipartFile imageFile,
                                     @RequestParam("birthDate") String birthDateStr,
                                     UserDTO userModel, RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
//                String imagePath = saveImage(imageFile);
                String img=imageService.saveImage(imageFile);

                userModel.setImage(img);
            }
            if (birthDateStr != null && !birthDateStr.isEmpty()) {
                LocalDate birthDate = LocalDate.parse(birthDateStr);
                userModel.setBirthDate(birthDate);
            }

            Long userID = authenticationHelper.getUserId();
            userModel.setUserId(userID);

            if (service.savePersonalInfo(userModel, userID)) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Dữ liệu không hợp lệ!");
        }
        return "redirect:/customer/personal-info";
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @PostMapping("/password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin người dùng hiện tại
            Long userId = authenticationHelper.getUserId();
            UserDTO user = service.fetchPersonalInfo(userId); // Fetch user info from DB

            // Kiểm tra mật khẩu hiện tại có hợp lệ không
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không chính xác!");
                return "redirect:/customer/personal-info";
            }

            // Kiểm tra khớp giữa mật khẩu mới và xác nhận mật khẩu
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
                return "redirect:/customer/personal-info";
            }

            // Mã hóa mật khẩu mới
            String encodedNewPassword = passwordEncoder.encode(newPassword);

            // Lưu mật khẩu mới
            if (service.updatePassword(userId, encodedNewPassword)) {
                redirectAttributes.addFlashAttribute("message", "Thay đổi mật khẩu thành công!");
                return "redirect:/customer/personal-info";
            } else {
                redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thay đổi mật khẩu!");
                return "redirect:/customer/personal-info";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Dữ liệu không hợp lệ!");
            return "redirect:/customer/personal-info";
        }
    }

    private String saveImage(MultipartFile imageFile) {
        String folderPath = "src/main/resources/static/assets/img/customer/";
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(folderPath + fileName);

        try {
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(filePath.getParent());
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/assets/img/customer/" + fileName + "?v=" + System.currentTimeMillis();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}