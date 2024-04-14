package com.example.banking.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsRequest {
	private long accountId;
	private int pageSize;
	private int pageIndex;
}
