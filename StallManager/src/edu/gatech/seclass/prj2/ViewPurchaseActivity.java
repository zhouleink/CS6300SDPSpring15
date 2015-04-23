package edu.gatech.seclass.prj2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewPurchaseActivity extends Activity {

	private int customerId;	
	private CustomerHandler customerHandler;
	private ListView mListView = null;
    private ArrayList<Map<String,Object>> mData = new ArrayList<Map<String,Object>>();
    private ArrayList<Purchase> purchaseList;
    private SimpleAdapter adapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_purchase);
		
		Bundle extra = getIntent().getExtras();
		customerId = extra.getInt("customerId");
		((TextView)findViewById(R.id.displayName2)).setText(extra.getString("lastName") + ", " + extra.getString("firstName"));
		
		// Create a list view to show all customers in the database 
		mListView = (ListView) findViewById(R.id.listPurchaseView);  
		customerHandler = new CustomerHandler(getBaseContext());
		purchaseList = customerHandler.getPurchaseList(customerId);
				
		if (purchaseList != null) {			
			for (int i = 0; i < purchaseList.size(); i++) {				
				Map<String,Object> item = new HashMap<String,Object>();  
				item.put("title", purchaseList.get(i).getPurchaseDate() + ", Total Purchase: $" + String.format("%.2f", purchaseList.get(i).getPurchaseTotal()));  
				item.put("text", "Gold Discount: $" + String.format("%.2f", purchaseList.get(i).getGoldDiscountAmount()) + 
				   		 ", Rewards Amount: $" + String.format("%.2f", purchaseList.get(i).getRewardsAmount()) + 
				   		 ", Adjusted Total: $" + String.format("%.2f", purchaseList.get(i).getAdjustedTotal()));  
				mData.add(item);
			}
		}
	    adapter = new SimpleAdapter(this, mData, android.R.layout.simple_list_item_2,  
	                                new String[]{"title","text"}, new int[]{android.R.id.text1, android.R.id.text2});  			
	    mListView.setAdapter(adapter);  
	}

	public void backClick(View v) {
		Intent intent = new Intent();
		intent.setClass(ViewPurchaseActivity.this, ViewCustomerActivity.class);    
		startActivity(intent);     
		ViewPurchaseActivity.this.finish(); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_purchase, menu);
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

}
