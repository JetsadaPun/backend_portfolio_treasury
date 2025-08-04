package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Email pattern สำหรับ validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<String> getAllUserNames() {
        return userRepository.findAllNames();
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> response = new HashMap<>();

        // Validate input
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            response.put("error", "Name is required");
            response.put("success", false);
            return response;
        }

        if (user.getName().trim().length() < 2) {
            response.put("error", "Name must be at least 2 characters");
            response.put("success", false);
            return response;
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            response.put("error", "Email is required");
            response.put("success", false);
            return response;
        }

        if (!isValidEmail(user.getEmail().trim())) {
            response.put("error", "Please provide a valid email address");
            response.put("success", false);
            return response;
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            response.put("error", "Password must be at least 6 characters");
            response.put("success", false);
            return response;
        }

        // Normalize email (lowercase และ trim)
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setName(user.getName().trim());

        // ตรวจสอบ email ซ้ำ
        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("error", "Email is already registered");
            response.put("success", false);
            return response;
        }

        try {
            int result = userRepository.saveUser(user);

            if (result > 0) {
                response.put("message", "User created successfully");
                response.put("userId", user.getId());
                response.put("success", true);

                // ส่ง user info กลับ (ไม่รวม password)
                response.put("user", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail()
                ));
            } else {
                response.put("error", "Failed to create user");
                response.put("success", false);
            }
        } catch (Exception e) {
            response.put("error", "Registration failed: " + e.getMessage());
            response.put("success", false);
        }

        return response;
    }

    public Map<String, Object> login(User loginRequest) {
        Map<String, Object> response = new HashMap<>();

        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            response.put("error", "Email and password are required");
            response.put("success", false);
            return response;
        }

        String email = loginRequest.getEmail().trim().toLowerCase();

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Password from request (raw): " + loginRequest.getPassword());
            System.out.println("Password from DB (encoded): " + user.getPassword());
            System.out.println("Password match: " + passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));

            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                System.out.println("Password matched, generating token...");
                try {
                    String token = jwtUtil.generateToken(user.getEmail());
                    System.out.println("Token generated: " + token);
                    user.setTokenId(token);
                    userRepository.updateUser(user);

                    response.put("token", token);
                    response.put("message", "Login successful");
                    response.put("success", true);
                    response.put("user", Map.of(
                            "id", user.getId(),
                            "name", user.getName(),
                            "email", user.getEmail()
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                    response.put("error", "Token generation failed");
                    response.put("success", false);
                }
            } else {
                response.put("error", "Invalid password");
                response.put("success", false);
            }
        } else {
            response.put("error", "User not found");
            response.put("success", false);
        }

        return response;
    }

    // เพิ่ม methods อื่นๆ
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public Map<String, Object> updateUser(String userId, User updatedUser) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate และ normalize ข้อมูล
            if (updatedUser.getName() != null) {
                updatedUser.setName(updatedUser.getName().trim());
            }
            if (updatedUser.getEmail() != null) {
                updatedUser.setEmail(updatedUser.getEmail().trim().toLowerCase());
            }

            updatedUser.setId(userId);
            int result = userRepository.updateUser(updatedUser);

            if (result > 0) {
                response.put("message", "User updated successfully");
                response.put("success", true);
            } else {
                response.put("error", "User not found");
                response.put("success", false);
            }
        } catch (Exception e) {
            response.put("error", "Update failed: " + e.getMessage());
            response.put("success", false);
        }

        return response;
    }
}