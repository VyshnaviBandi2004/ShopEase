package com.shopease.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

/**
 * Simple JWT service for generating and validating tokens.
 * Uses JJWT (io.jsonwebtoken) library.
 *
 * Note: For a production application, configure the signing key externally
 * (e.g. environment variable or secure configuration) so it survives restarts
 * and is not checked into source control.
 */
@Service
public class JwtService {

    // Stable signing key for demo purposes. In production, load this from
    // a secure external source (environment variable, vault, etc.).
    private static final byte[] SECRET_BYTES = "ReplaceThisWithASecureRandomKey_ChangeMe123456".getBytes(StandardCharsets.UTF_8);
    private final SecretKey signingKey = Keys.hmacShaKeyFor(SECRET_BYTES);

    // Token validity: 24 hours
    private final long validitySeconds = 24 * 60 * 60;

    public String generateToken(String email) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiresAt = Date.from(now.plus(validitySeconds, ChronoUnit.SECONDS));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
