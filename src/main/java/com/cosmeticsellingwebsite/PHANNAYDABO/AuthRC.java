//package com.cosmeticsellingwebsite.apidabo;
//
//import com.cosmeticsellingwebsite.payload.request.LoginRequest;
//import com.cosmeticsellingwebsite.payload.request.RegisterRequest;
//import com.cosmeticsellingwebsite.payload.response.ApiResponse;
//import com.cosmeticsellingwebsite.service.JwtService;
//import com.cosmeticsellingwebsite.service.impl.UserService;
//import com.cosmeticsellingwebsite.util.Logger;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//
//public class AuthRC {
//    @Autowired
//    UserService userService;
//    @Autowired
//    JwtService jwtService;
//    @Autowired
//    AuthenticationManager authenticationManager;
//    @Autowired
//    private ApplicationContext applicationContext;
//    private UserService getUserService() {
//        return applicationContext.getBean(UserService.class);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest user,
//                                   HttpServletRequest request,
//                                   HttpServletResponse response) {
//        try {
//            // 1. Xác thực người dùng
//            Authentication authenticate = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//            );
//
//            if (authenticate.isAuthenticated()) {
//                // 2. Lấy UserDetails từ Authentication
//                UserDetails userDetails = getUserService().loadUserByUsername(user.getUsername());
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                // Setting authentication details
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                // Setting the authentication token in the SecurityContext
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//                // 3. Tạo JWT token
//                String token = jwtService.generateToken(userDetails.getUsername());
//
//                // 4. Set Authentication vào SecurityContext - Giống như trong filter
//
//                // 5. Trả về response với token
//                Map<String, Object> responseMap = new HashMap<>();
//                responseMap.put("token", token);
//                responseMap.put("username", userDetails.getUsername());
//                responseMap.put("roles", userDetails.getAuthorities());
//
//                return ResponseEntity.ok(responseMap);
//            }
//        } catch (Exception ex) {
//            Logger.log("Login error: " + ex.getMessage());
//            throw new RuntimeException("Invalid login credentials");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
//    }
////    @PostMapping("/login")
////    public ResponseEntity<?> login(@RequestBody @Valid  LoginRequest user, HttpServletRequest request, HttpServletResponse response) {
////        try {
////            Logger.log("Login request: " + user);
////            // Attempt authentication
////            Authentication authenticate = authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
////            );
////            // If authentication is successful, generate and return JWT
////            if (authenticate.isAuthenticated()) {
////                // 2. Set SecurityContext
////                Logger.log("Login success: " + authenticate.getPrincipal());
////                UserDetails userDetails = (UserDetails) authenticate.getPrincipal();  // Đảm bảo bạn lấy userDetails đúng cách
////
////                // 2. Set Authentication vào SecurityContext
////                UsernamePasswordAuthenticationToken authenticationToken =
////                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
////                Logger.log("Login success: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
////
////                return ResponseEntity.ok(jwtService.generateJwtResponse(user.getUsername()));
////            }
////        } catch (Exception ex) {
////            // Handle invalid login here
//////            return ex.getMessage();
////            throw new RuntimeException("Invalid login");
////        }
////        return ResponseEntity.badRequest().body("Invalid login");
////    }
//    @GetMapping("/random")
//    public String randomStuff(){
//        return ("JWT Hợp lệ mới có thể thấy được message này");
//    }
//    @GetMapping("/info")
////    @ResponseBody
//    public String info(Principal principal){
//        Logger.log(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//        Logger.log("Principal: " +principal.getName());
//
//        return ("Xem thông tin user");
//    }
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest user) {
//        userService.registerUser(user);
//        return ResponseEntity.ok(new ApiResponse<Void>(true, "User registered successfully", null));
//    }
//
//    @GetMapping("logout")
//    public ResponseEntity<?> logout(){
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok(new ApiResponse<Void>(true, "Logout successfully", null));
//    }
//
//}
