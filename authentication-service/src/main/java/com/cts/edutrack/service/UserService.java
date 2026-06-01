package com.cts.edutrack.service;
 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
import com.cts.edutrack.dto.*;
import com.cts.edutrack.exception.*;
import com.cts.edutrack.feignClient.EnrollmentClient;
import com.cts.edutrack.model.User;
//import com.cts.edutrack.repository.AttendanceRepository;
import com.cts.edutrack.repository.UserRepository;
import com.cts.edutrack.security.JwtUtil;
 
@Service
public class UserService {
 
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private ModelMapper modelMapper;
 
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    @Autowired
    private JwtUtil jwtUtil;
    
    
    @Autowired
    private EnrollmentClient enrollmentClient;
    
//    @Autowired
//    private AttendanceService attendanceService;
 
    // ======================================================
    // REGISTER USER (ROLE SET INTERNALLY)
    // ======================================================
    public UserResponse registerUser(UserRequest request, String role) {
 
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already registered");
        }
 
        // Map DTO to Entity
        User user = modelMapper.map(request, User.class);
 
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));
 
        // Set role internally (STUDENT / INSTRUCTOR / ADMIN)
        user.setRole(role);
        
        if (role.equals("INSTRUCTOR")) {
            user.setAccountStatus("PENDING");
        } else {
            user.setAccountStatus("APPROVED");
        }
 
        User savedUser = userRepository.save(user);
 
        return modelMapper.map(savedUser, UserResponse.class);
    }
 
    // ======================================================
    // LOGIN (AUTHENTICATION + JWT TOKEN GENERATION)
    // ======================================================
    public LoginResponse login(LoginRequest request) {
    	 
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new NotFoundException("User not found"));
     
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if ("PENDING".equals(user.getAccountStatus())) {
            throw new RuntimeException(
                    "Your registration is pending admin approval.");
        }
     
        if ("REJECTED".equals(user.getAccountStatus())) {
            throw new RuntimeException(
                    "Admin rejected your registration.");
        }
     
        // Call attendance service
//        attendanceService.markAttendance(user.getUserId());
        enrollmentClient.markAttendance(user.getUserId());
     
        String token = jwtUtil.generateToken(user);
     
        return new LoginResponse(token, user.getRole());
    }
 
    // ======================================================
    // CREATE USER 
    // ======================================================
    public UserResponse createUser(UserRequest request, String role) {
 
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already registered");
        }
 
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
 
        return modelMapper.map(
                userRepository.save(user),
                UserResponse.class
        );
    }
 
    // ======================================================
    // GET ALL USERS
    // ======================================================
    public List<UserResponse> getAllUsers() {
 
        return userRepository.findAll()
                .stream()
                .map(user ->
                        modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }
 
    // ======================================================
    // GET USER BY ID
    // ======================================================
    public UserResponse getUserById(Long id) {
 
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "User not found with id: " + id));
 
        return modelMapper.map(user, UserResponse.class);
    }
 
    // ======================================================
    // UPDATE USER
    // ======================================================
    public UserResponse updateUser(Long id, UserRequest request) {
 
        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "User not found with id: " + id));
 
        // Update fields
        existingUser.setUserName(request.getUserName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhoneNumber(request.getPhoneNumber());
 
        // Encrypt updated password
        existingUser.setPassword(
                passwordEncoder.encode(request.getPassword()));
 
        return modelMapper.map(
                userRepository.save(existingUser),
                UserResponse.class
        );
    }
 
    // ======================================================
    // DELETE USER
    // ======================================================
    public void deleteUser(Long id) {
 
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "User not found with id: " + id));
 
        userRepository.delete(user);
    }
    
    
 // ========================================
 // UPDATE ACCOUNT STATUS BY EMAIL (ADMIN)
 // ========================================
 public void updateAccountStatusByEmail(String email, String status) {
  
     User user = userRepository.findByEmail(email)
             .orElseThrow(() ->
                     new NotFoundException("User not found with email: " + email));
  
     user.setAccountStatus(status);
  
     userRepository.save(user);
 }

 public User getUserByEmail(String email) {
	// TODO Auto-generated method stub
	return userRepository.findByEmail(email).get();
 }
 
 
//======================================================
 // CHANGE PASSWORD
 // ======================================================
 public void changePassword(ChangePasswordRequest request) {
     
     User user = userRepository.findById(request.getUserId())
             .orElseThrow(() -> 
                     new NotFoundException("User not found with id: " + request.getUserId()));

     // Verify the current password
     if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
         throw new RuntimeException("Incorrect current password.");
     }

     // Encrypt and set the new password
     user.setPassword(passwordEncoder.encode(request.getNewPassword()));
     
     userRepository.save(user);
 }
}