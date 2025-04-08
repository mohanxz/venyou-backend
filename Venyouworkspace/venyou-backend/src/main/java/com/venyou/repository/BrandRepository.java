package com.venyou.repository;

import com.venyou.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    
    // Find a brand by its name
    Optional<Brand> findByName(String name);
    
}
