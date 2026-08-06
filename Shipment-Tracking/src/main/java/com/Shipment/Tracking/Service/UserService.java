package com.Shipment.Tracking.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Shipment.Tracking.Entity.User;
import com.Shipment.Tracking.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//register user
	
	public User register(User user) {
		
		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepository.save(user);
	}
	
	//login user
	
	public User login(String email,String password) {
		
		User user=userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Invalid Email"));
	
	
	if(!passwordEncoder.matches(password,user.getPassword())) {
		throw new RuntimeException("Invalid Password");
	}
	return user;
	}
	
}
