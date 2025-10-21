package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // เพิ่มถ้าต้องการให้ frontend เรียกใช้ได้
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getUsers() {
        try {
            List<String> users = userService.getAllUserNames();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody User user) {
        Map<String, Object> response = userService.register(user);

        if (response.containsKey("error")) {
            return ResponseEntity.badRequest().body(response);
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
        } else if ("User not found".equals(response.get("error"))) {
            return ResponseEntity.status(404).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // เพิ่ม endpoint สำหรับดู user profile
    @GetMapping("/profile/{email}")
    public ResponseEntity<Map<String, String>> getUserProfile(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId().toString(),
                        "name", user.getName(),
                        "email", user.getEmail()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}