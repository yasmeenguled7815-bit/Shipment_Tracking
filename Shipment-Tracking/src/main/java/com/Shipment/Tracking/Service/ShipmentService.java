package com.Shipment.Tracking.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shipment.Tracking.Entity.Shipment;
import com.Shipment.Tracking.Enum.ShipmentStatus;
import com.Shipment.Tracking.Repository.ShipmentRepository;

@Service
public class ShipmentService {
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	public Shipment createShipment(Shipment shipment) {
		shipment.setStatus(ShipmentStatus.OPEN);
		return shipmentRepository.save(shipment);
	}
	
	public List<Shipment> getAllShipment(){
		return shipmentRepository.findAll();
	}
	
	public  Shipment getShipmentById(Long id) {
		return shipmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Shipment Not found"));
	}

}
