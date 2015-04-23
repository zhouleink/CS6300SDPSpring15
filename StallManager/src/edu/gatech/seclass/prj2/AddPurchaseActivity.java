package edu.gatech.seclass.prj2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.gatech.seclass.services.*; 

public class AddPurchaseActivity extends Activity implements DialogInterface.OnClickListener {
	
	public final static double REWARD = 10.0;
	private Customer customer;
	private CustomerHandler customerHandler;
	private EditText purchaseTotalEdit;
	private TextView displayPurchaseTotal;
	private TextView displayGoldDiscount;
	private TextView displayRewards;
	private TextView displayAdjustedTotal;

	private double displayPurchaseTotalDouble;			
	private double displayGoldDiscountDouble;		
	private double displayRewardsDouble;
	private double displayAdjustedTotalDouble;
	
	private Purchase purchase;
	private int customerId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_purchase);
		Bundle extra = getIntent().getExtras();
		customerId = extra.getInt("customerId");
		
		customerHandler = new CustomerHandler(getBaseContext());
		
		purchaseTotalEdit = (EditText)findViewById(R.id.purchaseTotalEdit);
	    displayPurchaseTotal = (TextView)findViewById(R.id.displayPurchaseTotal);
	    displayGoldDiscount = (TextView)findViewById(R.id.displayGoldDiscount);
	    displayRewards = (TextView)findViewById(R.id.displayRewards);
	    displayAdjustedTotal = (TextView)findViewById(R.id.displayAdjustedTotal);
	    ((TextView)findViewById(R.id.displayName)).setText(extra.getString("lastName") + ", " + extra.getString("firstName"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_purchase, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//	Plus button --- display transaction details
	public void plusClick(View v) {
		
		if (!purchaseTotalEdit.getText().toString().matches("")) {
			
			// retrieve customer's updated information
			customer = customerHandler.getCustomer(customerId);
			
			// Total purchase amount
			displayPurchaseTotalDouble = Double.parseDouble(purchaseTotalEdit.getText().toString());
			displayPurchaseTotal.setText(String.format("%.2f", displayPurchaseTotalDouble));
			// Gold discount applied
			if (customer.isGoldStatus()) {
				displayGoldDiscountDouble = displayPurchaseTotalDouble * .05;
			} else
				displayGoldDiscountDouble = 0;		
			displayGoldDiscount.setText(String.format("%.2f", displayGoldDiscountDouble));
			// Rewards applied, if any
			if ((displayPurchaseTotalDouble-displayGoldDiscountDouble) >= customer.getRewardsBalance()) {
				displayRewardsDouble = customer.getRewardsBalance();
			} else
				displayRewardsDouble = displayPurchaseTotalDouble - displayGoldDiscountDouble;
			displayRewards.setText(String.format("%.2f", displayRewardsDouble));
			// Adjusted total purchase amount
			displayAdjustedTotalDouble = displayPurchaseTotalDouble - displayGoldDiscountDouble - displayRewardsDouble;
			displayAdjustedTotal.setText(String.format("%.2f", displayAdjustedTotalDouble));
		} else
			inputAlertDialog();
	}
	
//	Minus button --- clear input
	public void minusClick(View v) {
		purchaseTotalEdit.setText("");
		displayPurchaseTotal.setText("");
		displayGoldDiscount.setText("");
		displayRewards.setText("");
		displayAdjustedTotal.setText("");
	}
	
//	Check out --- includes following steps
//	1. Swipe card
//	2. Payment process
//	3. Save purchase
//	4. Update customer
//	5. Send email
//	6. Clear input
	@SuppressLint("SimpleDateFormat")
	public void checkoutClick(View v) {

		if (displayAdjustedTotal.getText().toString().trim().length() > 0) {
			customer = customerHandler.getCustomer(customerId);
			
			// Swipe card
			String cardInfoString = CreditCardService.getCardInfo();
			double rewardsGained;
			
			// If card reads successfully, start processing payment
			if (!cardInfoString.matches("Error")) {
				String[] cardInfo = cardInfoString.split("#");
				
				// If payment succeeds, update customer
				if (PaymentService.processPayment(cardInfo[0], cardInfo[1], cardInfo[2], cardInfo[3], cardInfo[4], displayAdjustedTotalDouble)) {
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					String timeStamp = dateFormat.format(Calendar.getInstance().getTime());
					
					purchase = new Purchase(customerId, displayPurchaseTotalDouble, displayGoldDiscountDouble,
											displayRewardsDouble, displayAdjustedTotalDouble, timeStamp);
					
					customerHandler.savePurchase(customerId, purchase);
					
					// If reward gained, send email
					if ( Double.compare(displayAdjustedTotalDouble, 100.0) >= 0) {
						rewardsGained = REWARD;
						String recipient = customer.getEmailAddress();
						String subject = "Rewards Gained";
						String body = "You have gained a reward of $10 from this transaction!";
						if (EmailService.sendEmail(recipient, subject, body))
							rewardsEmailAlertDialog();
						else
							rewardsEmailFailedAlertDialog();
					} else 
						rewardsGained = 0.0;		
					
					// Update customer's rewards balance and Gold status
					double newRewardsBalance = customer.getRewardsBalance() - displayRewardsDouble + rewardsGained;
					double newYTDSpending = customerHandler.getYTDSpending(customerId);
					ContentValues cv = new ContentValues();
					cv.put(CustomerHandler.CustomerTable.REWARD_FIELD, newRewardsBalance);
					cv.put(CustomerHandler.CustomerTable.SPEND_FIELD, newYTDSpending);
					customerHandler.updateCustomer(cv, customerId);			
					// When customer achieves Gold status, send email notification
					if (!customer.isGoldStatus()) {
						int goldStatus = Double.compare(newYTDSpending, 1000.0) >= 0 ? 1 : 0;
						cv.put(CustomerHandler.CustomerTable.GOLD_FIELD, goldStatus);
						customerHandler.updateCustomer(cv, customerId);			
						
						if (goldStatus == 1) {
							String recipient = customer.getEmailAddress();
							String subject = "Gold Status Achieved";
							String body = "You have achieved Gold status!";
							if (EmailService.sendEmail(recipient, subject, body))
								goldEmailAlertDialog();
							else
								goldEmailFailedAlertDialog();
						}
					}
					
					// Clear input
					purchaseTotalEdit.setText("");
					displayPurchaseTotal.setText("");
					displayGoldDiscount.setText("");
					displayRewards.setText("");
					displayAdjustedTotal.setText("");
					
				} else
					paymentFailureAlertDialog();
			} else
				cardErrorAlertDialog();
		} else
			addPurchaseAlertDialog();
	}
	
	
//	Back to View Customer page
	public void backClick(View v) {
		Intent intent = new Intent();
		intent.setClass(AddPurchaseActivity.this, ViewCustomerActivity.class);    
		startActivity(intent);     
		AddPurchaseActivity.this.finish(); 
	}
	
	public void onStart() {
		super.onStart();
	}
	public void onResume() {
		super.onResume();
	}
	public void onPause() {
		super.onPause();
	}
	public void onStop() {
		super.onStop();
	}
	public void onDestroy() {
		super.onDestroy();
	}
	public void onRestart() {
		super.onRestart();
	}

	public void inputAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Please Input Purchase Amount First!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	public void cardErrorAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Card Error!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	public void paymentFailureAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Payment Failed!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	public void addPurchaseAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Please Add Purchase First!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	public void rewardsEmailAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Customer Earned $10. Email Sent!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	public void rewardsEmailFailedAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Customer Earned $10. Email Sending Failed, Please Check Email!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	public void goldEmailAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Customer Achieves Gold Status. Email Sent!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	public void goldEmailFailedAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Customer Achieves Gold Status. Email Sending Failed, Please Check Email!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
}
