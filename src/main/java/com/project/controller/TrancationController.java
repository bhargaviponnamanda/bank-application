package com.project.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.project.entity.Transaction;
import com.project.serviceImpl.BankStatement;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
@Tag(name = "Generating Bank Statement Api's")
public class TrancationController {
	
	private BankStatement bankStatement;
	
	@GetMapping
	public List<Transaction> generateBankStatement(@RequestParam String accountNumber, 
			                                       @RequestParam String startDate,
			                                       @RequestParam String endDate) throws DocumentException, MalformedURLException, IOException {
		
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
		
		

	}

}
