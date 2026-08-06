package com.Shipment.Tracking.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Shipment.Tracking.Entity.Shipment;
import com.Shipment.Tracking.Service.ShipmentService;

@RestController
@RequestMapping("/shipper")
@CrossOrigin("*")
public class ShipmentController {
	
	@Autowired
	private ShipmentService shipmentService;
	
	@PostMapping("/shipment")
	public Shipment createShipment(@RequestBody Shipment shipment) {
		return shipmentService.createShipment(shipment);
	}
	
	@GetMapping("/shipments")
	public List<Shipment> myShipment(){
		return shipmentService.getAllShipment();
	}
	
	@GetMapping("/shipment/{id}")
	public Shipment getShipment(@PathVariable Long id) {
		return shipmentService.getShipmentById(id);
	}
	

}
