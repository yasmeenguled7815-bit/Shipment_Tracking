package com.Shipment.Tracking.Entity;

import com.Shipment.Tracking.Enum.ShipmentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Shipment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String source;
	
	private String destination;
	
	private Double weight;
	
	@Enumerated(EnumType.STRING)
	private ShipmentStatus status;
	
	@ManyToOne
	@JoinColumn(name="shipper_id")
	private User shipper;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public ShipmentStatus getStatus() {
		return status;
	}

	public void setStatus(ShipmentStatus open) {
		this.status = open;
	}

	public User getShipper() {
		return shipper;
	}

	public void setShipper(User shipper) {
		this.shipper = shipper;
	}
}
