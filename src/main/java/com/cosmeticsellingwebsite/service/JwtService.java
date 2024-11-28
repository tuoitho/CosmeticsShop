package com.cosmeticsellingwebsite.service;

import com.cosmeticsellingwebsite.util.Logger;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import com.cosmeticsellingwebsite.payload.response.*;

//JwtService is responsible for handling JWT (JSON Web Token) operations
// such as token generation, extraction of claims, and token validation.

@Component
public class JwtService {
    // Secret Key for signing the JWT. It should be kept private.
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration}")
    private long EXPIRATION;
    // Generates a JWT token for the given userName.
    public String generateToken(String userName) {
        // Prepare claims for the token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "fsffsf"); // Thêm vai trò vào claims
        // Build JWT token with claims, subject, issued time, expiration time, and signing algorithm
          // Token valid for 3 minutes
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    // Creates a signing key from the base64 encoded secret.
    //returns a Key object for signing the JWT.
    private Key getSignKey() {
        // Decode the base64 encoded secret key and return a Key object
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class); // Lấy role từ claims
    }

    // Extracts the userName from the JWT token.
    //return -> The userName contained in the token.
    public String extractUserName(String token) {
        // Extract and return the subject claim from the token
        return extractClaim(token, Claims::getSubject);
    }


    // Extracts the expiration date from the JWT token.
    //@return The expiration date of the token.
    public Date extractExpiration(String token) {
        // Extract and return the expiration claim from the token
        return extractClaim(token, Claims::getExpiration);
    }


    // Extracts a specific claim from the JWT token.
    // claimResolver A function to extract the claim.
    // return-> The value of the specified claim.
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        // Extract the specified claim using the provided function
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    //Extracts all claims from the JWT token.
    //return-> Claims object containing all claims.
    private Claims extractAllClaims(String token) {
        // Parse and return all claims from the token
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build().parseClaimsJws(token).getBody();
    }


    //Checks if the JWT token is expired.
    //return-> True if the token is expired, false otherwise.
    public Boolean isTokenExpired(String token) {
        // Check if the token's expiration time is before the current time
        return extractExpiration(token).before(new Date());
    }

    //Validates the JWT token against the UserDetails.
    //return-> True if the token is valid, false otherwise.

    public Boolean validateToken(String token, UserDetails userDetails) {
        // Extract username from token and check if it matches UserDetails' username
        final String userName = extractUserName(token);
        // Also check if the token is expired
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public JwtResponse generateJwtResponse(String userName) {
        String token = generateToken(userName);
        Date expiration = extractExpiration(token);
        boolean isActive = true; // hoặc sử dụng một thuộc tính khác để kiểm tra trạng thái hoạt động
        String username = extractUserName(token);
        return new JwtResponse(username,token, expiration, isActive);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            Logger.log("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            Logger.log("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            Logger.log("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            Logger.log("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            Logger.log("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // Kiểm tra nếu cookies không phải null
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Kiểm tra tên cookie có phải là "JWT" hay không (hoặc tên bạn đặt cho cookie)
                if ("Authorization".equals(cookie.getName())) {
                    // Trả về giá trị của cookie (token JWT)
                    return cookie.getValue();
                }
            }
        }
        // Nếu không tìm thấy cookie chứa token, trả về null
        return null;
    }
}
