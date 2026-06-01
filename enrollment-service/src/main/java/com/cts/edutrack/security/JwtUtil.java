package com.cts.edutrack.security;
 
import java.security.Key;
import java.util.Date;
 
import org.springframework.stereotype.Component;
 
//import com.cts.edutrack.model.User;
 
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
 

@Component
public class JwtUtil {
    private final String SECRET = "MyEduTrackSecretKeyForJwtAuthentication2026";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}