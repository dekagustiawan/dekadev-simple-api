package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
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

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserRepository userRepo,
            PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userlogin) {
        String username = userlogin.getUsername();
        String password = userlogin.getPassword();

        logger.info("login {}", username);

        try {
            // Authenticate user
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Load user and reset login retry
            User user = userRepo.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            user.setLoginRetry(0); // reset retry
            userRepo.save(user);

            // Generate token
            String token = jwtUtil.generateToken(user);

            List<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();

            List<String> features = user.getRoles().stream()
                    .flatMap(role -> role.getFeatures().stream())
                    .map(Feature::getName)
                    .distinct()
                    .toList();

            return ResponseEntity.ok(new AuthResponseDTO(token, username, roles, features));

        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for user: {}", username);

            // Increment login retry and disable if needed
            User user = userRepo.findByUsername(username);
            if (user != null) {
                int retry = user.getLoginRetry() + 1;
                user.setLoginRetry(retry);

                if (retry >= 3) {
                    user.setUserDisabled(true);
                    logger.warn("User {} has been disabled due to too many login failures", username);
                }

                userRepo.save(user);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "error", "Unauthorized",
                            "message", "Invalid username or password"
                    ));

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "status", 403,
                            "error", "Forbidden",
                            "message", "Your account has been disabled"
                    ));

        } catch (CredentialsExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "error", "Password Expired",
                            "message", "Your password has expired. Please reset it."
                    ));

        } catch (Exception e) {
            logger.error("Unexpected login error for {}: {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "error", "Internal Server Error",
                            "message", e.getMessage()
                    ));
        }
    }

}
