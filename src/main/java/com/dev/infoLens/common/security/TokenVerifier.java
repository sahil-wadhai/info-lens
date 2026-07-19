package com.dev.infoLens.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenVerifier {

    @Value("${security.jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            return !extractAllClaims(token).getExpiration().before(new java.util.Date());
        } catch (Exception e) {
            return false;
        }
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        // 1. Extract all claims from the token
        Claims claims = extractAllClaims(token);

        // 2. Retrieve the list of roles stored in the token
        // you stored them as a List of Strings (e.g., ["ROLE_USER", "ROLE_ADMIN"])
        List<String> roles = claims.get("roles", List.class);

        if (roles == null) {
            return List.of(); // Return empty list if no roles exist
        }

        // 3. Map the string list to Spring Security SimpleGrantedAuthority objects
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

