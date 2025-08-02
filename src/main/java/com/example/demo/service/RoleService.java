package com.example.demo.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RoleRegisDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Feature;
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

    public Role getById(Long id) {
    return repo.findById(id)
               .orElseThrow(() -> new NotFoundException("Feature not found with ID: " + id));
    }

    public Role create(RoleRegisDTO dto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Role role = new Role();
        role.setName(dto.getName());

        // Fetch features from DB using IDs
        List<Feature> features = featureRepo.findAllById(dto.getFeatureId());
        role.setFeatures(new HashSet<>(features));

        role.setCreatedBy(currentUsername);
        role.setUpdatedBy(currentUsername);
        return repo.save(role);
    }

    public Role update(Long id, RoleRegisDTO updated) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Role r = repo.findById(id).orElseThrow();
        r.setName(updated.getName());
        // Fetch features from DB using IDs
        List<Feature> features = featureRepo.findAllById(updated.getFeatureId());
        r.setFeatures(new HashSet<>(features));
        r.setUpdatedBy(currentUsername);

        return repo.save(r);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
