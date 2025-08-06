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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user/")
@Tag(name = "User Account Management Api's")
public class UserController {

	@Autowired
	UserService service;
	
	@Operation(summary = "Create New User Account", description = "Creating a new user and assigning an account ID")
	@ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
	@PostMapping("createAccount")
	public BankResponse createAccount(@RequestBody UserRequest request) {
		return service.createAccount(request);

	}
	
	@Operation(summary = "Name Enquiry", description = "User Account Enquiry based on the name")
	@ApiResponse(responseCode = "202", description = "Http Status 202 CREATED")
	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return service.nameEnquiry(enquiryRequest);

	}
	
	@Operation(summary = "Balance Enquiry", description = "Retriveing User Account details for balance")
	@ApiResponse(responseCode = "203", description = "Http Status 203 CREATED")
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
