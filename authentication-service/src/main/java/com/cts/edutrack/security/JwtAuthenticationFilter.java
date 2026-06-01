package com.cts.edutrack.security;
 
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import com.cts.edutrack.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
 
import io.jsonwebtoken.ExpiredJwtException;
 
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 
    @Autowired
    private JwtUtil jwtUtil;
 
    @Autowired
    private CustomUserDetailsService userDetailsService;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
 
        String header = request.getHeader("Authorization");
 
        try {
 
            if (header != null && header.startsWith("Bearer ")) {
 
                String token = header.substring(7);
 
                String email = jwtUtil.extractEmail(token);
 
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);
 
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
 
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
 
            chain.doFilter(request, response);
 
        } catch (ExpiredJwtException ex) {
 
            // Clear context
            SecurityContextHolder.clearContext();
 
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
 
            ApiResponse errorResponse = new ApiResponse(
                    false,
                    "Token expired. Please login again.",
                    null,
                    HttpStatus.UNAUTHORIZED.value(),
                    null
            );
 
            new ObjectMapper()
                    .writeValue(response.getOutputStream(), errorResponse);
 
        } catch (Exception ex) {
 
            SecurityContextHolder.clearContext();
 
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
 
            ApiResponse errorResponse = new ApiResponse(
                    false,
                    "Invalid or malformed token.",
                    null,
                    HttpStatus.UNAUTHORIZED.value(),
                    null
            );
 
            new ObjectMapper()
                    .writeValue(response.getOutputStream(), errorResponse);
        }
    }
}