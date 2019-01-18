package com.web;


import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * The Class CarParkAPI.
 */
@Path("/")
public class CarParkAPI {

	/** The parking finder. */
	private ParkingFinder parkingFinder = new ParkingFinder();
	
	/** The ticker writer. */
	private TicketWriter tickerWriter = new TicketWriter();
	

	// http://localhost:1234/rest/{creditnumber}
	/**
	 * Check credit number.
	 *
	 * @param creditnumber the creditnumber
	 * @return the string
	 */
	//
	@Path("{creditnumber}")
	@GET

	@Produces("application/json")
	public String checkCreditNumber(@PathParam("creditnumber") String creditnumber) {

		boolean result = validateCreditCardNumber(creditnumber);
		if (!result) {
			return "Invalid card number!";
		}
		if (creditnumber.startsWith("4"))
			return "Visa";
		else if (creditnumber.startsWith("5"))
			return "MasterCard";
		else
			return "Unknown card type!";
	}

	/**
	 * Mark park.
	 *
	 * @return the string
	 */
	@Path("park")
	@GET
	@Produces("application/xml")
	public String markPark() {
		Ticket ticket = park();
		return tickerWriter.parseTicketToXml(ticket);
	}

	/**
	 * Validate ticket number.
	 *
	 * @param ticketNumber the ticket number
	 * @return the string
	 */
	@Path("validate-ticket/{ticketNumber}")
	@GET
	public String validateTicketNumber(@PathParam("ticketNumber") int ticketNumber) {
		ArrayList<Ticket> ticketList = parkingFinder.getTickets(); // save the
																// ticket in
																// ticketList
		for (int i = 0; i < ticketList.size(); i++) {
			if (ticketList.get(i).getTicketNumber() == ticketNumber) {
				return tickerWriter.parseTicketToXml(ticketList.get(i));
			}
		}
		return "null";
	}

	/**
	 * Pay ticket.
	 *
	 * @param ticketNumber the ticket number
	 * @param endTime the end time
	 * @param totalFee the total fee
	 * @return the string
	 */
	@Path("pay-ticket/{ticketNumber}/{endTime}/{totalFee}")
	@GET
	public String payTicket(@PathParam("ticketNumber") int ticketNumber, @PathParam("endTime") long endTime,
			@PathParam("totalFee") double totalFee) {
		ArrayList<Ticket> ticketList = parkingFinder.getTickets(); // save the
																// ticket in
																// ticketList
		for (int i = 0; i < ticketList.size(); i++) {
			if (ticketList.get(i).getTicketNumber() == ticketNumber) {
				ticketList.get(i).setEndTime(endTime);
				ticketList.get(i).setTotalFee(totalFee);
				tickerWriter.writeTickets(ticketList);
				List<ParkingSlot> slots = parkingFinder.getParkingSlots();
				for (int j = 0; j < slots.size(); j++) {
					if (slots.get(j).getSlotNumber() == ticketList.get(i).getSlotNumber()) {
						slots.get(j).setAvailability(true);
						ParkingLot.writeSlots(slots);
					}
				}
			}
		}
		return "success";
	}

	/**
	 * This method checks for available parking slot and generates a ticket if
	 * slot is available.
	 *
	 * @return Ticket object reference if there is a slot available or null if
	 *         no slots available
	 */
	public Ticket park() {
		ParkingSlot slot = parkingFinder.checkAvailability(); // check for available slots

		if (slot != null) {
			long startTimeMilliseconds = System.currentTimeMillis();

			Ticket ticket = new Ticket();
			ticket.setSlotNumber(slot.getSlotNumber());
			ticket.setStartTime(startTimeMilliseconds);
			ticket.setSlotPosition(slot.getSlotPosition());
			ticket.setTicketNumber(1);
			ArrayList<Ticket> ticketList = parkingFinder.getTickets(); // save the
																	// ticket in
																	// ticketList
			if (ticketList.size() > 0) {
				ticket.setTicketNumber(ticketList.get(ticketList.size() - 1).getTicketNumber() + 1);
			}
			ticketList.add(ticket);
			tickerWriter.writeTickets(ticketList);

			slot.setAvailability(false); // this slot is no more available
			return ticket;
		}
		return null;
	}



	/**
	 * Validate credit card number.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	private static boolean validateCreditCardNumber(String str) {
		int sum = 0;

		boolean alternate = false;
		for (int i = str.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(str.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}

		return (sum % 10 == 0);
	}
	
	/**
	 * Update schedule.
	 *
	 * @return the string
	 */
	@Path("updateSchedule")
	@GET
	public String updateSchedule() {
		ArrayList<Ticket> ticketList = parkingFinder.getTickets(); // save the
																// ticket in
																// ticketList
		for (int i = 0; i < ticketList.size(); i++) {}
		return "success";
	}

	/**
	 * Gets the parking finder.
	 *
	 * @return the parking finder
	 */
	public ParkingFinder getParkingFinder() {
		return parkingFinder;
	}

	/**
	 * Sets the parking finder.
	 *
	 * @param parkingFinder the new parking finder
	 */
	public void setParkingFinder(ParkingFinder parkingFinder) {
		this.parkingFinder = parkingFinder;
	}

	/**
	 * Gets the ticker writer.
	 *
	 * @return the ticker writer
	 */
	public TicketWriter getTickerWriter() {
		return tickerWriter;
	}

	/**
	 * Sets the ticker writer.
	 *
	 * @param tickerWriter the new ticker writer
	 */
	public void setTickerWriter(TicketWriter tickerWriter) {
		this.tickerWriter = tickerWriter;
	}
	
	@GET
	@Path("downloadSchedule")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFilebyPath() {
 		return download();
	}

	private Response download() {		
		ClassLoader classLoader = Schedule.class.getClassLoader();
		String fileLocation =classLoader.getResource("RateSchedule.xml").getFile().toString();
		Response response = null;
		NumberFormat myFormat = NumberFormat.getInstance();
	    myFormat.setGroupingUsed(true);
		
		// Retrieve the file 
		File file = new File(classLoader.getResource("RateSchedule.xml").getFile());
		if (file.exists()) {
			ResponseBuilder builder = Response.ok(file);
			builder.header("Content-Disposition", "attachment; filename=" + file.getName());
			response = builder.build();
			
			long file_size = file.length();
		} else {
			
			response = Response.status(404).
				      entity("FILE NOT FOUND: " + fileLocation).
				      type("text/plain").
				      build();
		}
		 
		return response;
	}

}