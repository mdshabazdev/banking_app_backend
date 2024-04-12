package com.example.banking.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.banking.application.dao.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	public CustomerEntity findByUsername(String username);
	
	@Query("SELECT username from CustomerEntity where username=:username or contact=:contact or email=:email")
	public List<String> findByUserdetails(@Param("username") String username, @Param("contact") String contact, @Param("email") String email);
}
