package edu.gatech.seclass.prj2;

import java.util.ArrayList;
import java.util.Calendar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomerHandler extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "StallCustomers.db";
    private static final int DATABASE_VERSION = 1;
    
    /* Definition of the customer table */
    public abstract static class CustomerTable{
    	
	    public static final String TABLE_NAME = "CustomerList";
	    public static final String ID_FIELD = "IdNumber";
	    public static final String FIRST_FIELD = "FirstName";
	    public static final String LAST_FIELD = "LastName";
	    public static final String ZIP_FIELD = "ZipCode";
	    public static final String EMAIL_FIELD = "EmailAddress";
	    public static final String REWARD_FIELD = "RewardBalance";
	    public static final String SPEND_FIELD = "YTDSpending";
	    public static final String GOLD_FIELD = "GoldStatus";
	    
	    /* Query run to create this table */
	    public static void createTable(SQLiteDatabase db){
	    	 db.execSQL("create table " + TABLE_NAME + 
	    			    " (" + ID_FIELD + " integer primary key autoincrement, " +
	    			           FIRST_FIELD + " text, " +
	    			           LAST_FIELD + " text, " +
	    			           ZIP_FIELD + " text, " +
	    			           EMAIL_FIELD + " text, " +
	    			           REWARD_FIELD + " real, " +
	    			           SPEND_FIELD + " real, " +
	    			           GOLD_FIELD + " integer);");
	    }
    }
    
    /* Definition of the purchase table.  These tables are linked by
     * customer id
     */
    public abstract static class PurchaseTable{
    	
    	public static final String TABLE_NAME = "Purchases";
    	public static final String CUSTOMER_ID_FIELD = "IdNumber";
    	public static final String TOTAL_FIELD = "PurchaseTotal";
    	public static final String GOLD_APPLIED_FIELD = "GoldDiscountApplied";
    	public static final String REWARD_APPLIED_FIELD = "RewardsApplied";
    	public static final String ADJUSTED_TOTAL_FIELD = "AdjustedTotal";
    	public static final String DATETIME_FIELD = "PurchaseDate";
    	
    	
    	/* Query run to create this table */
    	public static void createTable(SQLiteDatabase db){
    		db.execSQL("create table [" + TABLE_NAME + "]" +  
	    			   " (" + CUSTOMER_ID_FIELD + " integer, " +
	    			          TOTAL_FIELD + " real, " +
	    			          GOLD_APPLIED_FIELD + " real, " +
	    			          REWARD_APPLIED_FIELD + " real, " +
	    			          ADJUSTED_TOTAL_FIELD + " real, " +
	    			          DATETIME_FIELD + " text);");
    	}
    }

    /* Connect or create the database */
	public CustomerHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    
	/* If the database was just created, run the queries to create
	 * the tables
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		CustomerTable.createTable(db);
		PurchaseTable.createTable(db);
	}
	
	/* Not implemented */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This code would get all of the information from the database and 
		// update to the new version
	}
	
	/* Save a new customer to the customer table */
	public long saveCustomer(Customer customer) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		/* Create the query to input the data */
		ContentValues values = new ContentValues();
		values.put(CustomerTable.FIRST_FIELD, customer.getFirstName().toUpperCase());
		values.put(CustomerTable.LAST_FIELD, customer.getLastName().toUpperCase());
		values.put(CustomerTable.ZIP_FIELD, customer.getZipCode().toUpperCase());
		values.put(CustomerTable.EMAIL_FIELD, customer.getEmailAddress().toUpperCase());
		values.put(CustomerTable.REWARD_FIELD, customer.getRewardsBalance());
		values.put(CustomerTable.SPEND_FIELD, customer.getYtdSpending());
		int gold = customer.isGoldStatus() ? 1 : 0;
		values.put(CustomerTable.GOLD_FIELD, gold);
		

		/* Insert the data into the customer table */
		long insertId = db.insert(CustomerTable.TABLE_NAME, "null", values);
		db.close();
		return insertId;
	  }

	/* Retrieve the list of customers, and sort by Last Name > First Name > Zip Code > Email */
	public ArrayList<Customer> getCustomerList() {
		ArrayList<Customer> customerList = new ArrayList<Customer>();
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("select " + 
	    		                       CustomerTable.FIRST_FIELD + ", " + 
	    		                       CustomerTable.LAST_FIELD + ", " +
	    		                       CustomerTable.ZIP_FIELD + ", " + 
	    		                       CustomerTable.EMAIL_FIELD + 
	    		                    " from [" + CustomerTable.TABLE_NAME + "]" +  
	    		                    " order by " + CustomerTable.LAST_FIELD + ", " +
	    		                                   CustomerTable.FIRST_FIELD + ", " + 
	    		                                   CustomerTable.ZIP_FIELD + ", " + 
	    		                                   CustomerTable.EMAIL_FIELD + ";", null);
	    
	    if (cursor != null) {
	    	while (cursor.moveToNext()) {
	    		Customer customer = new Customer(cursor.getString(0), 
	    										 cursor.getString(1), 
	    										 cursor.getString(2), 
	    										 cursor.getString(3));
	    		customerList.add(customer);
	    	}
	    }
	    db.close();
	    return customerList;
	}
	
	/* Retrieve the customers id as automatically assigned by the database */
	public int getCustomerId(Customer customer){
		int id = -1;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + CustomerTable.ID_FIELD + 
				                    " FROM [" + CustomerTable.TABLE_NAME + "]" +
				                    " WHERE " + CustomerTable.FIRST_FIELD + "='" + customer.getFirstName().toUpperCase() + "' AND " +
				                                CustomerTable.LAST_FIELD + "='" + customer.getLastName().toUpperCase() + "' AND " +
				                                CustomerTable.ZIP_FIELD + "='" + customer.getZipCode().toUpperCase() + "' AND " +
				                                CustomerTable.EMAIL_FIELD + "='" + customer.getEmailAddress().toUpperCase() + "';",
				                    null);
		
		if(cursor.getCount() != 0){
			cursor.moveToNext();
			id = cursor.getInt(0);
		}
		db.close();
		return id;
	}
	
	/* Check if a customer has already existed in the database */
	public boolean customerExists(Customer customer){
		int id = getCustomerId(customer);
		return (id >= 0);
	}
	
