package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepo;

  public UserService(UserRepository userRepo) {
    this.userRepo = userRepo;
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
}
