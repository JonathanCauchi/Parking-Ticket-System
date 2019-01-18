package com.client;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Schedule {
	
	private static final Logger logger = Logger.getLogger(Schedule.class);

	public static List<RateSchedule> getRatesWithSchedule() {
		ArrayList<RateSchedule> listOfRateSchedules = new ArrayList<RateSchedule>();
		try {

			File fXmlFile = new File(
					RateSchedule.class.getClassLoader().getResource("RateSchedule.xml").toURI());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			logger.debug("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("RateSchedule");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				logger.debug("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					RateSchedule rateSchedule = new RateSchedule();
				
					rateSchedule.setSchedule(
							eElement.getElementsByTagName("schedule").item(0).getTextContent());
					
					rateSchedule.setPrice(Double.parseDouble(eElement.getElementsByTagName("price").item(0).getTextContent()));
					listOfRateSchedules.add(rateSchedule);
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return listOfRateSchedules;
	}


}
