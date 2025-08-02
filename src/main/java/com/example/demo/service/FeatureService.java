package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Feature;
import com.example.demo.repository.FeatureRepository;

@Service
public class FeatureService {
    private final FeatureRepository repo;

    public FeatureService(FeatureRepository repo) {
        this.repo = repo;
    }

    public List<Feature> getAll() {
        return repo.findAll();
    }

   public Feature getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Feature not found with ID: " + id));
    }

    public Feature create(Feature feature) {
        return repo.save(feature);
    }

    public Feature update(Long id, Feature updated) {
        Feature f = repo.findById(id).orElseThrow();
        f.setName(updated.getName());
        return repo.save(f);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
