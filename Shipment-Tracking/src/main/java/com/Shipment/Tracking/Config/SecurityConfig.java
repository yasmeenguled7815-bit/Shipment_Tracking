package com.Shipment.Tracking.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Shipment.Tracking.Security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/auth/**").permitAll()
						
						.requestMatchers("/ws/**").permitAll()

						.requestMatchers("/shipper/**").hasRole("SHIPPER")

						.requestMatchers("/carrier/**").hasRole("CARRIER")

						.anyRequest().authenticated())
						
						.sessionManagement(session ->
		                session.sessionCreationPolicy(
		                    SessionCreationPolicy.STATELESS))
						
						.addFilterBefore(
								jwtAuthFilter,
								UsernamePasswordAuthenticationFilter.class);
		                
		
		return http.build();

	}
	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}
