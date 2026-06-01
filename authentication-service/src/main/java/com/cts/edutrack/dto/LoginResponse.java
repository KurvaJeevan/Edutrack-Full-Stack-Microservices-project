package com.cts.edutrack.dto;
 
import lombok.*;
 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
 
    private String token;
    private String role;
}