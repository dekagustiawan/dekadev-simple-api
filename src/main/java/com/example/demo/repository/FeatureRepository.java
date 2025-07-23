package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

}
