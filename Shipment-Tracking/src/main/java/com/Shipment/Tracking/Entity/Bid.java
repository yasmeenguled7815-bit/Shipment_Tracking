package com.Shipment.Tracking.Entity;

import com.Shipment.Tracking.Enum.BidStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Bid {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double amount;
	
	@Enumerated(EnumType.STRING)
	private BidStatus status=BidStatus.PENDING;
	
	@ManyToOne
	@JoinColumn(name="shipment_id")
	private Shipment shipment;
	
	@ManyToOne
	@JoinColumn(name="carrier_id")
	private User carrier;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public BidStatus getStatus() {
		return status;
	}

	public void setStatus(BidStatus status) {
		this.status = status;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public User getCarrier() {
		return carrier;
	}

	public void setCarrier(User carrier) {
		this.carrier = carrier;
	}

	
}
