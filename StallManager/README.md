### StallManager App

#### Current progress

1. Main page (StallManagerActivity.java)
	- Add Customer: direct the stall manager to Add Customer page (AddEditCustomerActivity.java)
	- View Customer: direct stall manager to View Customer page (ViewCustomerActivity.java)
<br><br>
2. Add Customer page (AddEditCustomerActivity.java)
	- Stall manager input area: First Name, Last Name, Zip Code, and Email; (Email address will be checked if it is a valid email address)
	- Add button (3 actions included): 
		- Creat a Customer object with stall manager input information. Stall manager must input all fields otherwise a message will pop up; 
		- Save the object to a table called "customerList" in the database "stallmanager.db";
		- Direct to View Customer page (ViewCustomerActivity.java)
	- Clear button: clear stall manager input 
	- Back button: back to main page 
<br><br>
3. View Customer page (ViewCustomerActivity.java)
	- ListView: 
		- show the list of customers, customers are listed by ascending order of their last name, first name, zip code and email;
		- when clicking one customer, the selected item will be highlighted in blue color; 
		- after clicking one customer, then clicking one of the six buttons below would implement the corresponding action. 
	*(Before clicking any button below except for "Back", a customer has to be selected first, otherwise a dialog window will pop up)*
	- Add Purchase Button: direct to Add Purchase page; (AddPurchaseActivity.java)
	- View Purchase Button: direct to View Purchase page; (ViewPurchaseActivity.java)
	- View Rewards Button: direct to View Rewards page; (ViewRewardsActivity.java)
	- Edit Button: direct to the Edit Customer page; (AddEditCustomerActivity.java)
	- Delete Button: a dialog will pop up to confirm with the stall manager if he/she really wants to delete the selected customer. If "No" then return to the view customer page, if "YES" then delete the selected customer from both database and list view;
	- Back Button: back to Main page.
<br><br>
4. Edit Customer page (AddEditCustomerActivity.java)
	- Stall manager input area: will display the information of the customer that stall manager selected from View Customer page.
	- Update Button:
		- after revising cutomer's information, clicking this button will update the customer's information in the database; (Email will be checked if it is valid)
		- will direct to View Customer page;
		- customer list in View Customer page will be updated.
	- Clear button: clear input area
	- Back Button: back to View Customer page 
<br><br>
5. View Purchase page (ViewPurchaseActivity.java) and View Rewards page (ViewRewardsActivity.java) 
	- Back Button: direct back to View Customer page
	- Show a customer's all purchases details
<br><br>
6. Add Purchase page (AddPurchaseActivity.java)
	- Stall manager input the total purchase amount
	- "+" button: after clicking, the screen will show total purchase amount, gold discount amount, reward amount and adjusted total amount.
	- "-" button: will clear screen
	- "Check Out" button will trigger the following actions:
		- implement the CreditCardService first to get card info, a dialog window will pop up if card reading fails;
		- implement the PaymentService with card info and adjusted total amount, a dialog window will pop up if payment fails;
		- save purchase to database;
		- update customer's rewards balance and year to date spending and gold status;
		- send email, a dialog window will pop up to show if sending email succeeds;
		- clear input.
<br><br>
7. Customer.java ---- Customer class
8. Purchase.java ---- Customer class
9. CustomerHandler.java 
	- a class that help handle database;
	- add customer object to database and retrieve customer objects from database;	
	- delete customer object from database and update customer information in database
	- add purchase object to database