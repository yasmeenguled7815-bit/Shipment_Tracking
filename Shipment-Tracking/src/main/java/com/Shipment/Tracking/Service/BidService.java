package com.Shipment.Tracking.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shipment.Tracking.Entity.Bid;
import com.Shipment.Tracking.Repository.BidRepository;

@Service
public class BidService {

	@Autowired
	private BidRepository bidRepository;
	
	public Bid placeBid(Bid bid) {
		return bidRepository.save(bid);
	}
	
	public List<Bid> getAllBids(){
		return bidRepository.findAll();
	}
}
