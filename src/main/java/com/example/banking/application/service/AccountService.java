package com.example.banking.application.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.banking.application.dao.AccountEntity;
import com.example.banking.application.dao.CustomerEntity;
import com.example.banking.application.exception.AccountExistsException;
import com.example.banking.application.exception.InvalidAccountTypeException;
import com.example.banking.application.repository.AccountRepository;
import com.example.banking.application.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Transactional
	public String createAccount(String accountType) throws InvalidAccountTypeException, AccountExistsException {
		
		if (!"current".equalsIgnoreCase(accountType) && !"savings".equalsIgnoreCase(accountType)) {
			logger.error("Account type should be either Savings or Current");
			throw new InvalidAccountTypeException("Account type should be either Savings or Current");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		CustomerEntity customerEntity = customerRepository.findByUsername(userName);
		
		if (customerEntity == null) {
			logger.error("User not found for the token provided");
			throw new UsernameNotFoundException("User not found for the token provided " + userName);
		}
		
		for(AccountEntity accountEntity : customerEntity.getAccounts()) {
			if (accountEntity.getAccountType().equalsIgnoreCase(accountType)) {
				logger.error("An Account with account type already exists for the user");
				throw new AccountExistsException("An Account with account type already exists for the user");
			}
		}
		
		AccountEntity accountEntity = AccountEntity.builder()
				.accountType(accountType)
				.balance(new BigDecimal(0))
				.customerId(customerEntity.getCustomerId())
				.build();
		
		accountRepository.save(accountEntity);
		
		return "Account created successfully";
	}

}
