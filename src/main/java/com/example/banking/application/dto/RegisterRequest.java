package com.example.banking.application.dto;

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
public class RegisterRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String contact;
	private String username;
	private String password;
	private String role;
	private String userstatus;
}
