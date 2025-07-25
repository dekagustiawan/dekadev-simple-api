package com.example.demo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserRegisDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Read User')")
    @GetMapping 
    public List<User> all() { return service.getAll(); }

    @PreAuthorize("hasRole('Update User')")
    @PutMapping("/{id}") 
    public User update(@PathVariable Long id, @RequestBody User u) { return service.update(id, u); }
    
    @PreAuthorize("hasRole('Delete User')")
    @DeleteMapping("/{id}") 
    public void delete(@PathVariable Long id) { service.delete(id); }    
    
    @PreAuthorize("hasRole('Create User')")
    @PostMapping("/register")
    public User createUser(@RequestBody UserRegisDTO u) { return service.createUser(u); }

    @PreAuthorize("hasRole('Block User')")
    @PutMapping("/blcok/{id}") 
    public void block(@PathVariable Long id ) { service.blcok(id); }
}