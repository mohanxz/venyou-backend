package com.venyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venyou.model.BookingAdvanceDetails;

@Repository
public interface BookingAdvanceDetailsRepository extends JpaRepository<BookingAdvanceDetails, Long> {

}
