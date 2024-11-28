//package com.cosmeticsellingwebsite.filter;
//
//
//import com.cosmeticsellingwebsite.service.JwtService;
//import com.cosmeticsellingwebsite.service.impl.UserService;
//import com.cosmeticsellingwebsite.util.Logger;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.java.Log;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
////OncePerRequestFilter : Có request yêu cầu chứng thực thì chạy vào filter
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//    //    Bước 1 : Lấy token
////    Bước 2 : Giải mã token
////    Bước 3 : Token hợp lệ tạo chứng thực cho Security
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    // Method to lazily fetch the UserService bean from the ApplicationContext
//    // This is done to avoid Circular Dependency issues
//    private UserService getUserService() {
//        return applicationContext.getBean(UserService.class);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // Extracting token from the request header
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String userName = null;
//
////        String requestURI = request.getRequestURI();
////        Logger.log("Request URI: " + requestURI, request.getRequestURL().toString());
////        if (requestURI.equals("/auth/login") || requestURI.equals("/api/v1/auth/login")) {
////            // Nếu là trang login, bỏ qua việc xử lý token và tiếp tục với filter chain
////            Logger.log("Bỏ qua login");
////            filterChain.doFilter(request, response);
////            return;
////        }
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            // Extracting the token from the Authorization header
//            try {
//                // Extracting username from the token
//                userName = jwtService.extractUserName(token);
//            } catch (Exception e) {
//                // Handle exception while extracting username from the token
//                Logger.log("Lỗi jwt nè b âfbajnn faksnfàakfanfhkafafaf" + e.getMessage());
//                throw new RuntimeException("JWT token is invalid");
//            }
//        }
//        // Nếu token không có trong header, thử lấy từ cookie (nếu bạn lưu token trong cookie)
//
//        // If username is extracted and there is no authentication in the current SecurityContext
//        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Loading UserDetails by username extracted from the token
//            UserDetails userDetails = getUserService().loadUserByUsername(userName);
//            // Validating the token with loaded UserDetails
//            if (jwtService.validateToken(token, userDetails)) {
//                // Creating an authentication token using UserDetails
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                // Setting authentication details
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                // Setting the authentication token in the SecurityContext
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//                Logger.log("Da set context: " + userName);
//            } else {
//                // Token is invalid
//                throw new RuntimeException("JWT token is invalid");
//            }
//        }
//
//
//        // Proceeding with the filter chain
//        filterChain.doFilter(request, response);
//    }
//
//}
//
