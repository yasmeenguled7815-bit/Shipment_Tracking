package com.Shipment.Tracking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shipment.Tracking.Entity.Bid;
import com.Shipment.Tracking.Enum.BidStatus;

import java.util.List;


public interface BidRepository extends JpaRepository<Bid,Long> {
	
	List<Bid> findByShipmentId(Long shipmentId);
	
	List<Bid> findByShipmentIdAndStatus(Long ShipmentId, BidStatus status);
	
	List<Bid> findByCarrierId(Long carrierId);

}
