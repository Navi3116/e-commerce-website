package com.naveen.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naveen.authservice.dto.AuthRequest;
import com.naveen.authservice.entity.User;
import com.naveen.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Registers a new user and hashes their password before saving.
     */
    public String saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return "User registered successfully!";
    }

    /**
     * Verifies the user credentials and generates a JWT.
     */
    public String generateToken(AuthRequest authRequest) {
        // 1. Find user in the database
        User user = repository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // 2. Check if the raw password matches the hashed password in DB
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 3. Request the JwtService to create a token with the user's role
        return jwtService.generateToken(user);
    }

}
