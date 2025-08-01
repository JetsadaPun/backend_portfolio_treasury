package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<String> getAllUserNames() {
        return userRepository.findAllNames();
    }

    public Map<String, String> register(User user) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("error", "Email is already registered");
            return response;
        }

        int result = userRepository.saveUser(user);

        if (result > 0) {
            response.put("message", "User created successfully");
        } else {
            response.put("error", "Failed to create user");
        }

        return response;
    }

    public Map<String, Object> login(User loginRequest) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getEmail());

                response.put("token", token);
                response.put("email", user.getEmail());
            } else {
                response.put("error", "Invalid password");
            }
        } else {
            response.put("error", "User not found");
        }

        return response;
    }
}
