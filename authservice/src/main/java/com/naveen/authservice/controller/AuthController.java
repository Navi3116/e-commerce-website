package com.naveen.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naveen.authservice.dto.AuthRequest;
import com.naveen.authservice.entity.User;
import com.naveen.authservice.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService; // Custom service for DB logic

	@PostMapping("/register")
	public String register(@RequestBody User user) {
		return authService.saveUser(user);
	}

	@PostMapping("/login")
	public String getToken(@RequestBody AuthRequest authRequest) {
		// 1. Authenticate user via DB
		// 2. If valid, return JWT
		return authService.generateToken(authRequest);
	}

}
