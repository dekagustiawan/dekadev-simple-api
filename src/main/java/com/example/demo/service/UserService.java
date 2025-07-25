package com.example.demo.service;

import com.example.demo.dto.UserRegisDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
  public User update(Long id, User u) {
    User existing = userRepo.findById(id).orElseThrow();
    existing.setName(u.getName());
    existing.setUsername(u.getUsername());
    return userRepo.save(existing);
  }
  public void delete(Long id) { userRepo.deleteById(id); }
  public void blcok(Long id) { }

  public User createUser(UserRegisDTO userRegisDTO) {
      Role role = roleRepo.findById(userRegisDTO.getRoleId())
          .orElseThrow(() -> new RuntimeException("Role not found"));

      User user = new User();
      user.setName(userRegisDTO.getName());
      user.setUsername(userRegisDTO.getUsername());
      user.setPassword(passwordEncoder.encode(userRegisDTO.getPassword()));
      user.getRoles().add(role);

      return userRepo.save(user);
  }

}
