package com.example.banking.application.exception;

public class InSufficientBalanceException extends Exception {

	public InSufficientBalanceException(String message) {
		super(message);
	}
}
