package com.example.banking.application.exception;

public class AccountExistsException extends Exception {

	public AccountExistsException (String message) {
		super(message);
	}
}
