package com.venyou.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venyou.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	   // Find a user by email
    Optional<User> findByEmail(String email);

    // Find a user by phone number
    Optional<User> findByPhone(String phone);

    // Check if a user exists by email (for validation)
    boolean existsByEmail(String email);

    // Check if a user exists by phone number (for validation)
    boolean existsByPhone(String phone);

}
