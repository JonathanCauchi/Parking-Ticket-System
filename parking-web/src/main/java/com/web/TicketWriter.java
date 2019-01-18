package com.web;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class TicketWriter {

	public boolean writeTickets(ArrayList<Ticket> tickets) {
		String result = "<Tickets>\n";
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			result += parseTicketToXml(ticket) + "\n";
		}
		result += "</Tickets>\n";
		try {
			FileOutputStream output = new FileOutputStream(
					new File(new ParkingLot().getClass().getClassLoader().getResource("Tickets.xml").toURI()));
			try {
				output.write(result.getBytes());
				output.flush();
				output.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	public String parseTicketToXml(Ticket ticket) {
		if (ticket == null) {
			return "null";
		}
		String result = "";
		result += "<Ticket ticketNumber=\"" + ticket.getTicketNumber() + "\">\n";
		result += "<SlotNumber>" + ticket.getSlotNumber() + "</SlotNumber>\n";
		result += "<StartTime>" + ticket.getStartTime() + "</StartTime>\n";
		result += "<EndTime>" + ticket.getEndTime() + "</EndTime>\n";
		result += "<SlotPosition>" + ticket.getSlotPosition() + "</SlotPosition>\n";
		result += "<TotalFee>" + ticket.getTotalFee() + "</TotalFee>\n";
		result += "</Ticket>";
		return result;
	}

}
