package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/products")
    public String index(Principal principal) {
        Logger.log("Admin page");
        Logger.log("Admin page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Logger.log("Principal: " + principal.getName());
        Logger.log("Authorities: " + authentication.getAuthorities());
        return "admin/products";
    }
    @ResponseBody
    @GetMapping("")
    public String admin() {
        return "Admin page";
    }

}
