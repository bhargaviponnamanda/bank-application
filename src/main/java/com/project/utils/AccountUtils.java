package com.project.utils;

import java.time.Year;

public class AccountUtils {
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created";
	
	public static final String ACCOUNT_CREATIONAL_CODE = "002";
	public static final String ACCOUNT_CREATIONAL_MESSAGE = "Account Successfully created!!";
	
	public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
	public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with the provided Account Number does not exist";
	
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_MESSAGE = "User with the provided Account Number Already exist";
	
	public static final String ACCOUNT_CREDITED_SUCCESS = "005";
	public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Amount Credited to the User Account Successfully";
	
	public static final String INSUFFICIENT_BALANCE_CODE = "006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
	
	public static final String ACCOUNT_DEBITED_SUCCESS = "007";
	public static final String ACCOUNT_DEBITED_MESSAGE = "Amount Debited from the User Account Successfully";
	
	public static final String DEBIT_ACOUNT_NOT_EXISTS = "008";
	public static final String DEBIT_ACCOUNT_NOT_EXISTS_MESSAGE = "Debited Account is not Present";
	
	public static final String CREDIT_ACOUNT_NOT_EXISTS = "009";
	public static final String CREDIT_ACCOUNT_NOT_EXISTS_MESSAGE = "Credit Account is not Present";
	
	public static final String TRANSFER_SUCCESS_CODE = "010";
	public static final String TRANSFER_SUCCESS_MESSAGE = "Transfer Successfully";

	public static String generateAccountNumber() {

		/* 2025+ randomSixDigits */

		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;
		int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

		// Convert the Current year and randomNumber to Strings, Then concatenate
		String year = String.valueOf(currentYear);

		String randomNumber = String.valueOf(randNumber);

		StringBuilder accountNumber = new StringBuilder();
		return accountNumber.append(year).append(randomNumber).toString();

		
	}

}
