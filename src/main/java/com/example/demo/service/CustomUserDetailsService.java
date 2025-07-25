package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if (user != null) {
            if (user.isUserDisabled()) {
                throw new DisabledException("User account is disabled");
            }

            if (user.isPasswordExpired()) {
                throw new CredentialsExpiredException("Password expired. Please reset your password.");
            }

            if (user.isUserDisabled()) {
                throw new DisabledException("User account is disabled");
            }

            user.setLoginRetry(user.getLoginRetry() + 1);
            
            if (user.getLoginRetry() >= 3) {
                user.setUserDisabled(true);
            }
            repo.save(user);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                    .flatMap(r -> r.getFeatures().stream())
                    .map(f -> new SimpleGrantedAuthority("ROLE_" + f.getName()))
                    .collect(Collectors.toSet())
        );
    }
}
