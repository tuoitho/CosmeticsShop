package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.security.UserPrincipal;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "user/home";
    }
}
