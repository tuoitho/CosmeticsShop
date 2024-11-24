package com.cosmeticsellingwebsite.api;

import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.payload.request.LoginRequest;
import com.cosmeticsellingwebsite.payload.request.RegisterRequest;
import com.cosmeticsellingwebsite.payload.request.UserRequest;
import com.cosmeticsellingwebsite.payload.response.ApiResponse;
import com.cosmeticsellingwebsite.service.JwtService;
import com.cosmeticsellingwebsite.service.impl.UserService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRC {
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid  LoginRequest user) {
        try {
            Logger.log("Login request: " + user);
            // Attempt authentication
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            // If authentication is successful, generate and return JWT
            if (authenticate.isAuthenticated()) {
//                String token = jwtService.generateToken(user.getUsername());
                return ResponseEntity.ok(jwtService.generateJwtResponse(user.getUsername()));
            }
        } catch (Exception ex) {
            // Handle invalid login here
//            return ex.getMessage();
            throw new RuntimeException("Invalid login");
        }
        return ResponseEntity.badRequest().body("Invalid login");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest user) {
        userService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse<Void>(true, "User registered successfully", null));
    }
}
