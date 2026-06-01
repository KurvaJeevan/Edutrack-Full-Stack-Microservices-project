package com.cts.edutrack.model;


import jakarta.persistence.*;
import lombok.*;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
 
    private String userName;
 
    @Column(unique = true, nullable = false)
    private String email;
 
    private String phoneNumber;
 
    private String password;
 
    private String role;
    
    private String accountStatus;
}