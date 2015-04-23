package edu.gatech.seclass.prj2.tst;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.test.AndroidTestCase;
import android.util.Log;
import edu.gatech.seclass.prj2.Customer;
import edu.gatech.seclass.prj2.CustomerHandler;
import edu.gatech.seclass.prj2.Purchase;

@SuppressLint("SimpleDateFormat")
public class CustomerHandlerTest extends AndroidTestCase {
	private static final String TAG = "CustomerHandlerTest";
	private CustomerHandler handler;
	private Customer customer;
	private Purchase purchase;

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		handler = new CustomerHandler(getContext());
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		handler = null;
	}

	@Test
	public void testCustomerHandler() {
		handler = new CustomerHandler(getContext());
		handler.getWritableDatabase();
		assertNotNull(handler);
	}

	@Test
	public void testSaveCustomer() {
		for (int i = 1; i < 10; i++) {
			customer = new Customer("Tom" + i, "Liu" + i, "8807" + i, "host"
					+ i + "@gmail.com");
			handler.saveCustomer(customer);
		}
	}

	@Test
	public void testCustomerExists1() {
		customer = new Customer("Tom1", "Liu1", "88071", "host1@gmail.com");
		assertTrue(handler.customerExists(customer));
	}

	@Test
	public void testCustomerExists2() {
		customer = new Customer("San", "Zhang", "33044", "san@gmail.com");
		assertFalse(handler.customerExists(customer));
	}

	@Test
	public void testGetCustomer() {
		customer = handler.getCustomer(1);
		// Customer information can be found in LogCat
		Log.i(TAG, customer.toString());
	}

	@Test
	public void testGetCustomerList() {
		ArrayList<Customer> customers = handler.getCustomerList();
		for (Customer customer : customers) {
			// Customer information can be found in LogCat
			Log.i(TAG, customer.toString());
		}
	}

	@Test
	public void testGetCustomerId() {
		// customer = handler.getCustomer(128);
		customer = new Customer("Tom2", "Liu2", "88072", "host2@gmail.com");
		int id = handler.getCustomerId(customer);
		assertEquals(2, id);
	}

	@Test
	public void testUpdateCustomer() {
		ContentValues cv = new ContentValues();
		cv.put(CustomerHandler.CustomerTable.FIRST_FIELD, "ERIC");
		cv.put(CustomerHandler.CustomerTable.LAST_FIELD, "LIU");
		cv.put(CustomerHandler.CustomerTable.ZIP_FIELD, "77066");
		cv.put(CustomerHandler.CustomerTable.EMAIL_FIELD, "email@gmail.com");
		handler.updateCustomer(cv, 1);
		// Can be verified by method testGetCustomer()
	}

	@Test
	public void testDeleteCustomer() {
		handler.deleteCustomer(8);
		assertNull(handler.getCustomer(8));
	}

	@Test
	public void testSavePurchase() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		purchase = new Purchase(1, 20, 0, 0, 20, dateFormat.format(new Date(0)));
		handler.savePurchase(1, purchase);
	}

	@Test
	public void testGetPurchaseList() {
		ArrayList<Purchase> purchases = handler.getPurchaseList(1);
		for (Purchase purchase : purchases) {
			// Purchase information can be found in LogCat
			Log.i(TAG, purchase.toString());
		}
	}

	@Test
	public void testGetYTDSpending() {
		customer = handler.getCustomer(1);
		double actual = handler.getYTDSpending(1);
		double expected = customer.getYtdSpending();
		assertEquals(expected, actual);
	}
}
