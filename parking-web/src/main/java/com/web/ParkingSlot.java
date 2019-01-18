package com.web;


/**
 * @author JonathanCauchi
 * 
 * This class represents individual parking slot and stores slot number and its availability status
 */
public class ParkingSlot 
{
	private int slotNumber;
	private boolean availability;
	private String slotPosition;
	
	public ParkingSlot() {

	}
	public int getSlotNumber() {
		return slotNumber;
	}
	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}
	public boolean isAvailability() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	public String getSlotPosition() {
		return slotPosition;
	}
	public void setSlotPosition(String slotPosition) {
		this.slotPosition = slotPosition;
	}
	public ParkingSlot(int slotNumber, boolean availability, String slotPosition) {
		super();
		this.slotNumber = slotNumber;
		this.availability = availability;
		this.slotPosition = slotPosition;
	}
	
	
	
	
}
