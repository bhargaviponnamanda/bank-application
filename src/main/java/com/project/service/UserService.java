package com.project.service;

import com.project.dto.BankResponse;
import com.project.dto.CreditDebitRequest;
import com.project.dto.EnquiryRequest;
import com.project.dto.TransferRequest;
import com.project.dto.UserRequest;


public interface UserService {
	
	public BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	
	String nameEnquiry(EnquiryRequest enquiryRequest);
    
	BankResponse creditAccount(CreditDebitRequest request);
	
	BankResponse debitAccount(CreditDebitRequest request);
	
	BankResponse transfer(TransferRequest request);
}
