package com.example.banking.application.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.banking.application.dao.TransactionEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponse {

	@JsonInclude(Include.NON_NULL)
	private Error error;
	
	@JsonInclude(Include.NON_NULL)
	private TransactionDetails transactionDetails; 
	
	public void setErrorMessage(String errorMessage) {
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
	
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public class TransactionDetails {
		private long accountId;
		private BigDecimal balance;
		private int pageIndex;
		private int pageSize;
		private long total;
		private List<TransactionEntity> transactions;
	}

	public TransactionDetails getTransactionDetails() {
		if (this.transactionDetails == null) {
			return new TransactionDetails();
		}
		return this.transactionDetails;
	}
}
