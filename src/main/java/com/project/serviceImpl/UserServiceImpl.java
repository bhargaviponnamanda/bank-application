package com.project.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.AccountInfo;
import com.project.dto.BankResponse;
import com.project.dto.CreditDebitRequest;
import com.project.dto.EmailDetails;
import com.project.dto.EnquiryRequest;
import com.project.dto.TransactionDto;
import com.project.dto.TransferRequest;
import com.project.dto.UserRequest;
import com.project.entity.User;
import com.project.repository.UsersRepository;
import com.project.service.EmailService;
import com.project.service.TransactionService;
import com.project.service.UserService;
import com.project.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TransactionService transactionService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		
		/*
		 * 1.Creating an account - saving a new user into a db
		 * 
		 * 2.Check if User is Already having Account
		 * 
		 */		
		if(usersRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User newUser = User.builder()
				   .firstName(userRequest.getFirstName())
				   .lastName(userRequest.getLastName())
				   .otherName(userRequest.getOtherName())
				   .gender(userRequest.getGender())
				   .address(userRequest.getAddress())
				   .stateOfOrigin(userRequest.getStateOfOrigin())
				   .accountNumber(AccountUtils.generateAccountNumber())
				   .accountBalance(BigDecimal.ZERO)
				   .email(userRequest.getEmail())
				   .phoneNumber(userRequest.getPhoneNumber())
				   .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				   .status("ACTIVE")
				   .build();
		User saveUser = usersRepository.save(newUser);
		
		//Send email Alerts
		EmailDetails emailDetails = EmailDetails.builder()
				                                 .recipient(saveUser.getEmail())
				                                 .subject("ACCOUNT CREATION")
				                                 .messageBody("Congratulations! Your Account has been Successfully Created. \nYour Account Details: \n" 
				                                		 + "Account Name: \n" + saveUser.getFirstName() + " " 
				                                		 + saveUser.getLastName() + "\nAccount Number: " 
				                                		 + saveUser.getAccountNumber())
				                                 .build();
		emailService.sendEmailAlert(emailDetails);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATIONAL_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREATIONAL_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(saveUser.getAccountBalance())
						.accountNumber(saveUser.getAccountNumber())
						.accountName(saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherName())
						.build())
				.build();
	}
	
	//balance Enquiry, name Enguiry, Credit, debit, transfer

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		//Check if the provided account number exists in the db
		boolean isAccountExits = usersRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if(!isAccountExits) {
			return BankResponse.builder()
					           .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					           .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					           .accountInfo(null)
					           .build();
			
		}
		
		User foundUser = usersRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return BankResponse.builder()
		           .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
		           .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
		           .accountInfo(AccountInfo.builder()
		        		   .accountBalance(foundUser.getAccountBalance())
		        		   .accountNumber(foundUser.getAccountNumber())
		        		   .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
		        		   .build())
		           .build();
		
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {

		// Check if the provided account number exists in the db
		boolean isAccountExits = usersRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if (!isAccountExits) {
			return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
		}
		
		User foundUser = usersRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return foundUser.getFirstName() + " " + foundUser.getLastName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		// Checking if the account exists
		boolean isAccountExits = usersRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExits) {
			return BankResponse.builder()
			           .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
			           .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
			           .accountInfo(null)
			           .build();
		}
		
		User userToCredit = usersRepository.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		usersRepository.save(userToCredit);
		
		// Save Transaction
		TransactionDto dto = TransactionDto.builder()
				                  .accountNumber(userToCredit.getAccountNumber())
				                  .transactionType("CREDIT")
				                  .amount(request.getAmount())
				                  .build();
		
		transactionService.saveTransaction(dto);
		
		return BankResponse.builder()
		           .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
		           .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
		           .accountInfo(AccountInfo.builder()
		        		      .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
		        		      .accountBalance(userToCredit.getAccountBalance())
		        		      .accountNumber(userToCredit.getAccountNumber())
		        		   .build())
		           .build();
	}

	
	//@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
			// Checking if the account exists
			boolean isAccountExits = usersRepository.existsByAccountNumber(request.getAccountNumber());
			if (!isAccountExits) {
				return BankResponse.builder()
				           .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
				           .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
				           .accountInfo(null)
				           .build();
			}
	
	       // Check if the Amount we intend to withdraw is not more than the Current account balance
			
			User userToDebit = usersRepository.findByAccountNumber(request.getAccountNumber());
			
			BigInteger availableAmount = userToDebit.getAccountBalance().toBigInteger();
			BigInteger debitAmount = request.getAmount().toBigInteger();
			
			if (availableAmount.intValue() < debitAmount.intValue()) {
				return BankResponse.builder()
				           .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
				           .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
				           .accountInfo(null)
				           .build();
					
				} else {
					userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
					usersRepository.save(userToDebit);
					
					// Save Transaction
					TransactionDto dto = TransactionDto.builder()
			                  .accountNumber(userToDebit.getAccountNumber())
			                  .transactionType("DEBIT")
			                  .amount(request.getAmount())
			                  .build();

		               transactionService.saveTransaction(dto);
					
					return BankResponse.builder()
					           .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
					           .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
					           .accountInfo(AccountInfo.builder()
					        		      .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
					        		      .accountBalance(userToDebit.getAccountBalance())
					        		      .accountNumber(userToDebit.getAccountNumber())
					        		   .build())
					           .build();
				}
				
			}

	@Override
	public BankResponse transfer(TransferRequest request) {
		
		// 1. Get the Account to debit (Check if it exists)
		// 2.Check if the amount we are debiting is not more than the Current balance
		// 3.Debit from the Account
		// 4.Get the account to credit
		// 5.Credit the Amount to the account
		
		boolean isSourceAccountExit = usersRepository.existsByAccountNumber(request.getSourceAccountNumber());
		boolean isDestinationAccountExit = usersRepository.existsByAccountNumber(request.getDestinationAccountNumber());
		
		if(!isDestinationAccountExit) {
			return BankResponse.builder()
			           .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
			           .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
			           .accountInfo(null)
			           .build();
		}
		
		User sourceAccountUser = usersRepository.findByAccountNumber(request.getSourceAccountNumber());
		
		
		if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
			
			return BankResponse.builder()
			           .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
			           .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
			           .accountInfo(null)
			           .build();	
		}
		
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUsername = sourceAccountUser.getFirstName() + sourceAccountUser.getLastName();
		usersRepository.save(sourceAccountUser);
		
		EmailDetails debitAlert = EmailDetails.builder()
				                      .subject("DEBIT ALERT")
				                      .recipient(sourceAccountUser.getEmail())
				                      .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your Current balance is " 
				                      + sourceAccountUser.getAccountBalance())
				                     .build();
		
		emailService.sendEmailAlert(debitAlert);
		
		User destinationAccountUser = usersRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		String recipientUsername = destinationAccountUser.getFirstName() + destinationAccountUser.getLastName();
		usersRepository.save(destinationAccountUser);
		
		EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() + " has been sent to  your account from "  
                 + sourceUsername + " Your current balance is " + sourceAccountUser.getAccountBalance()).build();
        
           emailService.sendEmailAlert(creditAlert);
           
        // Save Transaction
         TransactionDto dto = TransactionDto.builder()
        	                  .accountNumber(destinationAccountUser.getAccountNumber())
        	                  .transactionType("DEBIT")
        	                  .amount(request.getAmount())
        	                  .build();

         transactionService.saveTransaction(dto);
           
           return BankResponse.builder()
           .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
           .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
           .accountInfo(null)
           .build(); 

	}	
	
	}
	
