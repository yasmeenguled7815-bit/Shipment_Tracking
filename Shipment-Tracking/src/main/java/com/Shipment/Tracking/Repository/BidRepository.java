package com.Shipment.Tracking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shipment.Tracking.Entity.Bid;

public interface BidRepository extends JpaRepository<Bid,Long> {

}
