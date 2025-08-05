package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.BankResponse;
import com.project.dto.CreditDebitRequest;
import com.project.dto.EnquiryRequest;
import com.project.dto.TransferRequest;
import com.project.dto.UserRequest;
import com.project.service.UserService;

@RestController
@RequestMapping("/api/user/")
public class UserController {

	@Autowired
	UserService service;

	@PostMapping("createAccount")
	public BankResponse createAccount(@RequestBody UserRequest request) {
		return service.createAccount(request);

	}
	
	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return service.nameEnquiry(enquiryRequest);

	}
	
	@GetMapping("balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return service.balanceEnquiry(enquiryRequest);

	}
	
	@PostMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return service.creditAccount(request);

	}
	
	@PostMapping("debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return service.debitAccount(request);

	}
	
	@PostMapping("transfer")
	public BankResponse debitAccount(@RequestBody TransferRequest request) {
		return service.transfer(request);

	}

}