//	Retrieve a customer by id
	public Customer getCustomer(int id) {
		Customer customer = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM [" + CustomerTable.TABLE_NAME + "]" +
                					" WHERE " + CustomerTable.ID_FIELD + "=" + id + " ;", null);
		if(cursor.getCount() != 0){
			cursor.moveToNext();
			customer = new Customer(cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
			double rewardsBalance = cursor.getDouble(5);
			double ytdSpending = cursor.getDouble(6);
			boolean goldStatus = (cursor.getInt(7) == 1) ? true : false;
			customer.setRewardsBalance(rewardsBalance);
			customer.setYtdSpending(ytdSpending);
			customer.setGoldStatus(goldStatus);
		}
		db.close();
		return customer;
	}
	
//	Delete a customer by its ID
	public void deleteCustomer(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(CustomerTable.TABLE_NAME, CustomerTable.ID_FIELD + " = " + id, null);
		db.delete(PurchaseTable.TABLE_NAME, PurchaseTable.CUSTOMER_ID_FIELD + " = " + id, null);
		db.close();
	}
	
//	Update a customer's information
	public void updateCustomer(ContentValues cv, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(CustomerTable.TABLE_NAME, cv, CustomerTable.ID_FIELD + " = " + id, null);
		db.close();
	}
	
//	Save purchase to database
	public long savePurchase(int customerId, Purchase purchase) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		     
		/* Create the query to input the data */
		ContentValues values = new ContentValues();
		values.put(PurchaseTable.CUSTOMER_ID_FIELD, customerId);
		values.put(PurchaseTable.TOTAL_FIELD, purchase.getPurchaseTotal());
		values.put(PurchaseTable.GOLD_APPLIED_FIELD, purchase.getGoldDiscountAmount());
		values.put(PurchaseTable.REWARD_APPLIED_FIELD, purchase.getRewardsAmount());
		values.put(PurchaseTable.ADJUSTED_TOTAL_FIELD, purchase.getAdjustedTotal());
		values.put(PurchaseTable.DATETIME_FIELD, purchase.getPurchaseDate());
		
		/* Insert the data into the customer table */
		long insertId = db.insert(PurchaseTable.TABLE_NAME, "null", values);
		db.close();
		return insertId;
	}
	
//	Retrieve the list of purchases for a customer, and sort by date
	public ArrayList<Purchase> getPurchaseList(int id) {
		ArrayList<Purchase> purchaseList = new ArrayList<Purchase>();
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("select * from [" + PurchaseTable.TABLE_NAME + "]" +  
	    							" where " + PurchaseTable.CUSTOMER_ID_FIELD + " = " + id +
	    		                    " order by " + PurchaseTable.DATETIME_FIELD + ";", null);
	    
	    if (cursor != null) {
	    	while (cursor.moveToNext()) {
	    		Purchase purchase = new Purchase(cursor.getInt(0), 
	    										 cursor.getDouble(1), 
	    										 cursor.getDouble(2), 
	    										 cursor.getDouble(3),
	    										 cursor.getDouble(4),
	    										 cursor.getString(5));
	    		purchaseList.add(purchase);
	    	}
	    }
	    db.close();
	    
	    return purchaseList;
	}
	

//	Calculate the YTD spending in current calendar year
	
	public double getYTDSpending(int id) {
		double ytdSpending = 0.0 ;
		String currentYear = ""+Calendar.getInstance().get(Calendar.YEAR);
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("select sum (" + PurchaseTable.ADJUSTED_TOTAL_FIELD + ") from " + PurchaseTable.TABLE_NAME +
				                    " where " + PurchaseTable.DATETIME_FIELD + " like '%" + currentYear + "%' and " +
				                    PurchaseTable.CUSTOMER_ID_FIELD + " = " + id, null);
		if (cursor != null) {
			cursor.moveToFirst() ;
			ytdSpending = cursor.getDouble(0);
		}
		db.close();
		return ytdSpending;
	}
	
	
	
	
}
