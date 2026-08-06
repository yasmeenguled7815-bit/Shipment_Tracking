package com.Shipment.Tracking.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shipment.Tracking.Entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByEmail(String email);
}
