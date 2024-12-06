package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.security.UserPrincipal;
import com.cosmeticsellingwebsite.security.oauth.CustomOAuth2User;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private AuthenticationHelper authenticationHelper;
    @Autowired
    private ProductService productService;
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("top20BestSellingProducts", productService.getTop20BestSellingProducts());
        model.addAttribute("top20NewestProducts", productService.getTop20NewestProducts());
        model.addAttribute("top20HighestRatingProducts", productService.getTop20HighestRatedProducts());
        return "user/home";
    }
}
