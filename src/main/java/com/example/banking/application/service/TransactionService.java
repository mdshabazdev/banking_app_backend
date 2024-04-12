package com.example.banking.application.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.banking.application.dao.AccountEntity;
import com.example.banking.application.dao.CustomerEntity;
import com.example.banking.application.dao.TransactionEntity;
import com.example.banking.application.dto.SendMoneyRequest;
import com.example.banking.application.exception.InSufficientBalanceException;
import com.example.banking.application.exception.InvalidAccountDetailsException;
import com.example.banking.application.repository.AccountRepository;
import com.example.banking.application.repository.CustomerRepository;
import com.example.banking.application.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
	
	@Transactional
	public String transact(long accountId, BigDecimal amount, String transactionType) throws Exception {
		Optional<AccountEntity> accountEntityResult = accountRepository.findById(accountId);
		
		if(accountEntityResult.isEmpty()) {
			logger.error("Account not found for the account id");
			throw new AccountNotFoundException("Account not found for the account id");
		}
		
		AccountEntity accountEntity = accountEntityResult.get();
		
		TransactionEntity transactionEntity = TransactionEntity.builder()
				.accountId(accountEntity.getAccountId())
				.amount(amount)
				.transactionType(transactionType)
				.timestamp(new Date())
				.build();
		
		switch (transactionType) {
			case "CREDIT":
				accountEntity.setBalance(accountEntity.getBalance().add(amount));
				break;
			case "DEBIT":
				if (accountEntity.getBalance().compareTo(amount) < 0) {
					throw new InSufficientBalanceException("Insufficient balance in the account");
				}
				accountEntity.setBalance(accountEntity.getBalance().subtract(amount));
				break;
			default:
				throw new Exception("Invalid transaction type");
		}
		
		TransactionEntity transactionResult = transactionRepository.save(transactionEntity);
		
		accountRepository.save(accountEntity);
		
		return "Transaction successful with transaction id: " + transactionResult.getTransactionId();
	}
	
	@Transactional
	public String sendMoneyTransact(long fromAccountId, long accountId, BigDecimal amount) throws Exception {
		AccountEntity fromAccountEntity = getValidUserAccount(fromAccountId);
		if(fromAccountEntity == null) {
			throw new Exception("Account id doesn't belongs to the user");
		}
		
		if (fromAccountEntity.getBalance().compareTo(amount) < 0) {
			throw new InSufficientBalanceException("Insufficient balance in the account");
		}
		
		Optional<AccountEntity> accountEntityResult = accountRepository.findById(accountId);
		
		if(accountEntityResult.isEmpty()) {
			logger.error("Account not found for the account id");
			throw new AccountNotFoundException("Account not found for the account id");
		}
		
		AccountEntity accountEntity = accountEntityResult.get();
		
		TransactionEntity transactionEntityCredit = TransactionEntity.builder()
				.accountId(accountEntity.getAccountId())
				.amount(amount)
				.transactionType("CREDIT")
				.timestamp(new Date())
				.build();
		
		TransactionEntity transactionEntityDebit = TransactionEntity.builder()
				.accountId(fromAccountEntity.getAccountId())
				.amount(amount)
				.transactionType("DEBIT")
				.timestamp(new Date())
				.build();
		
		accountEntity.setBalance(accountEntity.getBalance().add(amount));
		fromAccountEntity.setBalance(fromAccountEntity.getBalance().subtract(amount));
		
		TransactionEntity transactionResult = transactionRepository.save(transactionEntityCredit);
		transactionRepository.save(transactionEntityDebit);
		accountRepository.save(accountEntity);
		accountRepository.save(fromAccountEntity);
		
		return "Transaction successful with transaction id: " + transactionResult.getTransactionId();
	}

	public void validateDetails(SendMoneyRequest sendMoneyRequest) throws InvalidAccountDetailsException {
		if (sendMoneyRequest.getAccountId() == 0L) {
			throw new InvalidAccountDetailsException("Account id cannot be 0");
		} else if (sendMoneyRequest.getBankingName().isEmpty()) {
			throw new InvalidAccountDetailsException("Banking name cannot be empty");
		} else if (sendMoneyRequest.getIfscCode().isEmpty()) {
			throw new InvalidAccountDetailsException("IFSC code cannot be empty");
		}
	}
	
	public AccountEntity getValidUserAccount(long accountId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		CustomerEntity customerEntity = customerRepository.findByUsername(userName);
		
		for(AccountEntity accountEntity : customerEntity.getAccounts()) {
			if (accountEntity.getAccountId() == accountId) {
				return accountEntity;
			}
		}
		return null;
	}
}
