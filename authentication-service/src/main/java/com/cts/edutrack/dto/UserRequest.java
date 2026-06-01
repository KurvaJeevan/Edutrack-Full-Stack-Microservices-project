package com.cts.edutrack.dto;
 
import jakarta.validation.constraints.*;
import lombok.Data;
 
@Data
public class UserRequest {
 
    @NotBlank(message = "Username is required")
    private String userName;
 
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
 
    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must be exactly 10 digits"
    )
    private String phoneNumber;
 
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}