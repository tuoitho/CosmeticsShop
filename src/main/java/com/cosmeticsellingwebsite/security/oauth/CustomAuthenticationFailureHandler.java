package com.cosmeticsellingwebsite.security.oauth;

import com.cosmeticsellingwebsite.security.UserPrincipal;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        String errorMessage;

        if (exception instanceof LockedException) {
            errorMessage = "Tài khoản của bạn đã bị khóa.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Sai tên đăng nhập hoặc mật khẩu.";
        } else {
            errorMessage = "Đã xảy ra lỗi không xác định.";
        }
        Logger.log("Login failed for username: " + username + ". Reason: " + errorMessage);
        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        // Redirect to the login page with the error message as a query parameter
        response.sendRedirect("/auth/login?error=" + errorMessage);
    }
}