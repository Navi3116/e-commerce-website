package com.naveen.authservice.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.naveen.authservice.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secret;
	
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRole().name());
	    claims.put("userId", user.getId()); // <--- Add this!

	    return Jwts.builder()
	            .claims(claims)
	            .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getSigningKey(), Jwts.SIG.HS256) 
                .compact();
    }

    private SecretKey getSigningKey() {
        // 3. Use Decoders if your secret is the Hex/Base64 string we generated
        // Using .getBytes() on a Hex string creates a different key than decoding it!
        byte[] keyBytes = Decoders.BASE64.decode(secret); 
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
