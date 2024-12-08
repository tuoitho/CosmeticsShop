package com.cosmeticsellingwebsite.security;


//import com.cosmeticsellingwebsite.filter.JwtFilter;

import com.cosmeticsellingwebsite.enums.RoleEnum;
import com.cosmeticsellingwebsite.security.oauth.CustomAuthenticationFailureHandler;
import com.cosmeticsellingwebsite.security.oauth.CustomOAuth2UserService;
import com.cosmeticsellingwebsite.security.oauth.OAuth2LoginSuccessHandler;
import com.cosmeticsellingwebsite.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@CrossOrigin
public class SecurityConfig {
    //    @Autowired
//    @Lazy


    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(@Lazy OAuth2LoginSuccessHandler oauth2LoginSuccessHandler) {
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    }

    @Bean
    public OAuth2LoginSuccessHandler oauth2LoginSuccessHandler(PasswordEncoder passwordEncoder) {
        return new OAuth2LoginSuccessHandler(passwordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    // Configures the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(session -> session
                        .maximumSessions(1) // Chỉ cho phép 1 phiên đăng nhập cùng lúc
                        .maxSessionsPreventsLogin(false) // Không cấm đăng nhập nếu đạt giới hạn
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
//                        STATIC_RESOURCES
                        .requestMatchers("/assets/**", "/showMsg.js", "/notification.js", "/error", "/error/**", " /login").permitAll()
//                        PUBLIC
                        .requestMatchers("/api/images/**", "/auth/**", "/oauth2/**", "/user/**", "/browser/**","/about", "/").permitAll()
//                        .requestMatchers("/shipper/**").hasRole("SHIPPER")
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/revenue/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers(
                                "/admin/products/**",
                                "/admin/report/**",
                                "/admin/orders/**",
                                "/admin/revenue/**",
                                "/admin/categories/**",
                                "/admin/vouchers/**",
                                "/admin/stock/**",
                                "/admin/feedbacks/**",
                                "/admin/customers/**"

                        ).hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/user/**").hasRole("ADMIN")
                        .anyRequest().authenticated()) // Require authentication for all other requests
                .formLogin(f -> f.loginPage("/auth/login").permitAll()
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
//                        .failureUrl("/auth/login-failure")
                                .failureHandler(new CustomAuthenticationFailureHandler())
                )
                .rememberMe(remember -> remember
                        .key("yourSecretRememberMeKey") // Replace with a strong, unique key
                        .userDetailsService(userDetailsService()) // Cần thiết để lấy thông tin người dùng
                        .tokenValiditySeconds(7*24*60*60) // 7 days
                        .useSecureCookie(true) // Chỉ gửi cookie qua HTTPS
                         )
                //nhớ tích chọn rememberMe lúc đăng nhập và hãy thử tắt trình duyệt đi rồi mở lại để xem kết quả nhé

                .authenticationProvider(authenticationProvider()) // Register the authentication provider
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login") // Custom login page
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Custom OAuth2 user service
                        )
                        .successHandler(oauth2LoginSuccessHandler) // Handle success
                )

                .build();
    }


    // Creates a DaoAuthenticationProvider to handle user authentication
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    // Defines an AuthenticationManager bean to manage authentication processes
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}