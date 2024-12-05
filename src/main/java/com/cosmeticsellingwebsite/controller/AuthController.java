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
    private GoogleService googleUtils;
    @Autowired
    private UserService userservice;
    @Autowired
    MailService mailService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping({"/login", "/register"})
    public String login() {
        return "user/login-register";
    }
    //lấy thông tin user đã đăng nhập
    @GetMapping("/info")
    @ResponseBody
    public String info(Principal principal){
        Logger.log("Principal: " +principal.getName());
        //gget role
        Logger.log("Authorities: "+principal);
        return ("Xem thông tin user thành công: "+principal.getName());
    }
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestParam("g-recaptcha-response") String response, @ModelAttribute @Valid RegisterReq registerRequest) {
        captchaService.processResponse(response);
        Logger.log("Register: " + registerRequest);
        userService.registerUser(registerRequest);
        return ResponseEntity.ok("Đăng ký thành công");
    }



    @RequestMapping("/loginGG")
    public String LoginWithGoogle(@RequestParam String code) {
        try {
            String accessToken = googleUtils.getToken(code);
            GooglePojo googleUser = googleUtils.getUserInfo(accessToken);
            Logger.log("Google User: " + googleUser);
            User user = userservice.findByEmail(googleUser.getEmail());
            if (user == null) {
                RegisterReq customer = new RegisterReq();
                customer.setEmail(googleUser.getEmail());
                customer.setFullname(googleUser.getName());
                customer.setPassword("");
                userservice.registerUser(customer);
            }
            // Create authorities
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Add any roles as needed

            // Create Authentication object
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
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
