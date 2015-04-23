package edu.gatech.seclass.prj2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewCustomerActivity extends Activity implements DialogInterface.OnClickListener {

	private CustomerHandler customerHandler;
	private ListView mListView = null;  
    private int itemPosition = -1;
    private ArrayList<Map<String,Object>> mData = new ArrayList<Map<String,Object>>();
    private ArrayList<Customer> customerList;
    private SimpleAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_customer);

		// Create a list view to show all customers in the database 
		mListView = (ListView) findViewById(R.id.listCustomerView);  
        customerHandler = new CustomerHandler(getBaseContext());
		customerList = customerHandler.getCustomerList();
		
		if (customerList != null) {			
			for (int i = 0; i < customerList.size(); i++) {				
				Map<String,Object> item = new HashMap<String,Object>();  
		        item.put("title", customerList.get(i).getLastName() + ", " + customerList.get(i).getFirstName());  
		        item.put("text", customerList.get(i).getEmailAddress() + ",      ZIP: " + customerList.get(i).getZipCode());  
		        mData.add(item);
			}
		}
  
	    adapter = new SimpleAdapter(this, mData, android.R.layout.simple_list_item_2,  
	                                new String[]{"title","text"}, 
	                                new int[]{android.R.id.text1, android.R.id.text2});  
	    mListView.setAdapter(adapter);  
	    
	    // Set up OnItemClick function
	    mListView.setOnItemClickListener(new OnItemClickListener() {  
	        @Override  
	        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) { 
	        	customerList = customerHandler.getCustomerList();
		        itemPosition = position;		    }  
	    }); 
	}

//  Add customer's transactions
	public void addPurchaseClick(View v) {
		if (mData.isEmpty()) {
			emptyDataBaseAlertDialog();
		} else if (itemPosition >= 0) {
			Intent intent = new Intent();
			intent.setClass(ViewCustomerActivity.this, AddPurchaseActivity.class);  
			intent.putExtra("customerId", customerHandler.getCustomerId(customerList.get(itemPosition)));
			intent.putExtra("firstName", customerList.get(itemPosition).getFirstName());
			intent.putExtra("lastName", customerList.get(itemPosition).getLastName());			
			startActivity(intent);     
			ViewCustomerActivity.this.finish(); 
		} else {
			selectCustomerAlertDialog();
		}
	}
	
//  View customer's transactions
	public void viewPurchaseClick(View v) {
		if (mData.isEmpty()) {
			emptyDataBaseAlertDialog();
		} else if (itemPosition >= 0) {
			Intent intent = new Intent();
			intent.setClass(ViewCustomerActivity.this, ViewPurchaseActivity.class);  
			intent.putExtra("customerId", customerHandler.getCustomerId(customerList.get(itemPosition)));
			intent.putExtra("firstName", customerList.get(itemPosition).getFirstName());
			intent.putExtra("lastName", customerList.get(itemPosition).getLastName());	
			startActivity(intent);     
			ViewCustomerActivity.this.finish();
		} else
			selectCustomerAlertDialog();
	}
			
//  View customer's rewards information
	public void viewRewardsClick(View v) {
		if (mData.isEmpty()) {
			emptyDataBaseAlertDialog();
		} else if (itemPosition >= 0) {
			Intent intent = new Intent();
			intent.setClass(ViewCustomerActivity.this, ViewRewardsActivity.class);    
			intent.putExtra("customerId", customerHandler.getCustomerId(customerList.get(itemPosition)));
			intent.putExtra("firstName", customerList.get(itemPosition).getFirstName());
			intent.putExtra("lastName", customerList.get(itemPosition).getLastName());	
			startActivity(intent);     
			ViewCustomerActivity.this.finish(); 
		} else
			selectCustomerAlertDialog();
	}
	
//	Edit customer
	public void editClick(View v) {
		if (mData.isEmpty()) {
			emptyDataBaseAlertDialog();
		} else if (itemPosition >= 0) {
			Intent intent = new Intent();
			intent.setClass(ViewCustomerActivity.this, AddEditCustomerActivity.class);
			intent.putExtra("page", "edit");
			intent.putExtra("firstName", customerList.get(itemPosition).getFirstName());
			intent.putExtra("lastName", customerList.get(itemPosition).getLastName());
			intent.putExtra("zipCode", customerList.get(itemPosition).getZipCode());
			intent.putExtra("emailAddress", customerList.get(itemPosition).getEmailAddress());
			intent.putExtra("customerId", customerHandler.getCustomerId(customerList.get(itemPosition)));
			startActivity(intent);     
			ViewCustomerActivity.this.finish();
		} else
			selectCustomerAlertDialog();
	}
		
//	Delete customer
	public void deleteClick(View v) {	
		if (mData.isEmpty()) {
			emptyDataBaseAlertDialog();
		} else if(itemPosition >= 0) {		
			deleteCustomerAlertDialog();
		} else
			selectCustomerAlertDialog();
	}
	
	
//	Back to main page
	public void backClick(View v) {
		Intent intent = new Intent();
		intent.setClass(ViewCustomerActivity.this, StallManagerActivity.class);    
		startActivity(intent);     
		ViewCustomerActivity.this.finish(); 
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_customer, menu);
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

//	Pop up a message to remind the user of selecting a customer
	public void selectCustomerAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Please First Select A Customer!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
//	Pop up a message to indicate that the database is empty
	public void emptyDataBaseAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("No Customer In Database!")
		.setNeutralButton("BACK", this)
		.setCancelable(true)
		.create();
		alert.show();
	}
	
//	Pop up a message to confirm customer deletion
	public void deleteCustomerAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(this)
		.setMessage("Delete This Customer?")
		.setNegativeButton("NO", this)
		.setPositiveButton("YES", this)
		.setCancelable(false)
		.create();
		alert.show();
	}

//	Set up deleting customer actions: Yes/No
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			int id = customerHandler.getCustomerId(customerList.get(itemPosition));
			customerHandler.deleteCustomer(id);
			// Remove customer from list 
			Map<String,Object> item = mData.get(itemPosition);
		    mData.remove(item);
		    adapter.notifyDataSetChanged();
			itemPosition = -1; //after deleting customer, reset the itemPosition to -1
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		default:
			break;
		}
	}
}
