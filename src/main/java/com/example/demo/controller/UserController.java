package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;
  public UserController(UserService userService) { this.userService = userService; }

  @GetMapping public List<User> all() { return userService.getAll(); }
  @GetMapping("/{id}") public User one(@PathVariable Long id) { return userService.getById(id); }
  @PostMapping public User create(@RequestBody User u) { return userService.create(u); }
  @PutMapping("/{id}") public User update(@PathVariable Long id, @RequestBody User u) { return userService.update(id, u); }
  @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { userService.delete(id); }
}
