package com.Shipment.Tracking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Shipment.Tracking.Entity.Bid;
import com.Shipment.Tracking.Service.BidService;

@RestController
@RequestMapping("/carrier")
@CrossOrigin("*")
public class BidController {

	@Autowired
	private BidService service;
	
	@PostMapping("/bids")
	public Bid placeBid(@RequestBody Bid bid) {
		return service.placeBid(bid);
	}
	
	@GetMapping("/bids")
	public List<Bid> getAllBids(){
		return service.getAllBids();
	}
}
