package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Role;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findById(Long id);
}
