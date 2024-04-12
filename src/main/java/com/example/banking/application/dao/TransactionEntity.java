package com.example.banking.application.dao;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "transaction")
public class TransactionEntity {

	@SequenceGenerator(name = "transaction_seq", initialValue = 25562412, allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "transaction_seq")
	@Column(name = "transaction_id")
	private long transactionId;

	@Column(name = "account_id")
	private long accountId;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "timestamp")
	private Date timestamp;

}
