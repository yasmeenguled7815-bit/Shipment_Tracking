package com.Shipment.Tracking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shipment.Tracking.Entity.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment,Long>{
	

}
