//package com.cts.edutrack.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//import java.security.Key;
//
//@Component
//public class JwtUtil {
//
//    // Must be identical to the one in your Auth-Service and Course-Service
//    public static final String SECRET = "MyEduTrackSecretKeyForJwtAuthentication2026";
//
//    public void validateToken(final String token) {
//        Jwts.parserBuilder()
//            .setSigningKey(getSigningKey())
//            .build()
//            .parseClaimsJws(token);
//    }
//
//    private Key getSigningKey() {
//        byte[] keyBytes = SECRET.getBytes();
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}