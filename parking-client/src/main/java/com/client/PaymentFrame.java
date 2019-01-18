package com.client;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

/**
 * @author JonathanCauchi	
 *
 *         This class is the User Interface for entering credit card details
 */
public class PaymentFrame extends JFrame {
	
	
	private static final Logger logger = Logger.getLogger(PaymentFrame.class);

	private JPanel contentPane;

	private JLabel cardtype;
	private JLabel ccLabel;
	private JLabel cvvLabel;
	private JLabel expiryLabel;

	private JTextField cardtypeField;
	private JTextField ccTextField;
	private JTextField cvvTextField;
	private JTextField expiryTextField;

	private JButton payButton;

	private Ticket ticket;
	// private String durationParked;

	String cardtypeText = "";
	String ccNumber = "";
	String cvvNumber = "";
	String expiry = "";

	ParkingSystemApp app;

	PaymentFrame paymentFrame = this;

	double totalFee = 0;

	/**
	 * Create the frame.
	 */
	public PaymentFrame(ParkingSystemApp paymentApp, Ticket ticket) {
		this.app = paymentApp;
		this.ticket = ticket;
		initComponents();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Pay & Exit");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(8, 2));

		contentPane.add(cardtype);
		contentPane.add(cardtypeField);

		contentPane.add(ccLabel);
		contentPane.add(ccTextField);

		contentPane.add(cvvLabel);
		contentPane.add(cvvTextField);

		contentPane.add(expiryLabel);
		contentPane.add(expiryTextField);

		contentPane.add(payButton);

		setContentPane(contentPane);
	}

	private void initComponents() {

		cardtype = new JLabel("Credit card type(only visa or mastercard):");
		cardtypeField = new JTextField();
		cardtypeField.setEditable(false);

		ccLabel = new JLabel("Credit Card Number: ");
		ccTextField = new JTextField();

		ccTextField.addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				String ccNumber = ccTextField.getText().toString();
				String result = "Invalid card number";
				if (ccNumber.length() == 16) {

					result = app.validateCreditCard(ccNumber);
				}
				cardtypeField.setText(result);
			}

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		cvvLabel = new JLabel("CVV Number: ");
		cvvTextField = new JTextField();

		expiryLabel = new JLabel("Expiry: ");
		expiryTextField = new JTextField();

		payButton = new JButton("Pay");
		payButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardtypeText = cardtypeField.getText();
				ccNumber = ccTextField.getText();
				cvvNumber = ccTextField.getText();
				expiry = expiryTextField.getText();

				app.setPaymentInformation(cardtypeText, ccNumber, cvvNumber, expiry);

				if (cardtypeText.equals("Visa") || cardtypeText.equals("MasterCard")) {
					double totalFee = app.getTotalFee();
					boolean result = app.payTicket(ticket.getTicketNumber(), ticket.getEndTime(), totalFee);
					if (result) {

						JOptionPane.showMessageDialog(payButton,
								"Thank You for the payment of: $" + totalFee + "\n" + "Visit again! ");
						logger.info("--------------");
						logger.info("<--Payment information-->");
					logger.info("Ticket # :"+ticket.getTicketNumber());	
					logger.info("Amount paid :"+totalFee);
					logger.info("--------------");
					paymentFrame.dispose();
						ParkingSystemFrame.mainFrame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(payButton,
								"Cannot pay at now, check connection! \nPlease re-enter the details again.");
					}
				} else {
					JOptionPane.showMessageDialog(payButton,
							"Invalid Credit Card! \nPlease re-enter the details again.");
					cardtypeField.setText("");
					ccTextField.setText("");
					ccTextField.requestFocus();
					cvvTextField.setText("");
					expiryTextField.setText("");
				}
			}
		});
	}

}
