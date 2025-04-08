package com.venyou.service;

import java.util.List;
import java.util.Optional;

import com.venyou.model.User;

public interface UserService {
	   User saveUser(User user);

	   List<User> getAllUsers();
	   
	   Optional<User> getUserById(Long userId);
	   
	   Optional<User> getUserByEmail(String email);
	   
	   void deleteUser(Long userId);}
