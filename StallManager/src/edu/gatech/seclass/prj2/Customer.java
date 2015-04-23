package edu.gatech.seclass.prj2;

public class Customer {
	
	private String firstName;
	private String lastName;
	private String zipCode;
	private String emailAddress;
	private double rewardsBalance;
	private double ytdSpending;
	private boolean goldStatus;


	public Customer(String firstName, String lastName, String zipCode, String emailAddress) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.zipCode = zipCode;
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public double getRewardsBalance() {
		return rewardsBalance;
	}

	public void setRewardsBalance(double rewardsBalance) {
		this.rewardsBalance = rewardsBalance;
	}

	public double getYtdSpending() {
		return ytdSpending;
	}

	public void setYtdSpending(double ytdSpending) {
		this.ytdSpending = ytdSpending;
	}

	public boolean isGoldStatus() {
		return goldStatus;
	}

	public void setGoldStatus(boolean goldStatus) {
		this.goldStatus = goldStatus;
	}

	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", lastName=" + lastName
				+ ", zipCode=" + zipCode + ", emailAddress=" + emailAddress
				+ ", rewardsBalance=" + rewardsBalance + ", ytdSpending="
				+ ytdSpending + ", goldStatus=" + goldStatus + "]";
	}

}
