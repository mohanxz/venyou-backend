package com.venyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venyou.model.HallPricing;
@Repository
public interface HallPricingRepository extends JpaRepository<HallPricing, Long> {

}
