package com.example.banking.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.banking.application.config.CustomerUserDetailsService;
import com.example.banking.application.config.JWTTokenUtil;
import com.example.banking.application.dao.CustomerEntity;
import com.example.banking.application.dto.AuthenticationRequest;
import com.example.banking.application.dto.AuthenticationResponse;
import com.example.banking.application.dto.RegisterRequest;
import com.example.banking.application.exception.UserExistsException;
import com.example.banking.application.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	CustomerUserDetailsService userDetailsService;
	
	@Autowired
	JWTTokenUtil tokenUtil;

	@Transactional
	public AuthenticationResponse register(RegisterRequest request) throws Exception {

		List<String> usernames = customerRepository.findByUserdetails(request.getUsername(), request.getContact(),
				request.getEmail());
		if (usernames.isEmpty()) {
			try {
				CustomerEntity customer = CustomerEntity.builder()
						.firstName(request.getFirstName())
						.lastName(request.getLastName())
						.email(request.getEmail())
						.contact(request.getContact())
						.username(request.getUsername())
						.role(request.getRole())
						.userstatus(request.getUserstatus())
						.password(passwordEncoder.encode(request.getPassword()))
						.build();

				customerRepository.save(customer);

				var jwtToken = tokenUtil.generateToken(customer);
				AuthenticationResponse authenticationResponse = new AuthenticationResponse();
				authenticationResponse.setToken(jwtToken);
				return authenticationResponse;
			} catch (Exception ex) {
				throw new Exception("Something went wrong.");
			}
		} else {
			throw new UserExistsException("Username or email or contact number already exists.");
		}
	}

	public String authenticate(AuthenticationRequest authenticationRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		return tokenUtil.generateToken(userDetails);
	}
}
