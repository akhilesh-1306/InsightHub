package com.project.InsightHub.auth.config;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validate(token)) {
                String email = jwtUtil.extractEmail(token);
                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        /**
         * This will be used if i send the entire User object in the request
         */
        
        // String authHeader = request.getHeader("Authorization");
        // String token = null;
        // String email = null;
        
        // // Extract token from Authorization header
        // if (authHeader != null && authHeader.startsWith("Bearer ")) {
        //     token = authHeader.substring(7);
        //     email = jwtUtil.extractEmail(token); // Use your existing method
        // }
        
        // // If token is valid and no authentication is set
        // if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        //     if (jwtUtil.validate(token)) { // Use your existing validate method
        //         // Get user details from token or database
        //         User user = jwtUtil.extractUserFromToken(token); // You'll need to implement this
        //         // Or alternatively: User user = userService.findByEmail(email);
                
        //         // Create authentication token with User object
        //         UsernamePasswordAuthenticationToken authToken = 
        //             new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        //         authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //         SecurityContextHolder.getContext().setAuthentication(authToken);
        //     }
        // }

        filterChain.doFilter(request, response);
    }
}
