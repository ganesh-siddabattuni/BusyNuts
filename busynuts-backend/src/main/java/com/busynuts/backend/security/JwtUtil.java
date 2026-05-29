package com.busynuts.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // In a real app, this should be in application.properties! 
    // We are generating a secure 256-bit key for development.
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Token is valid for 24 hours
    private final long EXPIRATION_TIME = 86400000; 

    // 1. Generate the token (stamping the username and role on it)
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Store the role inside the token!
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 2. Extract the username from a token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 3. Extract the role from a token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 4. Validate if the token is still good
    public boolean validateToken(String token) {
        try {
            return !extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}