package com.cts.edutrack.security;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
// IMPORTANT: These MUST be the .reactive. versions for Spring Cloud Gateway
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            // 1. Apply the CORS configuration defined below
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Disable CSRF (standard for Stateless Microservices/Gateways)
            .csrf(csrf -> csrf.disable()) 
            
            // 3. Routing authorization
            .authorizeExchange(exchanges -> exchanges
                // Allow all traffic through the security layer; 
                // your Global Filter handles the JWT logic.
                .anyExchange().permitAll() 
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow the Angular frontend
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        // Standard REST methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Standard headers + Authorization for JWT
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        
        // Allow sending the JWT in the header or cookies
        config.setAllowCredentials(true);
        
        // Make sure the frontend can read the Authorization header if returned by the server
        config.setExposedHeaders(Arrays.asList("Authorization"));

        // Use the Reactive URL-based source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }
}