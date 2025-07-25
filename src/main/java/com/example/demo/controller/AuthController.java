package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.UserLoginDTO;
import com.example.demo.model.Feature;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO userlogin) {
        String username = userlogin.getUsername();
        String password = userlogin.getPassword();

        logger.info("login {}", username);
        try {
            // Authenticate
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Load user from DB
            logger.info("find username {}", username);
            User user = userRepo.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            logger.info("generate token {}", username);
            // Generate JWT
            String token = jwtUtil.generateToken(user); // Update this method to accept User

            // Extract roles & features
            List<String> roles = user.getRoles().stream()
                                    .map(Role::getName)
                                    .toList();

            List<String> features = user.getRoles().stream()
                .flatMap(role -> role.getFeatures().stream())
                .map(Feature::getName)
                .distinct()
                .toList();

            // Return full response
            return ResponseEntity.ok(new AuthResponseDTO(token, username, roles, features));
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Unexpected login error for {}: {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
