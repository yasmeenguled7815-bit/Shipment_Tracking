package com.Shipment.Tracking.DTO;

public class LocationUpdate {

	private Long shipmentId;
	private double latitude;
	private double longitude;
	
	public LocationUpdate(Long shipmentId, double latitude, double longitude) {
		this.shipmentId=shipmentId;
		this.latitude=latitude;
	}
	public Long getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
