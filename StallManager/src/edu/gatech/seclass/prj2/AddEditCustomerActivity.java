package edu.gatech.seclass.prj2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddEditCustomerActivity extends Activity implements DialogInterface.OnClickListener {
	
	private Bundle extras;
	private Customer customer;
	private CustomerHandler customerHandler;
	private static final String EPATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@"+
	                                       "((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_customer);
		
		extras = getIntent().getExtras();
		
		Button addUpdate = (Button)findViewById(R.id.addUpdateBtn);
		TextView addEditView= (TextView)findViewById(R.id.addEditCustomer);
		
		// Initiate the "Add Customer" page
		if (extras.getString("page").matches("add")) {
			addEditView.setText("Add Customer");
			addUpdate.setText("Add");			
		} 
		// Initiate the "Edit Customer" page
		if (extras.getString("page").matches("edit")) {
			addEditView.setText("Edit Customer");
			addUpdate.setText("Update");
			((EditText)findViewById(R.id.displayFirstName)).setText(extras.getString("firstName").toString());
			((EditText)findViewById(R.id.displayLastName)).setText(extras.getString("lastName").toString());
			((EditText)findViewById(R.id.displayZipCode)).setText(extras.getString("zipCode").toString());
			((EditText)findViewById(R.id.displayEmailAddress)).setText(extras.getString("emailAddress").toString());
		}
	}
	
	// Set up "Add/Edit" button
	public void addUpdateClick(View v) {
		
		String firstName = ((EditText)findViewById(R.id.displayFirstName)).getText().toString().trim();
		String lastName = ((EditText)findViewById(R.id.displayLastName)).getText().toString().trim(); 
		String zipCode = ((EditText)findViewById(R.id.displayZipCode)).getText().toString().trim();
	    String emailAdd = ((EditText)findViewById(R.id.displayEmailAddress)).getText().toString().trim();
	    
	    customerHandler = new CustomerHandler(getBaseContext());	
	    // Add
		if (extras.getString("page").matches("add")) {
			
		    if (firstName.length() > 0 && lastName.length() > 0  &&
				zipCode.length() > 0 && emailAdd.length() > 0) {
			
		    	if (!isValidEmailAddress(emailAdd)) {
		    		invalidEmailAlertDialog();
		    	}
		    	else {
			    	customer = new Customer(firstName, lastName, zipCode, emailAdd);
			    	customer.setRewardsBalance(0);
			    	customer.setYtdSpending(0);
			    	customer.setGoldStatus(false);		    	
			    	
					if (customerHandler.customerExists(customer)) {
						customerExistsAlertDialog();	
					} else { // Add new customer to database and jump to "View Customer" page
						customerHandler.saveCustomer(customer);
						Intent intent = new Intent();
						intent.setClass(AddEditCustomerActivity.this, ViewCustomerActivity.class);    
						startActivity(intent);     
						AddEditCustomerActivity.this.finish(); 
					}
		    	}
		    } else
		    	inputAllFieldsAlertDialog();
		} 
		// Edit
		if (extras.getString("page").matches("edit")) {
			
			if (firstName.length() > 0 && lastName.length() > 0 &&
				zipCode.length() > 0 && emailAdd.length() >0 ) {
				
				if (!isValidEmailAddress(emailAdd)) {
					invalidEmailAlertDialog();
			    }
			    else {
					int id = extras.getInt("customerId");
					ContentValues cv = new ContentValues();
					cv.put(CustomerHandler.CustomerTable.FIRST_FIELD, firstName.toUpperCase());
					cv.put(CustomerHandler.CustomerTable.LAST_FIELD, lastName.toUpperCase());
					cv.put(CustomerHandler.CustomerTable.ZIP_FIELD, zipCode.toUpperCase());
					cv.put(CustomerHandler.CustomerTable.EMAIL_FIELD, emailAdd.toUpperCase());
					// Update the customer and jump back to "View Customer" page
					customerHandler.updateCustomer(cv, id);	
					Intent intent = new Intent();
					intent.setClass(AddEditCustomerActivity.this, ViewCustomerActivity.class);    
					startActivity(intent);     
					AddEditCustomerActivity.this.finish(); 
			    }
			} else 
				inputAllFieldsAlertDialog();
		}
	
	}
	
	// Clear input button
	public void clearInput(View v) {
		((EditText)findViewById(R.id.displayFirstName)).setText("");
		((EditText)findViewById(R.id.displayLastName)).setText("");
		((EditText)findViewById(R.id.displayZipCode)).setText("");
		((EditText)findViewById(R.id.displayEmailAddress)).setText("");
	}
	
	// Back button
	public void backClick(View v) {
		// From "Add Customer" back to main page
		if (extras.getString("page").matches("add")) {
			Intent intent = new Intent();
			intent.setClass(AddEditCustomerActivity.this, StallManagerActivity.class);    
			startActivity(intent);     
			AddEditCustomerActivity.this.finish(); 
		}
		// From "Edit Customer" back to "View Customer" page
		if (extras.getString("page").matches("edit")) {
			Intent intent = new Intent();
			intent.setClass(AddEditCustomerActivity.this, ViewCustomerActivity.class);    
			startActivity(intent);     
			AddEditCustomerActivity.this.finish(); 
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_edit_customer, menu);
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

//	Invalid Email Alert Dialog
	public void invalidEmailAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Please Input A Valid Email Address!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
//	Empty Field Alert Dialog
	public void inputAllFieldsAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Please Input All Fields!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
//	Customer Exists Alert Dialog
	public void customerExistsAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Customer Already Exists!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	}
	
//	Check if valid email address
	public boolean isValidEmailAddress(String email) {
         String ePattern = EPATTERN;
         java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
         java.util.regex.Matcher m = p.matcher(email);
         return m.matches();
	}
	
}
