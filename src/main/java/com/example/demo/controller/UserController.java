package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

// อย่าลืม import User กับ UserRepository และ JwtUtil
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<String> getUsers() {
        return userService.getAllUserNames();
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        Map<String, String> response = userService.register(user);
        if (response.containsKey("error")) {
            return ResponseEntity.status(400).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> response = userService.login(user);

        if (response.containsKey("token")) {
            return ResponseEntity.ok(response);
        } else if ("Invalid password".equals(response.get("error"))) {
            return ResponseEntity.status(401).body(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
}
