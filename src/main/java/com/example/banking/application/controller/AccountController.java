package com.example.banking.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.banking.application.dto.AuthenticationResponse;
import com.example.banking.application.dto.CreateAccountResponse;
import com.example.banking.application.exception.AccountExistsException;
import com.example.banking.application.exception.InvalidAccountTypeException;
import com.example.banking.application.service.AccountService;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;

	@PostMapping("/createAccount")
	public ResponseEntity<?> createAccount(@RequestParam String accountType) {
		CreateAccountResponse createAccountResponse = new CreateAccountResponse();
		try {
			createAccountResponse.setMessage(accountService.createAccount(accountType));
			return ResponseEntity.ok(createAccountResponse);
		} catch (InvalidAccountTypeException e) {
			createAccountResponse.setError("Account type should be either Savings or Current");
			return new ResponseEntity<CreateAccountResponse>(createAccountResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (AccountExistsException e) {
			createAccountResponse.setError("An Account with account type already exists for the user");
			return new ResponseEntity<CreateAccountResponse>(createAccountResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (UsernameNotFoundException e) {
			createAccountResponse.setError("User not found for the token provided");
			return new ResponseEntity<CreateAccountResponse>(createAccountResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			logger.error("Some exception occured at the server: ", e);
			return ResponseEntity.internalServerError().body("Some exception occured at the server");
		}
	}
}
