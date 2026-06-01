package com.cts.edutrack.security;
 
import java.security.Key;
import java.util.Date;
 
import org.springframework.stereotype.Component;
 
import com.cts.edutrack.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
 
@Component
public class JwtUtil {
 
    private final String SECRET =
            "MyEduTrackSecretKeyForJwtAuthentication2026";
 
    // 1 minute for testing
    private final long EXPIRATION = 1000 * 60 * 60;
 
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
 
    public String generateToken(User user) {
 
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .claim("userId",user.getUserId())                
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
 
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}