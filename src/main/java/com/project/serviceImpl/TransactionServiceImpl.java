package com.project.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.TransactionDto;
import com.project.repository.TransactionRepository;
import com.project.service.TransactionService;
import com.project.entity.Transaction;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public void saveTransaction(TransactionDto transactiondto) {
		
		Transaction transaction = Transaction.builder()
				                       .transactionType(transactiondto.getTransactionType())
				                       .accountNumber(transactiondto.getAccountNumber())
				                       .amount(transactiondto.getAmount())
				                       .status("SUCCESS")
				                       .build();
		
		transactionRepository.save(transaction);
		System.out.println("Transaction Saved Successfully");
		
	}

}
