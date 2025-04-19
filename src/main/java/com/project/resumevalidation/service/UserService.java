//package com.project.resumevalidation.service;
//
//import java.util.Optional;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.project.resumevalidation.dto.User;
//import com.project.resumevalidation.repository.UserRepository;
//
//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder; // Assuming you're using BCrypt or another encoder
//
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public boolean authenticateUser(String username, String password) {
//        // Retrieve user by username
//        Optional<User> user = userRepository.findByUsername(username);
//        
//        if (user.isPresent()) {
//            // Compare the password with the hashed password stored in the database
//            return passwordEncoder.matches(password, user.get().getPassword());
//        }
//        
//        return false;
//    }
//}
