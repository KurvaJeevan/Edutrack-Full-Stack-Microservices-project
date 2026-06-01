package com.cts.edutrack.controller;
 
import jakarta.validation.Valid;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
 
import com.cts.edutrack.dto.*;
import com.cts.edutrack.service.UserService;
 
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 
    @Autowired
    private UserService userService;
 
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request) {
 
        LoginResponse response =
                userService.login(request);
 
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Login successful",
                        response,
                        HttpStatus.OK.value(),
                        null
                )
        );
    }
}