package com.venyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venyou.model.HallAvailability;

@Repository
public interface HallAvailabilityRepository extends JpaRepository<HallAvailability, Long> {

}
