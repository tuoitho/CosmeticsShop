package com.cosmeticsellingwebsite.security;

import com.cosmeticsellingwebsite.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Lấy thông tin người dùng từ Authentication object
        //lay role
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        Logger.log(authorities);
        switch (authorities) {
            case "ROLE_ADMIN", "ROLE_MANAGER" -> response.sendRedirect("/admin/report");
            case "ROLE_CUSTOMER" -> response.sendRedirect("/");
            default -> response.sendRedirect("/login");
        }

    }
}