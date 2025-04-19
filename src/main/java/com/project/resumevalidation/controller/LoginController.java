package com.project.resumevalidation.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.resumevalidation.dto.UserLoginDto;
import com.project.resumevalidation.service.OnboardService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/onboard/api")
@Validated
public class LoginController {
	public static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	OnboardService service;

	LoginController(OnboardService service) {
		this.service = service;
	}
	@Operation(summary="userLogin",description="This methods help user to login with user credentials")
	@PostMapping(value = "/userLogin", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> userLogin(@RequestBody UserLoginDto userCredentials) {
		boolean isAuthenticated = service.userLogin(userCredentials);
		Map<String, String> response = new HashMap<>();

		if (isAuthenticated) {
			response.put("message", "User has been logged in successfully");
			LOGGER.info("User login successful for: {}", userCredentials.getGmail()); // Log without exposing
																							// password
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("error", "Invalid credentials");
			LOGGER.warn("Failed login attempt for: {}", userCredentials.getGmail());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
	}

}
