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
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<String> getAllUserNames() {
        return userRepository.findAll()
                .stream()
                .map(User::getName)
                .toList();
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> response = new HashMap<>();

        // Normalize input
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setName(user.getName().trim());

        // validate
        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("error", "Email is already registered");
            response.put("success", false);
            return response;
        }

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set default role and status
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        if (user.getStatus() == null || user.getStatus().isEmpty()) {
            user.setStatus("ACTIVE");
        }

        // save โดย JPA
        User savedUser = userRepository.save(user);

        response.put("message", "User created successfully");
        response.put("userId", savedUser.getId());
        response.put("success", true);
        response.put("user", Map.of(
                "id", savedUser.getId(),
                "name", savedUser.getName(),
                "email", savedUser.getEmail()));

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
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                try {
                    String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId(), user.getName());
                    user.setTokenId(token);
                    userRepository.save(user);

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("id", user.getId());
                    userData.put("name", user.getName());
                    userData.put("email", user.getEmail());
                    userData.put("role", user.getRole());

                    response.put("token", token);
                    response.put("message", "Login successful");
                    response.put("success", true);
                    response.put("user", userData);
                } catch (Exception e) {
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

        return userRepository.findById(userId).map(existingUser -> {
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName().trim());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail().trim().toLowerCase());
            }
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }
            if (updatedUser.getStatus() != null) {
                existingUser.setStatus(updatedUser.getStatus());
            }
            if (updatedUser.getTokenId() != null) {
                existingUser.setTokenId(updatedUser.getTokenId());
            }
            if (updatedUser.getPhone() != null) {
                existingUser.setPhone(updatedUser.getPhone());
            }
            if (updatedUser.getProfileImage() != null) {
                existingUser.setProfileImage(updatedUser.getProfileImage());
            }
            if (updatedUser.getDob() != null) {
                existingUser.setDob(updatedUser.getDob());
            }
            if (updatedUser.getEducation() != null) {
                existingUser.setEducation(updatedUser.getEducation());
            }
            if (updatedUser.getBio() != null) {
                existingUser.setBio(updatedUser.getBio());
            }
            if (updatedUser.getStudentId() != null) {
                existingUser.setStudentId(updatedUser.getStudentId());
            }

            userRepository.save(existingUser);

            response.put("message", "User updated successfully");
            response.put("success", true);
            return response;
        }).orElseGet(() -> {
            response.put("error", "User not found");
            response.put("success", false);
            return response;
        });
    }

    public List<User> searchUsersByName(String query) {
        return userRepository.findByNameContainingIgnoreCase(query);
    }

    public Optional<User> findByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }
}
