package com.example.banking.application.dto;

import java.util.List;
import java.util.Set;

import com.example.banking.application.dao.AccountEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AccountsResponse {

	@JsonInclude(Include.NON_NULL)
	private Set<AccountEntity> accounts;

	@JsonInclude(Include.NON_NULL)
	private Error error;

	public AccountsResponse() {
	}

	public Set<AccountEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountEntity> accounts) {
		this.accounts = accounts;
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
