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

import com.example.demo.model.Feature;
import com.example.demo.service.FeatureService;

@RestController
@RequestMapping("/api/features")
public class FeatureController {
    private final FeatureService service;

    public FeatureController(FeatureService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Read Feature')")
    @GetMapping 
    public List<Feature> all() { return service.getAll(); }
    
    @PreAuthorize("hasRole('Create Feature')")
    @PostMapping 
    public Feature create(@RequestBody Feature f) { return service.create(f); }
    
    @PreAuthorize("hasRole('Update Feature')")
    @PutMapping("/{id}") 
    public Feature update(@PathVariable Long id, @RequestBody Feature f) { return service.update(id, f); }
    
    @PreAuthorize("hasRole('Delete Feature')")
    @DeleteMapping("/{id}") 
    public void delete(@PathVariable Long id) { service.delete(id); }
}

