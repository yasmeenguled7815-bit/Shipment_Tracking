package com.Shipment.Tracking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Shipment.Tracking.Entity.Bid;
import com.Shipment.Tracking.Service.BidService;

@RestController
@RequestMapping("/carrier")
@CrossOrigin("*")
public class BidController {

	@Autowired
	private BidService service;
	
	//carrier place a bid
	
	@PostMapping("/bids")
	public Bid placeBid(@RequestParam Long shipmentId,
			@RequestParam Long carrierId, 
			@RequestParam Double amount) {
		return service.placeBid(shipmentId,carrierId,amount);
	}
	
	//carrier get all bids
	@GetMapping("/bids")
	public List<Bid> getAllBids(){
		return service.getAllBids();
	}
}
