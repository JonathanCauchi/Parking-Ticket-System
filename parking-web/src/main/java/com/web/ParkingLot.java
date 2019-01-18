package com.web;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author JonathanCauchi
 * 
 *         This class has count of parking slots list of all parking slots
 */
public class ParkingLot {



	public static String parseParkingSlotToXml(ParkingSlot slot) {
		if (slot == null) {
			return "null";
		}
		String result = "";
		result += "<ParkingSlot slotNumber=\"" + slot.getSlotNumber() + "\">\n";
		result += "<SlotPosition>" + slot.getSlotPosition() + "</SlotPosition>\n";
		result += "<Available>" + slot.isAvailability() + "</Available>\n";
		result += "</ParkingSlot>";
		return result;
	}



	public static void writeSlots(List<ParkingSlot> slots) {
		String result = "<ParkingLot>\n";
		for (int i = 0; i < slots.size(); i++) {
			ParkingSlot slot = slots.get(i);
			result += parseParkingSlotToXml(slot) + "\n";
		}
		result += "</ParkingLot>\n";
		try {
			FileOutputStream output = new FileOutputStream(
					new File(new ParkingLot().getClass().getClassLoader().getResource("ParkingSlots.xml").toURI()));
			try {
				output.write(result.getBytes());
				output.flush();
				output.close();
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

	}



	public void writeParkingSlotToFile(ArrayList<ParkingSlot> slots) {

	}
}
