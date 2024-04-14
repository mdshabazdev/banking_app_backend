package com.example.banking.application.dao;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
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
@Table(name = "account")
public class AccountEntity {

	@SequenceGenerator(name = "customer_seq", initialValue = 14414532, allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "customer_seq")
	private long accountId;

	@Column(name = "customer_id")
	private long customerId;

	@Column(name = "account_type")
	private String accountType;

	@Column(name = "balance")
	private BigDecimal balance;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="transaction_id")
	private Set<TransactionEntity> transactions;

}
