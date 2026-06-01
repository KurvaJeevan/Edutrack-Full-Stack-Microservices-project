package com.cts.edutrack.config;
 
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import com.cts.edutrack.model.User;
import com.cts.edutrack.repository.UserRepository;
 
@Configuration
public class DataInitializer implements CommandLineRunner {
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
 
    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
 
    @Override
    public void run(String... args) {
 
        if (!userRepository.existsByEmail("admin@gmail.com")) {
 
            User admin = new User();
 
            admin.setUserName("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setPhoneNumber("9999999999");
            admin.setRole("ADMIN");
            admin.setAccountStatus("APPROVED");
 
            userRepository.save(admin);
 
            System.out.println("Admin created successfully!");
        }
    }
}