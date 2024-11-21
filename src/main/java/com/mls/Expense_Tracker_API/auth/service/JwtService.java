package com.mls.Expense_Tracker_API.auth.service;

import com.mls.Expense_Tracker_API.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;
    @Value("${security.jwt.refresh-token.expiration-time}")
    private Long refreshExpiration;


    public String generateToken (final User user) {
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken (final User user) {
        return buildToken(user, refreshExpiration);
    }

    public String buildToken(final User user, final Long expiration) {
        return Jwts.builder()
                .setId(user.getId().toString())
                .addClaims(Map.of("name", user.getName()))
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
