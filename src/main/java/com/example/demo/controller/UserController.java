package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<String> getUsers() {
        return userRepository.findAllNames();
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        int result = userRepository.saveUser(user);
        Map<String, String> response = new HashMap<>();
        if (result > 0) {
            response.put("message", "User created successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Failed to create user");
            return ResponseEntity.status(400).body(response);
        }
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        Map<String, Object> response = new HashMap<>();

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPassword().equals(loginRequest.getPassword())) {
                response.put("status", "success");
                response.put("user", user); // ส่ง user กลับทั้ง object
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }
}
