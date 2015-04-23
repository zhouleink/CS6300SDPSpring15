package edu.gatech.seclass.prj2;

public class Purchase {
	
	private int customerId;
	private double purchaseTotal;
	private double goldDiscountAmount;
	private double rewardsAmount;
	private double adjustedTotal;
	private String purchaseDate;
	
	public Purchase (int customerId, double purchaseTotal, double goldDiscountAmount,
					 double rewardsAmount, double adjustedTotal, String purchaseDate) {
		this.customerId = customerId;
		this.purchaseTotal = purchaseTotal;
		this.goldDiscountAmount = goldDiscountAmount;
		this.rewardsAmount = rewardsAmount;
		this.adjustedTotal = adjustedTotal;
		this.purchaseDate = purchaseDate;
	}
		
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public double getPurchaseTotal() {
		return purchaseTotal;
	}
	public void setPurchaseTotal(double purchaseTotal) {
		this.purchaseTotal = purchaseTotal;
	}
	public double getGoldDiscountAmount() {
		return goldDiscountAmount;
	}
	public void setGoldDiscountAmount(double goldDiscountAmount) {
		this.goldDiscountAmount = goldDiscountAmount;
	}
	public double getRewardsAmount() {
		return rewardsAmount;
	}
	public void setRewardsAmount(double rewardsAmount) {
		this.rewardsAmount = rewardsAmount;
	}
	public double getAdjustedTotal() {
		return adjustedTotal;
	}
	public void setAdjustedTotal(double adjustedTotal) {
		this.adjustedTotal = adjustedTotal;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	
	
}
