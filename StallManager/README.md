# Stall Manager App
##*Payment and Rewards Management System (PReMS)*

## 1 Introduction

Payment and Rewards Management System (PReMS) is an android based application that will allow for the management and tracking of loyal customer's purchases and rewards.  It is intended to be used by a mobile shop manager.

## 2 Business Needs/Requirements

There currently is not a good mobile point of sale (PoS) solution that will allow for a mobile shop manager process payments, track a customer's purchases, and allow for any type of rewards program.

Mobile shop managers will benefit from a solution that is as mobile as their buisness.  Creating an application on the android platform will allow for a shop manager to process credit card transactions as well as track purchases.  The ability to track purchases will allow for the shop manager to implement a rewards program for their most loyal customers.

## 3 Product / Solution Overview

PReMS will be an android based application that will process payments, store purchase information and track rewards information.  This system will also e-mail customers when they have earned a reward in the rewards program.



## 4 User Manual
PReMS has two main operations: ***Customer Account Management*** and ***Purchase Management***. The following sections will give an overview of the various operations of the PReMS and how to perform those.

------------

##### Customer Account Management

PReMS will start from "Stall Manager" page.

###### Add a new customer.

* Select **Add Customer** to add the following customer information:

  -- First Name
  
  -- Last Name
  
  -- Zip Code
  
  -- E-mail Address
  

* **Add** button to add customer information and go to customer list view

* **Clear** button to clear off the information put on the screen

* **Back** button to go back to "Stall Manager" page

###### View customer.
* Select **View Customer** to show customer information from 
customer list

* Select the customer which you want to check for details, it will be highlighted

* **View Purchase** Button: direct to View Purchase history

* **View Rewards** Button: direct to View Rewards details

* **Back** Button to go back to "Stall Manager" page

###### Edit customer.
* Select **View Customer** to show customer information from customer list

* Select the customer to be edited, it will be highlighted

* **Edit** button to go to customer information

* **Update** when you complete editing customer information 

* **Back** Button to go back to "Stall Manager" page

###### Delete customer
* Select **View Customer** to show customer information from customer list

* Select the customer to be deleted, it will be highlighted 

* **Delete** button to delete customer account

* **Back** button to go back to "Stall Manager" page


----------

##### Purchase Management

PReMS will start from "Stall Manager" page.

###### Adding a purchase
* if the customer is a new customer, add customer first (refer to Customer Account Management => Add a new customer)

* Select **View Customer**, for customer who is an existing customer has an account

* Select the customer on the list, it will be highlighted

* **Add Purchase** to input new purchase

* Enter total purchase then Press **"+"** button: after clicking, the screen will show total purchase amount, gold discount amount, reward amount and adjusted total amount.

* **"-"** button: will clear screen 

* **Check out** button to complete the current transaction, Rewards balance and Gold Status notice emails will be sent automatically as needed

* **Back** button to go back to previous view

----------

#### Requirements
##### Platform
PReMS is designed to run on the Android 4.1+ platform.

##### Network Connectivity
PReMS requires network connectivity in order to send emails to customer. If network connectivity is temporarily unavailable PReMS will not send purchase information or rewards balance to customer.
