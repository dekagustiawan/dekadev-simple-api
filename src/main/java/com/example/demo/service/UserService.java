package com.example.demo.service;

import com.example.demo.dto.UserRegisDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepo;
  private final RoleRepository roleRepo;

  public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.passwordEncoder = passwordEncoder;
  }

  public List<User> getAll() { return userRepo.findAll(); }
  public User getById(Long id) { return userRepo.findById(id).orElse(null); }
  public User create(User u) {
    u.setId(null); 
    return userRepo.save(u); 
  }
  public User update(Long id, UserUpdateDTO u) {
    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    User existing = userRepo.findById(id).orElseThrow();
    Role role = roleRepo.findById(u.getRoleId())
        .orElseThrow(() -> new RuntimeException("Role not found"));


    existing.setName(u.getName());
    existing.setUsername(u.getUsername());
    existing.getRoles().add(role);
    existing.setUpdatedBy(currentUsername);
    return userRepo.save(existing);
  }
  public void delete(Long id) { userRepo.deleteById(id); }
  public void blcok(Long id) { }

  public User createUser(UserRegisDTO userRegisDTO) {
    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

    Role role = roleRepo.findById(userRegisDTO.getRoleId())
        .orElseThrow(() -> new RuntimeException("Role not found"));

    User user = new User();
    user.setName(userRegisDTO.getName());
    user.setUsername(userRegisDTO.getUsername());
    user.setPassword(passwordEncoder.encode(userRegisDTO.getPassword()));
    user.getRoles().add(role);

    user.setCreatedBy(currentUsername);
    user.setUpdatedBy(currentUsername);

      return userRepo.save(user);
  }
  public void resetPassword(Long userId, String newPassword, String performedBy) {
      User user = userRepo.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      user.setPassword(passwordEncoder.encode(newPassword));
      user.setPasswordExpired(false); // reset flag if expired
      user.setUpdatedBy(performedBy);
      user.setUpdatedTime(Instant.now());

      userRepo.save(user);
  }
  public void updateUserStatus(Long userId, boolean disabled, String updatedBy) {
      User user = userRepo.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
      user.setUserDisabled(disabled);
      user.setUpdatedBy(updatedBy);
      user.setUpdatedTime(Instant.now());
      userRepo.save(user);
  }

}
