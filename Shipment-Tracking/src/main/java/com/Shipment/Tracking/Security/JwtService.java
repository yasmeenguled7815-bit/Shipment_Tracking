package com.Shipment.Tracking.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.Shipment.Tracking.Entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey secretKey=Keys.hmacShaKeyFor("mySecretKeyForShipmentTrackingApplication123456".getBytes());
	
	private final long expiration=1000*60*60;  //1 hour
	
	//generate JWT token
	public String generateToken(User user) {
		
		return Jwts.builder()
				.setSubject(user.getEmail())
				.claim("role", user.getRole().name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+expiration))
				.signWith(secretKey)
				.compact();
	}
	
	//extract email from jwt
	
	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	//Valid jwt
	
	public boolean isTokenValid(String token, User user) {
		
		String email=extractEmail(token);
		
		return email.equals(user.getEmail()) && !isTokenExpired(token);
	}
	
	//check expiration
	
	private boolean isTokenExpired(String token) {
		
		return extractAllClaims(token)
				.getExpiration().before(new Date());
	}
	
	//Extract claims
	
	private Claims extractAllClaims(String token) {
		
		return Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
		
	}
	
	
	
}
