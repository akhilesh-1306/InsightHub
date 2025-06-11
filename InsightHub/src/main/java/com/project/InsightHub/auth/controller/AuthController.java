package com.project.InsightHub.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.InsightHub.auth.config.JwtUtil;
import com.project.InsightHub.auth.dto.AuthResponse;
import com.project.InsightHub.auth.dto.LoginRequest;
import com.project.InsightHub.auth.dto.RegisterRequest;
import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.user.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
     @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User();
        user.setEmail(req.email());
        user.setName(req.name());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setGoogleUser(false);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getName(), user.getEmail(), false));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getName(), user.getEmail(), user.isGoogleUser()));
    }

    //@PostMapping("/google")
    //     public ResponseEntity<AuthResponse> googleLogin(@RequestBody Map<String, String> request) {
    //     String idTokenString = request.get("token");

    //     GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
    //             .setAudience(Collections.singletonList("YOUR_GOOGLE_CLIENT_ID"))
    //             .build();

    //     GoogleIdToken idToken = verifier.verify(idTokenString);
    //     if (idToken == null) {
    //         throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google token");
    //     }

    //     GoogleIdToken.Payload payload = idToken.getPayload();
    //     String email = payload.getEmail();
    //     String name = (String) payload.get("name");
    //     String picture = (String) payload.get("picture");

    //     User user = userRepository.findByEmail(email).orElseGet(() -> {
    //         User newUser = new User();
    //         newUser.setEmail(email);
    //         newUser.setName(name);
    //         newUser.setGoogleUser(true);
    //         newUser.setProfileImage(picture);
    //         return userRepository.save(newUser);
    //     });

    //     String token = jwtUtil.generateToken(user);
    //     return ResponseEntity.ok(new AuthResponse(token, user.getName(), user.getEmail(), true));
    // }
}
