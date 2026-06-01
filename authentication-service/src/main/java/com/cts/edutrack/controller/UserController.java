package com.cts.edutrack.controller;
 
import java.util.List;
 
import jakarta.validation.Valid;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import com.cts.edutrack.dto.*;
import com.cts.edutrack.service.UserService;
 
@RestController
@RequestMapping("/api/users")
public class UserController {
 
    @Autowired
    private UserService userService;
 
    // ======================================================
    // REGISTER STUDENT (PUBLIC)
    // ======================================================
    @PostMapping("/registerUser")
    public ResponseEntity<ApiResponse> registerUser(
            @Valid @RequestBody UserRequest request) {
 
        return new ResponseEntity<>(
                new ApiResponse(true,
                        "User registered successfully",
                        userService.registerUser(request, "STUDENT"),
                        HttpStatus.CREATED.value(),
                        null),
                HttpStatus.CREATED
        );
    }
 
    // ======================================================
    // REGISTER PROFESSOR (PUBLIC)
    // ======================================================
    @PostMapping("/registerProfessor")
    public ResponseEntity<ApiResponse> registerProfessor(
            @Valid @RequestBody UserRequest request) {
 
        return new ResponseEntity<>(	
                new ApiResponse(true,
                        "Professor registered successfully",
                        userService.registerUser(request, "INSTRUCTOR"),
                        HttpStatus.CREATED.value(),
                        null),
                HttpStatus.CREATED
        );
    }
 
    // ======================================================
    // ADMIN ONLY - GET ALL USERS
    // ======================================================
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/getUsers")
    public ResponseEntity<ApiResponse> getAllUsers() {
 
        List<UserResponse> users = userService.getAllUsers();
 
        return ResponseEntity.ok(
                new ApiResponse(true,
                        "Users fetched successfully",
                        users,
                        HttpStatus.OK.value(),
                        null)
        );
    }
 
    // ======================================================
    // ADMIN OR INSTRUCTOR - GET USER BY ID
    // ======================================================
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/getUser/{id}")
    public ResponseEntity<ApiResponse> getUserById(
            @PathVariable Long id) {
 
        return ResponseEntity.ok(
                new ApiResponse(true,
                        "User fetched successfully",
                        userService.getUserById(id),
                        HttpStatus.OK.value(),
                        null)
        );
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<ApiResponse> getUserByEmail(
            @PathVariable String email) {
 
        return ResponseEntity.ok(
                new ApiResponse(true,
                        "User fetched successfully",
                        userService.getUserByEmail(email),
                        HttpStatus.OK.value(),
                        null)
        );
    }
 
    // ======================================================
    // ADMIN ONLY - UPDATE USER
    // ======================================================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
 
        return ResponseEntity.ok(
                new ApiResponse(true,
                        "User updated successfully",
                        userService.updateUser(id, request),
                        HttpStatus.OK.value(),
                        null)
        );
    }
 
    // ======================================================
    // ADMIN ONLY - DELETE USER
    // ======================================================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long id) {
 
        userService.deleteUser(id);
 
        return ResponseEntity.ok(
                new ApiResponse(true,
                        "User deleted successfully",
                        null,
                        HttpStatus.OK.value(),
                        null)
        );
    }
    
    
    
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{email}")
    public ResponseEntity<ApiResponse> approveInstructor(
            @PathVariable String email) {
     
        userService.updateAccountStatusByEmail(email, "APPROVED");
     
        return ResponseEntity.ok(
                new ApiResponse(true,
                        "Instructor approved successfully",
                        null,
                        HttpStatus.OK.value(),
                        null)
        );
    }
    
    
    
    
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{email}")
    public ResponseEntity<ApiResponse> rejectInstructor(
            @PathVariable String email) {
     
        userService.updateAccountStatusByEmail(email, "REJECTED");
     
        return ResponseEntity.ok(
                new ApiResponse(true,
                        "Instructor rejected successfully",
                        null,
                        HttpStatus.OK.value(),
                        null)
        );
    }
    
    
 // ======================================================
    // CHANGE PASSWORD (ALL ROLES)
    // ======================================================
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @PutMapping("/changePassword")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(request);

        return ResponseEntity.ok(
                new ApiResponse(true,
                        "Password updated successfully",
                        null,
                        HttpStatus.OK.value(),
                        null)
        );
    }
}