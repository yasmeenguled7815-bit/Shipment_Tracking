package com.Shipment.Tracking.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.Shipment.Tracking.DTO.LocationUpdate;

@Controller
public class TrackingWebSocketController {

	private final SimpMessagingTemplate messageingTemplate;
	
	public TrackingWebSocketController(
			SimpMessagingTemplate messageingTemplate ) {
		
		this.messageingTemplate=messageingTemplate;
	}
	
	@MessageMapping("/location")
	public void updateLocation(@Payload LocationUpdate location) {
		
		Long shipmentId=location.getShipmentId();
		
	      System.out.println("Location received for shipment: " + shipmentId);
	      
	      messageingTemplate.convertAndSend( "/topic/shipment" +shipmentId, location);
	}
}
