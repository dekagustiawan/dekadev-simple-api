package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Feature;


public interface FeatureRepository extends JpaRepository<Feature, Long> {
    public Optional<Feature> findById(Long id);

}
