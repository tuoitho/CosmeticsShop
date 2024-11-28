package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.entity.Customer;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.payload.request.RegisterReq;
import com.cosmeticsellingwebsite.service.capcha.CaptchaService;
import com.cosmeticsellingwebsite.service.google.GoogleService;
import com.cosmeticsellingwebsite.service.impl.UserService;
import com.cosmeticsellingwebsite.util.Logger;
import com.cosmeticsellingwebsite.dto.gooogle.GooglePojo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

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

    @GetMapping({"/login", "/register"})
    public String login() {
        return "user/login-register";
    }
    //lấy thông tin user đã đăng nhập
    @GetMapping("/info")
    @ResponseBody
    public String info(Principal principal){
        Logger.log("Principal: " +principal.getName());
        return ("Xem thông tin user thành công: "+principal.getName());
    }
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestParam("g-recaptcha-response") String response, @RequestBody @Valid RegisterReq registerRequest) {
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
}
