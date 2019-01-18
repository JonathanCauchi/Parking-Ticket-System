package com.web;

import static org.easymock.EasyMock.createNiceMock;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class CarParkAPITest {
	CarParkAPI carParkAPI;
	ParkingFinder parkingFinder;
	TicketWriter ticketWriter;

	@Before
	public void setUp() throws Exception {
		carParkAPI = new CarParkAPI();
		parkingFinder = createNiceMock(ParkingFinder.class);
		ticketWriter = createNiceMock(TicketWriter.class);
		carParkAPI.setParkingFinder(parkingFinder);
		carParkAPI.setTickerWriter(ticketWriter);
		
	};

	@Test
	public void shouldSuccessfullyParkTheCarAndGetTheSlotNumberWhenTheSlotIsAvailable() {
		ParkingSlot parkignSlot = new ParkingSlot();
		parkignSlot.setSlotNumber(123);
		parkignSlot.setAvailability(true);
		parkignSlot.setSlotPosition("A1");
		ArrayList<Ticket> parkingTicketList = new ArrayList<Ticket>();
		Ticket ticket  =new Ticket();
		ticket.setTicketNumber(123);
		ticket.setSlotNumber(123);
		parkingTicketList.add(ticket);
		
		EasyMock.expect(parkingFinder.getTickets()).andReturn(parkingTicketList);
		EasyMock.expect(parkingFinder.checkAvailability()).andReturn(parkignSlot);
		EasyMock.replay(parkingFinder);
		Ticket park = carParkAPI.park();
		Assert.assertEquals(parkignSlot.getSlotNumber(), park.getSlotNumber());
	}
	
	@Test
	public void shouldSuccessfullyIssueTicketNumberWhenParkingIsDoneSuccesfully() {
		ParkingSlot parkignSlot = new ParkingSlot();
		parkignSlot.setSlotNumber(123);
		parkignSlot.setAvailability(true);
		parkignSlot.setSlotPosition("A1");
		ArrayList<Ticket> parkingTicketList = new ArrayList<Ticket>();
		Ticket ticket  =new Ticket();
		ticket.setTicketNumber(123);
		ticket.setSlotNumber(123);
		parkingTicketList.add(ticket);
		
		EasyMock.expect(parkingFinder.getTickets()).andReturn(parkingTicketList);
		EasyMock.expect(parkingFinder.checkAvailability()).andReturn(parkignSlot);
		EasyMock.replay(parkingFinder);
		Ticket park = carParkAPI.park();
		Assert.assertNotNull(park.getTicketNumber());
	}
	@Test
	public void shouldSuccessfullyPayInvoiceWhenValidParkingTicketNumberIsProvided() {
		ArrayList<ParkingSlot> parkingSlotList = new ArrayList<ParkingSlot>();
		ParkingSlot parkignSlot = new ParkingSlot();
		parkignSlot.setSlotNumber(123);
		parkignSlot.setAvailability(true);
		parkignSlot.setSlotPosition("A1");
		parkingSlotList.add(parkignSlot);
		
		ArrayList<Ticket> parkingTicketList = new ArrayList<Ticket>();
		Ticket ticket  =new Ticket();
		ticket.setTicketNumber(123);
		ticket.setSlotNumber(123);
		parkingTicketList.add(ticket);
		
		EasyMock.expect(parkingFinder.getParkingSlots()).andReturn(parkingSlotList);
		EasyMock.expect(parkingFinder.getTickets()).andReturn(parkingTicketList);
		
		EasyMock.replay(parkingFinder);
		String message = carParkAPI.payTicket(123,12,12.1);
		Assert.assertEquals("success", message);
	}

}
