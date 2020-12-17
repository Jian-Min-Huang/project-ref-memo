package com.example.demo.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenService {

    private final String ISSUER = "jianminhuang.cc";
    private final Integer EXPIRATION_MS = 30 * 60 * 1000;
    private final String SECRET = "ajfojefoijeoifjeiojfoiejfoijeoifjeiofjeoifauiwbgowerogfiajfojefoijeoifjeiojfoiejfoijeoifjeiofjeoifauiwbgowerogfi";

    public String generate(final String subject) {
        final long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(subject)
                .setAudience(null)
                .setExpiration(new Date(currentTimeMillis + EXPIRATION_MS))
                .setNotBefore(new Date(currentTimeMillis))
                .setIssuedAt(new Date(currentTimeMillis))
                .setId(null)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String retrieveSubject(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
