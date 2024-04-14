create database banking_application;
CREATE TABLE IF NOT EXISTS customer (     
	customer_id SERIAL PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	contact VARCHAR(20) UNIQUE NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	username VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(300) NOT NULL,
	user_status VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS account (
	account_id SERIAL PRIMARY KEY,
	customer_id BIGINT NOT NULL,
	account_type VARCHAR(50) NOT NULL,
	balance NUMERIC(10, 2) NOT NULL,
	FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE IF NOT EXISTS transaction (
	transaction_id SERIAL PRIMARY KEY,
	account_id BIGINT NOT NULL,
	transaction_type VARCHAR(10) NOT NULL,
	amount NUMERIC(10, 2) NOT NULL,
	timestamp TIMESTAMP NOT NULL, 
	FOREIGN KEY (account_id) REFERENCES account(account_id)
);

CREATE SEQUENCE customer_seq
INCREMENT 1
START 14414532;

CREATE SEQUENCE transaction_seq
INCREMENT 1
START 25562412;