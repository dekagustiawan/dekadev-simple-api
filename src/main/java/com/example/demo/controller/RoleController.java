package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Role;
import com.example.demo.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping public List<Role> all() { return service.getAll(); }
    @PostMapping public Role create(@RequestBody Role r) { return service.create(r); }
    @PutMapping("/{id}") public Role update(@PathVariable Long id, @RequestBody Role r) { return service.update(id, r); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}

