package com.web;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParkingFinder {
	
	private List<ParkingSlot> slots = null;
	/**
	 * 
	 * This method checks for available slots in the parking lot
	 * 
	 * @return slot if available or null if no slots are available
	 */
	public ParkingSlot checkAvailability() {
		slots = getParkingSlots();
		for (int i = 0; i < slots.size(); i++) {
			ParkingSlot slot = slots.get(i);
			if (slot.isAvailability()) {
				slots.get(i).setAvailability(false);
				ParkingLot.writeSlots(slots);
				return slot;
			}
		}
		return null;
	}
	
	
	/**
	 * This method returns list of all the parking slots
	 * 
	 * @return list of the slots in the parking lot
	 */
	public  List<ParkingSlot> getParkingSlots() {
		ArrayList<ParkingSlot> listOfSlots = new ArrayList<ParkingSlot>();
		try {

			File fXmlFile = new File(
					new ParkingLot().getClass().getClassLoader().getResource("ParkingSlots.xml").toURI());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("ParkingSlot");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					ParkingSlot slot = new ParkingSlot();
					slot.setSlotNumber(Integer.parseInt(eElement.getAttribute("slotNumber")));
					slot.setAvailability(
							Boolean.parseBoolean(eElement.getElementsByTagName("Available").item(0).getTextContent()));

					slot.setSlotPosition(eElement.getElementsByTagName("SlotPosition").item(0).getTextContent());
					listOfSlots.add(slot);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listOfSlots;
	}
	
	public  ArrayList<Ticket> getTickets() {
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		try {

			File fXmlFile = new File(new ParkingLot().getClass().getClassLoader().getResource("Tickets.xml").toURI());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Ticket");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					Ticket ticket = new Ticket();
					ticket.setTicketNumber(Integer.parseInt(eElement.getAttribute("ticketNumber")));
					ticket.setSlotNumber(
							Integer.parseInt(eElement.getElementsByTagName("SlotNumber").item(0).getTextContent()));
					ticket.setStartTime(
							Long.parseLong(eElement.getElementsByTagName("StartTime").item(0).getTextContent()));
					ticket.setEndTime(
							Long.parseLong(eElement.getElementsByTagName("EndTime").item(0).getTextContent()));
					ticket.setSlotPosition(eElement.getElementsByTagName("SlotPosition").item(0).getTextContent());
					ticket.setTotalFee(
							Double.parseDouble(eElement.getElementsByTagName("TotalFee").item(0).getTextContent()));
					tickets.add(ticket);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return tickets;
	}
	

}
