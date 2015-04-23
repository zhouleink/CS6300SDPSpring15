package edu.gatech.seclass.prj2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewRewardsActivity extends Activity {

	private int customerId;
	private CustomerHandler customerHandler;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_rewards);
		
		Bundle extra = getIntent().getExtras();
		customerId = extra.getInt("customerId");
		((TextView)findViewById(R.id.displayName3)).setText(extra.getString("lastName") + ", " + extra.getString("firstName"));
		
		
		TextView displayYTDTotal = (TextView)findViewById(R.id.displayYTDTotal);
		TextView displayToSpend = (TextView)findViewById(R.id.displayToSpend);
		TextView displayRewardsBalance = (TextView)findViewById(R.id.displayRewardsBalance);
		TextView displayGoldStatus = (TextView)findViewById(R.id.displayGoldStatus);
		
		customerHandler = new CustomerHandler(getBaseContext());
		Customer customer = customerHandler.getCustomer(customerId);
		
		displayYTDTotal.setText("$" + String.format("%.2f", customer.getYtdSpending()));
		displayRewardsBalance.setText("$" + String.format("%.2f", customer.getRewardsBalance()));
		
		if (customer.isGoldStatus()) {
			displayToSpend.setText("N/A");
			displayGoldStatus.setText("Yes");
		} else {
			double toSpend = 1000.0 - customer.getYtdSpending();
			displayToSpend.setText("$" + String.format("%.2f", toSpend));
			displayGoldStatus.setText("No");
		}
	
	}

	public void backClick(View v) {
		Intent intent = new Intent();
		intent.setClass(ViewRewardsActivity.this, ViewCustomerActivity.class);    
		startActivity(intent);     
		ViewRewardsActivity.this.finish(); 
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_rewards, menu);
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
