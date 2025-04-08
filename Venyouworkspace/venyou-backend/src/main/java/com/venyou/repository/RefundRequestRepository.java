package com.venyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venyou.model.RefundRequest;
@Repository
public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {

}
