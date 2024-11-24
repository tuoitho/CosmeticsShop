package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    public String login() {
        return "auth/login";
    }
    public String register() {
        return "auth/register";
    }
}
