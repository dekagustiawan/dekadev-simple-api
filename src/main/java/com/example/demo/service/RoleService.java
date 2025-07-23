package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.repository.FeatureRepository;
import com.example.demo.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository repo;
    private final FeatureRepository featureRepo;

    public RoleService(RoleRepository repo, FeatureRepository featureRepo) {
        this.repo = repo;
        this.featureRepo = featureRepo;
    }

    public List<Role> getAll() {
        return repo.findAll();
    }

    public Role create(Role role) {
        return repo.save(role);
    }

    public Role update(Long id, Role updated) {
        Role r = repo.findById(id).orElseThrow();
        r.setName(updated.getName());
        r.setFeatures(updated.getFeatures());
        return repo.save(r);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
