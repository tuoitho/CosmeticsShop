package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.payload.request.RegisterReq;
import com.cosmeticsellingwebsite.service.impl.UserService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
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
    public ResponseEntity<?> register(@RequestBody @Valid RegisterReq registerRequest) {
        Logger.log("Register: " + username + " - " + password);
        return ResponseEntity.ok("Đăng ký thành công");
    }
}
