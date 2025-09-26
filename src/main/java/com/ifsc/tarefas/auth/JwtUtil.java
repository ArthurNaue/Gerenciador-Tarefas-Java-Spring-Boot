package com.ifsc.tarefas.auth;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil 
{
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXP_SECONDS = 8 * 60 * 60;

    public String generateToken(String username, String role)
    {
        Instant now = Instant.now();
        return Jwts.builder()
            .setSubject(username)
            .addClaims(Map.of("role", role))
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(EXP_SECONDS)))
            .signWith(key)
            .compact();
    }

    public String getSubject(String token) throws Exception 
    {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public String getRole(String token) throws Exception 
    {
        Object role = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role");
            
        return role == null ? null : role.toString();
    }
}
