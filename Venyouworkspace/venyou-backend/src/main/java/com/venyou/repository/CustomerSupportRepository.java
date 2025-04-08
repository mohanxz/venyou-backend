package com.venyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venyou.model.CustomerSupport;
@Repository
public interface CustomerSupportRepository extends JpaRepository<CustomerSupport, Long> {

}
