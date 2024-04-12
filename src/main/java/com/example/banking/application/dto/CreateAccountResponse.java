package com.example.banking.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CreateAccountResponse {

	@JsonInclude(Include.NON_NULL)
	private String message;
	
	@JsonInclude(Include.NON_NULL)
	private Error error;
	
	public CreateAccountResponse() {}
	
	public CreateAccountResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Error getError() {
		return error;
	}

	public void setError(String errorMessage) {
		this.error = new Error();
		error.setMessage(errorMessage);
	}

	class Error {
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
	}
}
