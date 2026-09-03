package com.Shipment.Tracking.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Shipment.Tracking.Entity.Shipment;
import com.Shipment.Tracking.Entity.User;
import com.Shipment.Tracking.Enum.ShipmentStatus;
import com.Shipment.Tracking.Repository.ShipmentRepository;
import com.Shipment.Tracking.Repository.UserRepository;

@Service
public class ShipmentService {
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Shipment createShipment(Shipment shipment) {
		
		//get current looged-in user
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		
	//	String email=authentication.getName();
		
		//find user from db
		User shipper=(User)authentication.getPrincipal();
		
		//set shipper automatically
		shipment.setShipper(shipper);
		
		//set status automatically
		shipment.setStatus(ShipmentStatus.PENDING);
		
		return shipmentRepository.save(shipment);
	}
	
	public List<Shipment> getAllShipment(){
		return shipmentRepository.findAll();
	}
	
	public  Shipment getShipmentById(Long id) {
		return shipmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Shipment Not found"));
	}

}
