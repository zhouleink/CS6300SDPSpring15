package edu.gatech.seclass.prj2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class StallManagerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

//	Start Add Customer page 
	public void addCustomerClick(View v) {
		Intent intent = new Intent();
		intent.setClass(StallManagerActivity.this, AddEditCustomerActivity.class);    
		intent.putExtra("page", "add"); 
		startActivity(intent);     
		StallManagerActivity.this.finish(); 
	}

//	Start View Customer page
	public void viewCustomerClick(View v) {
		Intent intent = new Intent();
		intent.setClass(StallManagerActivity.this, ViewCustomerActivity.class);    
		startActivity(intent);     
		StallManagerActivity.this.finish(); 
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