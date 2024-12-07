package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.dto.mail.MailDTO;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.payload.request.RegisterReq;
import com.cosmeticsellingwebsite.service.captcha.CaptchaService;
import com.cosmeticsellingwebsite.service.google.GoogleService;
import com.cosmeticsellingwebsite.service.impl.UserService;
import com.cosmeticsellingwebsite.service.mail.MailService;
import com.cosmeticsellingwebsite.util.Logger;
import com.cosmeticsellingwebsite.dto.gooogle.GooglePojo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    MailService mailService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping({"/login", "/register"})
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        //get from RedirectAttributes
        if (error != null) {
            model.addAttribute("messageLogin", error);
        }
        return "user/login-register";
    }
    @GetMapping({"/login-failure"})
    public String loginFailureHander(Model model) {
        model.addAttribute("messageLogin", "Sai tên đăng nhập hoặc mật khẩu");
        return "user/login-register";
    }
    @GetMapping("/alogin")
    public String alogin() {
        return "admin/alogin";
    }
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestParam("g-recaptcha-response") String response, @ModelAttribute @Valid RegisterReq registerRequest) {
        captchaService.processResponse(response);
        Logger.log("Register: " + registerRequest);
        userService.registerUser(registerRequest);
        return ResponseEntity.ok("Đăng ký thành công");
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "user/forgot-password";
    }
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("g-recaptcha-response") String response, @RequestParam String email, Model model) {
        captchaService.processResponse(response);
        if (userService.findByEmail(email)==null) {
            model.addAttribute("error", "Không tìm thấy email");
            return "user/forgot-password";
        }
        //khởi tạo token là 1 chuỗi ngẫu nhiên
        String resetPasswordToken = UUID.randomUUID().toString();
        String resetPasswordLink = "http://localhost:8081/auth/reset-password?token=" + resetPasswordToken;

        // lưu tạm token vào redis với key là token, thời gian hết hạn là 30 phút
        redisTemplate.opsForValue().set(resetPasswordToken, email, 30, TimeUnit.MINUTES);
        MailDTO mailDTO = new MailDTO();
        mailDTO.setSubject("Reset Password");
        mailDTO.setTo(email);
        mailDTO.setText("Click vào link sau để reset password: " + resetPasswordLink);
        mailService.sendPasswordResetEmail(mailDTO);
        model.addAttribute("success", "Email đã được gửi, vui lòng kiểm tra email để reset password");
        return "user/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String token, ModelMap model) {
        if (redisTemplate.hasKey(token) && redisTemplate.getExpire(token) > 0) {
            String email = redisTemplate.opsForValue().get(token);
            model.addAttribute("email", email);
        } else {
            model.addAttribute("error", "Token đã hết hạn hoặc không tồn tại");
        }
        model.addAttribute("token", token);
        return "user/resetpassword";
    }
    @PostMapping("/reset-password")
    public String updatePassword(@RequestParam String token, @RequestParam String email, @RequestParam String newPassword) {
        if (!redisTemplate.hasKey(token) || redisTemplate.getExpire(token) <= 0 || !Objects.equals(redisTemplate.opsForValue().get(token), email)) {
            return "err/error";
        }
        userService.resetPassword(email,newPassword);
        //delete token sau khi reset password
        redisTemplate.delete(token);
        return "user/reset-password-success";
    }
}
