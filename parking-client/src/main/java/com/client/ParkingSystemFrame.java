package com.client;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;



/**
 * @author JonathanCauchi
 * 
 * This is the User Interface which allows user to Park and Pay for the parking slot
 */
public class ParkingSystemFrame extends JFrame {

	private JPanel contentPane;
	JButton btnPark;
	
	private static ParkingSystemApp app = new ParkingSystemApp();

	static ParkingSystemFrame mainFrame;
	
	private static Logger logger = Logger.getLogger(ParkingSystemApp.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFrame = new ParkingSystemFrame();
					mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ParkingSystemFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Welcome to the Automated Parking System!");
		setBounds(100, 100, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(2, 2));
		setContentPane(contentPane);
		
		btnPark = new JButton("Park");
		btnPark.setBackground(new Color(240, 240, 240));
		btnPark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{				
				Ticket ticket = app.park();
				
				if (ticket == null)
				{
					JOptionPane.showMessageDialog(btnPark, "Parking full!");
				}
				else
				{
					int ticketNumber = ticket.getTicketNumber();
					long time = ticket.getStartTime();
					
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date(time);
					dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
										
					JOptionPane.showMessageDialog(btnPark, "Today's Date: " + dateFormat.format(date) + "\n" +
														   "Your parking ticket number: " + ticketNumber + "\n" +
														   "Your parking position: " + ticket.getSlotPosition() + "\n" +
														   "Start Time: " + timeFormat.format(date));
					logger.info("--------------");
					logger.info("<--Ticket information-->");
					logger.info("Today's date :"+dateFormat.format(date));
				logger.info("Parking ticket number :"+ticketNumber);	
				logger.info("Parking Position :"+ticket.getSlotNumber());
				logger.info("Start time :"+timeFormat.format(date));	
				logger.info("--------------");
				}
			}
		});
		contentPane.add(btnPark, BorderLayout.CENTER);
		
		JButton btnPayExit = new JButton("Pay & Exit");
		btnPayExit.setForeground(new Color(0, 0, 0));
		btnPayExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				app.captureEndTime(); 
				
				SlotFrame slotFrame = null;
				
				try {
					slotFrame = new SlotFrame(app); // display new frame for entering ticket number
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
				
				mainFrame.setVisible(false); // hide the first frame
				slotFrame.setVisible(true);
			}
		});
		contentPane.add(btnPayExit, BorderLayout.EAST);
		
		JButton btnRateSchedue = new JButton("Rate Schedule");
		btnRateSchedue.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				List<RateSchedule> ratesWithSchedule = Schedule.getRatesWithSchedule();
				StringBuilder builder = new StringBuilder();
				
				for (RateSchedule rateSchedule : ratesWithSchedule) {
					builder.append("Schedule :" + rateSchedule.getSchedule()+ " minutes"+"\n" );
					builder.append("Price : "+rateSchedule.getPrice()+"\n" );
					builder.append(" ---------------- "+"\n");
					
				}
				JOptionPane.showMessageDialog(null, builder.toString());
			
				
			}
		});
		
		
		
		
		contentPane.add(btnRateSchedue, BorderLayout.EAST);
		
		
		
	}
	

}
