package com.project.InsightHub.auth.config;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
   @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, java.io.IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        
        // Extract token from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtUtil.extractEmail(token); // Use your existing method
        }
        
        // If token is valid and no authentication is set
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validate(token)) { // Use your existing validate method
                // Create authentication token
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
