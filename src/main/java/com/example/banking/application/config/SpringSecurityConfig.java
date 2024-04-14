package com.example.banking.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import com.example.banking.application.config.JWTAuthenticationEntryPoint;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

	@Autowired
	private JWTAuthenticationFilter authenticationTokenFilter;
	
	@Autowired
	private JWTAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	private static final String[] WHITE_LIST_URL = {"/api/auth/**"};
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
				.authorizeHttpRequests(auth -> {
					auth
					.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
					.requestMatchers(WHITE_LIST_URL).permitAll()
					.requestMatchers("/api/account/**").hasRole("USER")
					.requestMatchers("/api/transaction/**").hasRole("USER")
					.anyRequest().authenticated();
				})
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

}
