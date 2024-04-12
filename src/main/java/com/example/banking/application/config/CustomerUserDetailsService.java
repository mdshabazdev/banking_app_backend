package com.example.banking.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.banking.application.dao.CustomerEntity;
import com.example.banking.application.repository.CustomerRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomerEntity customerDAO = customerRepository.findByUsername(username);
		if (customerDAO != null) {
			return customerDAO;
		}
		throw new UsernameNotFoundException("Username not found: " + username);
	}
	
}
