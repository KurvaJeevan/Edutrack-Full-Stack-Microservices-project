//package com.cts.edutrack.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import java.util.List;
//
//@Component
//public class AuthenticationFilter implements GlobalFilter, Ordered {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private final List<String> openApiEndpoints = List.of(
//            "/api/auth/login",
//            "/api/users/registerUser",
//            "/api/users/registerProfessor",
//            "/v3/api-docs",
//            "/swagger-ui"
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getURI().getPath();
//
//        // 1. Whitelist logic
//        if (openApiEndpoints.stream().anyMatch(path::contains)) {
//            return chain.filter(exchange);
//        }
//
//        // 2. Token extraction
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
//        }
//
//        String token = authHeader.substring(7);
//        try {
//            // This will throw an exception if the token is invalid or expired
//            jwtUtil.validateToken(token);
//        } catch (Exception e) {
//            return onError(exchange, "Unauthorized access - Invalid Token", HttpStatus.UNAUTHORIZED);
//        }
//
//        return chain.filter(exchange);
//    }
//
//    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
//        exchange.getResponse().setStatusCode(httpStatus);
//        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        
//        // Optional: Return a JSON error message instead of an empty body
//        String body = "{\"success\": false, \"message\": \"" + err + "\"}";
//        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
//                .bufferFactory().wrap(body.getBytes())));
//    }
//
//    @Override
//    public int getOrder() {
//        return -1; // Run before other filters
//    }
//}