package com.Shipment.Tracking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shipment.Tracking.Entity.Shipment;
import com.Shipment.Tracking.Enum.ShipmentStatus;

public interface ShipmentRepository extends JpaRepository<Shipment,Long>{
	
List<Shipment> findByStatus(ShipmentStatus status);

List<Shipment> findByStatusAndSourceContainingIgnoreCase(ShipmentStatus status, String source);

List<Shipment> findByStatusAndDestinationContainingIgnoreCase(ShipmentStatus status, String destination);


}
