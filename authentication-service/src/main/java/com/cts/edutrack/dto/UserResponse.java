package com.cts.edutrack.dto;
 
import lombok.Data;
 
@Data
public class UserResponse {
 
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String role;
    private String accountStatus;
}