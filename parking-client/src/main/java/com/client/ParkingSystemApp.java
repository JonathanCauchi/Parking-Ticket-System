package com.client;


import java.sql.Date;
import java.util.List;


/**
 * @author JonathanCauchi
 * 
 *  This application simulates the working of a automated parking system.
 *  
 *  User parks the car in the slot alloted by the automated parking system. It generates a ticket with ticket number for the user.
 *  Also, driver uses this system and the ticket printed to pay the fee for the duration the car was parked in the slot.
 *  This application tracks the available slots and also informs the user if the parking lot is full.
 */
public class ParkingSystemApp {

	/**
	 * Launch the application.
	 */
	static ParkingSystemFrame mainFrame;
	
	
	
	private long startTimeMilliseconds;
	private long startTime = 0;
	private long endTimeMilliseconds;
	private String durationParked;
	private Date date;
	
	private  double fee = 0.5; // Parking fee $0.5 for 15 minutes
	private static final int minimumTime = 15;
	int timeInMinutes = 0;
	private double totalFee = 0;
	PaymentInformation payInfo = null;
	
	public ParkingSystemApp() 
	{
		
	}
	
	/**
	 * This method checks for available parking slot and generates a ticket if slot is available
	 * 
	 * @return Ticket object reference if there is a slot available or null if no slots available
	 */
	public Ticket park()
	{
		String url = RestHelper.MAIN_DOMAIN + "/park";
		try {
			String result = RestHelper.callRest(url, "application/xml").trim();
			if("null".equals(result)) {
				return null;
				
			}
			else {
				return RestHelper.parseTicket(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * This method checks for available parking slot and generates a ticket if slot is available
	 * 
	 * @return Ticket object reference if there is a slot available or null if no slots available
	 */
	public Ticket validateTicketNumber(int ticketNumber)
	{
		String url = RestHelper.MAIN_DOMAIN + "/validate-ticket/"+ticketNumber;
		try {
			String result = RestHelper.callRest(url, "application/xml").trim();
			if("null".equals(result)) {
				return null;
				
			}
			else {
				return RestHelper.parseTicket(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * This method checks for available parking slot and generates a ticket if slot is available
	 * 
	 * @return Ticket object reference if there is a slot available or null if no slots available
	 */
	public boolean payTicket(int ticketNumber, long endTime, double fee)
	{
		String url = RestHelper.MAIN_DOMAIN + "/pay-ticket/"+ticketNumber+"/"+endTime+"/"+fee;
		try {
			String result = RestHelper.callRest(url, "application/xml").trim();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	
	/**
	 * This method saves the end time
	 */
	public void captureEndTime()
	{
		// capture end time
		endTimeMilliseconds = System.currentTimeMillis();
	}
	
	
	/**
	 * This method calculates the total time (in minutes) the car was parked in the parking lot
	 */
	public void calculateTotalMinutes(Ticket ticket)
	{
		captureEndTime();
		startTime = ticket.getStartTime();
		long durationMilliSeconds = endTimeMilliseconds - startTime; // total time the card was parked in the slot
		durationParked = convertTimeFormat(durationMilliSeconds);
		String [] time = durationParked.split(":");
		int hours = Integer.parseInt(time[0]);
		int minutes = Integer.parseInt(time[1]);
		int seconds = Integer.parseInt(time[2]);
		checkParkingRateSchedule(minutes);
		timeInMinutes = (hours * 60) + minutes + (seconds / 60);
	}
	
        /**
	 * This method modified the parking fee based on the prices set in RateSchedule.xml file
	 */
	public void checkParkingRateSchedule(long durationParked) {
		List<RateSchedule> ratesWithSchedule = Schedule.getRatesWithSchedule();
		for (RateSchedule rateSchedule : ratesWithSchedule) {
			String[] schedule = rateSchedule.getSchedule().split(" - ");

			if (durationParked >= Integer.parseInt(schedule[0]) && durationParked <= Integer.parseInt(schedule[1])) {
				fee = rateSchedule.getPrice();
			}

		}
	}
	
	/**
	 * This method calculate the total fee due payment for the duration the car was parked in the parking lot
	 * 
	 * @return total fee calculated
	 */
	public double getTotalFee()
	{
		if(totalFee == 0)
		{
			if (timeInMinutes < 15)
				totalFee = 0.5;
			else
				totalFee = (timeInMinutes / minimumTime) * fee;
		}
		
		return totalFee;
	}
		
	
	/**
	 * This method creates a payment information object and sets all the credit card details.
	 * 
	 * @param ccNumber users credit card number
	 * @param cvvNumber ccv number of the credit card
	 * @param expiry credit card expiry date
	 */
	public void setPaymentInformation(String cardtype, String ccNumber, String cvvNumber, String expiry)
	{
		payInfo = new PaymentInformation(cardtype, ccNumber, cvvNumber, expiry);
	}
	
	/**
	 * This method sends the payment information to the bank class
	 * 
	 * @return true if validation is successful else false (For demo - Assumption is made that the validation is successful always.)
	 */
	public String validateCreditCard(String ccNumber)
	{
		
		return Bank.validateCreditCard(ccNumber);
	}
	
	public String convertTimeFormat(long milliSeconds)
	{
	    // Obtain the total seconds since midnight, Jan 1, 1970
	    long totalSeconds = milliSeconds / 1000;
	    // Compute the current second in the minute in the hour
	    long currentSecond = totalSeconds % 60;
	    // Obtain the total minutes
	    long totalMinutes = totalSeconds / 60;
	    // Compute the current minute in the hour
		long currentMinute = totalMinutes % 60;
	    // Obtain the total hours
	    long totalHours = totalMinutes / 60;
	    // Compute the current hour
	    long currentHour = totalHours % 12;
		
	    return currentHour + ":" + currentMinute + ":" + currentSecond;
	
	}
	
	
	public boolean updateSchedule()
	{
		String url = RestHelper.MAIN_DOMAIN + "/updateSchedule";
		try {
			String result = RestHelper.callRest(url, "application/xml").trim();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
}
