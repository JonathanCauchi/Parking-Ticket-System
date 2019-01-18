package com.client;


/**
 * @author JonathanCauchi This class simulates a Bank which would validate the credit
 *         card Here we are making an assumption that the credit card validation
 *         is always successful.
 */
public class Bank {
	String creditCardNumber;
	String cvvNumber;
	String expiryDate;
	String cardtype;

	public Bank() {
		super();
	}

	public Bank(PaymentInformation paymentInformation) {
		this.creditCardNumber = paymentInformation.getCreditCardNumber();
		this.cvvNumber = paymentInformation.getCvvNumber();
		this.expiryDate = paymentInformation.getExpiryDate();
		this.cardtype = paymentInformation.getCardType();
	}

	// simulating credit card validation done by back.
	// Assuming the credit card validation is successful always.
	// LUNN algorithm must be implemented here
	public static String validateCreditCard(String ccNumber) {
		String url = RestHelper.MAIN_DOMAIN + "/" + ccNumber;
		try {
			String result = RestHelper.callRest(url, "application/json").trim();
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Network error!";
		}

	}
}
