package com.Shipment.Tracking.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain security(HttpSecurity http) throws Exception{
		
		http.csrf(csrf -> csrf.disable())
		        .authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/shipper/**").hasRole("SHIPPER")
				.requestMatchers("/carrier/**").hasRole("CARRIER")
				.anyRequest().authenticated());
		
		return http.build();
	}

}
