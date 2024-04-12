package com.example.banking.application.controller;

import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banking.application.dto.DepositRequest;
import com.example.banking.application.dto.SendMoneyRequest;
import com.example.banking.application.dto.TransactionResponse;
import com.example.banking.application.dto.WithdrawRequest;
import com.example.banking.application.exception.InSufficientBalanceException;
import com.example.banking.application.exception.InvalidAccountDetailsException;
import com.example.banking.application.service.TransactionService;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	enum TransactionType {
	    CREDIT,
	    DEBIT;
	}

	@PostMapping("/deposit")
	public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {
		TransactionResponse transactionResponse = new TransactionResponse();
		try {
			if (transactionService.getValidUserAccount(depositRequest.getAccountId()) == null) {
				transactionResponse.setError("Account id doesn't belongs to the user");
				return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			String message = transactionService.transact(depositRequest.getAccountId(), depositRequest.getAmount(), TransactionType.CREDIT.name());
			transactionResponse.setMessage(message);
			return ResponseEntity.ok(transactionResponse);
		} catch (AccountNotFoundException e) {
			transactionResponse.setError("Account not found for the account id");
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			logger.error("Some exception occured at the server: ", e);
			return ResponseEntity.internalServerError().body("Something went wong while deposit");
		}
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
		TransactionResponse transactionResponse = new TransactionResponse();
		try {
			if (transactionService.getValidUserAccount(withdrawRequest.getAccountId()) == null) {
				transactionResponse.setError("Account id doesn't belongs to the user");
				return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			String message = transactionService.transact(withdrawRequest.getAccountId(), withdrawRequest.getAmount(), TransactionType.DEBIT.name());
			transactionResponse.setMessage(message);
			return ResponseEntity.ok(transactionResponse);
		} catch (AccountNotFoundException e) {
			transactionResponse.setError("Account not found for the account id");
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (InSufficientBalanceException e) {
			transactionResponse.setError("Insufficient balance in the account");
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			logger.error("Some exception occured at the server: ", e);
			return ResponseEntity.internalServerError().body("Something went wong while deposit");
		}
	}
	
	@PostMapping("/sendMoney")
	public ResponseEntity<?> sendMoney(@RequestBody SendMoneyRequest sendMoneyRequest) {
		TransactionResponse transactionResponse = new TransactionResponse();
		try {
			transactionService.validateDetails(sendMoneyRequest);
			String message = transactionService.sendMoneyTransact(sendMoneyRequest.getFromAccountId(), sendMoneyRequest.getAccountId(), sendMoneyRequest.getAmount());
			transactionResponse.setMessage(message);
			return ResponseEntity.ok(transactionResponse);
		} catch (AccountNotFoundException e) {
			transactionResponse.setError("Account not found for the account id");
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (InvalidAccountDetailsException e) {
			transactionResponse.setError(e.getMessage());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (InSufficientBalanceException e) {
			transactionResponse.setError("Insufficient balance in the account");
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			logger.error("Some exception occured at the server: ", e);
			return ResponseEntity.internalServerError().body("Something went wong while deposit");
		}
	}
}
