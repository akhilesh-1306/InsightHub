package com.project.InsightHub.auth.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.project.InsightHub.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String jwtSecret = "MySecretKeyForJWTTokenGenerationThatIsLongEnoughForHS256Algorithm";
    private final long jwtExpirationMs = 86400000;
    private final SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    // Generate JWT token
    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getEmail())
            .claim("id", user.getId())
            .claim("email", user.getEmail())
            .claim("name", user.getName())
            .claim("googleUser", user.isGoogleUser())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(secretKey)
            .compact();
    }

    // Extract email from JWT token
    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null; 
        }
    }

    // Validate JWT token
    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
        * This will be used if i send the entire User object in the request
    */

    // public User extractUserFromToken(String token) {
    //     try {
    //         Claims claims = Jwts.parser()
    //                 .verifyWith(secretKey)
    //                 .build()
    //                 .parseSignedClaims(token)
    //                 .getPayload();
            
    //         User user = new User();
    //         user.setId(claims.get("id", Long.class));
    //         user.setEmail(claims.get("email", String.class));
    //         user.setName(claims.get("name", String.class));
    //         user.setGoogleUser(claims.get("googleUser", Boolean.class));
            
    //         return user;
    //     } catch (JwtException | IllegalArgumentException e) {
    //         return null;
    //     }
    // }
}
