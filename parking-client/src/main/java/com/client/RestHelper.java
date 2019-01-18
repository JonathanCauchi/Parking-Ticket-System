package com.client;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;

public class RestHelper {
	public static String MAIN_DOMAIN = "http://localhost:1234/parking-web/rest";
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	public static Ticket parseTicket(String s) {
		System.out.println(s);
		try {

		
			Document doc = loadXMLFromString(s);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Ticket");
			Ticket ticket = null;
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					ticket = new Ticket();
					ticket.setTicketNumber(Integer.parseInt(eElement.getAttribute("ticketNumber")));
					ticket.setSlotNumber(
							Integer.parseInt(eElement.getElementsByTagName("SlotNumber").item(0).getTextContent()));
					ticket.setStartTime(
							Long.parseLong(eElement.getElementsByTagName("StartTime").item(0).getTextContent()));
					ticket.setEndTime(
							Long.parseLong(eElement.getElementsByTagName("EndTime").item(0).getTextContent()));
					ticket.setSlotPosition(eElement.getElementsByTagName("SlotPosition").item(0).getTextContent());

				}
			}
			return ticket;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static String callRest(String urlStr, String applicationType) throws Exception {
		try {

			URL url = new URL(urlStr);
			System.out.println(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", applicationType);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String result = "";
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				result += output;
			}

			conn.disconnect();
			return result;

		} catch (MalformedURLException e) {

			e.printStackTrace();
			throw e;

		} catch (IOException e) {

			e.printStackTrace();
			throw e;

		}
	}
}
