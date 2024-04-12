package com.example.banking.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendMoneyRequest {

	private long fromAccountId;
	private long accountId;
	private String ifscCode;
	private String bankingName;
	private BigDecimal amount;

}
