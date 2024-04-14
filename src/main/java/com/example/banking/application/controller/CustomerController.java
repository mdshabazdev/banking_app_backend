package com.example.banking.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banking.application.config.JWTTokenUtil;
import com.example.banking.application.dto.AuthenticationRequest;
import com.example.banking.application.dto.AuthenticationResponse;
import com.example.banking.application.dto.RegisterRequest;
import com.example.banking.application.exception.UserExistsException;
import com.example.banking.application.service.AuthenticationService;

@RestController
@RequestMapping(value = "/api/auth")
public class CustomerController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	JWTTokenUtil tokenUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		AuthenticationResponse resp = new AuthenticationResponse();
		try {
			logger.info("Received request for auth token");
			String token = authenticationService.authenticate(authenticationRequest);
			resp.setToken(token);
			return ResponseEntity.ok(resp);
		} catch (DisabledException exception) {
			resp.setError("Unauthorized! User is disabled!");
			return new ResponseEntity<AuthenticationResponse>(resp, HttpStatus.UNAUTHORIZED);
		} catch (BadCredentialsException exception) {
			resp.setError("Unauthorized! Bad credentials");
			return new ResponseEntity<AuthenticationResponse>(resp, HttpStatus.UNAUTHORIZED);
		} catch (Exception exception) {
			logger.error("Some exception occured at the server: ", exception.getMessage());
			resp.setError("Something went wong while registering user");
			return ResponseEntity.internalServerError().body(resp);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		AuthenticationResponse resp = new AuthenticationResponse();
		try {
			return ResponseEntity.ok(authenticationService.register(request));
		} catch (UserExistsException ex) {
			resp.setError("Username or email or contact number already exists.");
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			resp.setError("Something went wong while registering user");
			return ResponseEntity.internalServerError().body(resp);
		}
	}
}
