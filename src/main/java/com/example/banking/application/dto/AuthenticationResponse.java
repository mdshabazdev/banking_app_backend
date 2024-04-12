package com.example.banking.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AuthenticationResponse {

	@JsonInclude(Include.NON_NULL)
	private String token;
	
	@JsonInclude(Include.NON_NULL)
	private Error error;
	
	public AuthenticationResponse() {}
	
	public AuthenticationResponse(String token) {
		super();
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
