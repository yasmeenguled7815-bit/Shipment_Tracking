package com.Shipment.Tracking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Shipment.Tracking.DTO.LoginRequest;
import com.Shipment.Tracking.Entity.User;
import com.Shipment.Tracking.Service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService service;
	
	@PostMapping("/register")
	public User register(@RequestBody User user) {
		return service.register(user);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest request) {
		
		User loggedUser=service.login(request.getEmail(),request.getPassword());
		return "Login Successful :"+loggedUser.getRole();
	}

}
