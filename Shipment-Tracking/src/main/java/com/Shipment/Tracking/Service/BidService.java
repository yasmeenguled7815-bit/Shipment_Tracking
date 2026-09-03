package com.Shipment.Tracking.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shipment.Tracking.Entity.Bid;
import com.Shipment.Tracking.Entity.Shipment;
import com.Shipment.Tracking.Entity.User;
import com.Shipment.Tracking.Enum.BidStatus;
import com.Shipment.Tracking.Enum.ShipmentStatus;
import com.Shipment.Tracking.Repository.BidRepository;
import com.Shipment.Tracking.Repository.ShipmentRepository;
import com.Shipment.Tracking.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BidService {

	@Autowired
	private BidRepository bidRepository;
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//carrier places a bid
	
	public Bid placeBid(Long shipmentId, Long carrierId, Double amount) {
		
		Shipment shipment=shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new RuntimeException("Shipment not found"));
		
		User carrier=userRepository.findById(carrierId)
				.orElseThrow(() -> new RuntimeException("Carrier not found"));
		
		//shipment must still be  available
		
		if(shipment.getStatus() !=ShipmentStatus.PENDING) {
			throw new RuntimeException("Bidding is closed for this shipment");
		}
		
		Bid bid=new Bid();
		
		bid.setAmount(amount);
		bid.setStatus(BidStatus.PENDING);
		bid.setShipment(shipment);
		bid.setCarrier(carrier);
		
		return bidRepository.save(bid);
	}
	
	//get all bids
	
	public List<Bid> getAllBids(){
		return bidRepository.findAll();
	}
	// get bids for a particular shipment
	
	public List<Bid> getBidsForShipment(Long shipmentId){
		return bidRepository.findByShipmentId(shipmentId);
	}
	
	//shipper accept a bid
	@Transactional
	public Bid acceptBid(Long bidId) {
		
		Bid acceptedBid=bidRepository.findById(bidId)
				.orElseThrow(() -> new RuntimeException("Bid not found"));
		
		Shipment shipment=acceptedBid.getShipment();
		
		//make sure shipment is still available
		
		if(shipment.getStatus() !=ShipmentStatus.PENDING) {
			throw new RuntimeException("Shipment is no longer available for bidding");
		}
		
		//accept selected bid
		
		acceptedBid.setStatus(BidStatus.ACCEPTED);
		
		//reject every other bid
		
		List<Bid> allBids=bidRepository.findByShipmentId(shipment.getId());
		
		for(Bid bid : allBids) {
			if(!bid.getId().equals(acceptedBid.getId())) {
				bid.setStatus(BidStatus.REJECTED);
			}
		}
		
		bidRepository.saveAll(allBids);
		
		//Lock shipment
		
		shipment.setStatus(ShipmentStatus.AWAITING_PICKUP);
		
		shipmentRepository.save(shipment);
		
		return bidRepository.save(acceptedBid);
	}
}
