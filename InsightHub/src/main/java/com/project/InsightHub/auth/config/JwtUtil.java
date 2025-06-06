package com.project.InsightHub.auth.config;

import java.util.Date;

import com.project.InsightHub.user.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private final String jwtSecret = "secret";
    private final long jwtExpirationMs = 86400000;

    public String generateToken(User user) {
    return Jwts.builder()
            .subject(user.getEmail())
            .claim("id", user.getId())
            .claim("email", user.getEmail())
            .claim("name", user.getName())
            .claim("googleUser", user.isGoogleUser())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
            .compact();
    }
}
