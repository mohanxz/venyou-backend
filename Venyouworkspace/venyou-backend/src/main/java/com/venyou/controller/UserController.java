package com.venyou.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venyou.model.User;
import com.venyou.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	 @Autowired
	    private UserService userService;

	    @PostMapping
	    public User createUser(@RequestBody User user) {
	        return userService.saveUser(user);
	    }
	    
	    @GetMapping
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }
	    
	    @GetMapping("/{id}")
	    public Optional<User> getUserById(@PathVariable Long id) {
	        return userService.getUserById(id);
	    }

	    @GetMapping("/email/{email}")
	    public Optional<User> getUserByEmail(@PathVariable String email) {
	        return userService.getUserByEmail(email);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteUser(@PathVariable Long id) {
	        userService.deleteUser(id);
	    }
	

}
