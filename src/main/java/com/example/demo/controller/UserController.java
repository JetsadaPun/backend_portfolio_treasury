package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
        String email = principal.getName();
        return userService.findByEmail(email)
                .map(user -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", user.getId());
                    data.put("name", user.getName());
                    data.put("email", user.getEmail());
                    data.put("phone", user.getPhone());
                    data.put("profileImage", user.getProfileImage());
                    data.put("dob", user.getDob());
                    data.put("education", user.getEducation());
                    data.put("bio", user.getBio());
                    data.put("studentId", user.getStudentId());
                    data.put("role", user.getRole());
                    return ResponseEntity.ok(data);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<Map<String, String>> getUserProfile(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "phone", user.getPhone() != null ? user.getPhone() : "",
                        "profileImage", user.getProfileImage() != null ? user.getProfileImage() : "",
                        "dob", user.getDob() != null ? user.getDob() : "",
                        "education", user.getEducation() != null ? user.getEducation() : "",
                        "bio", user.getBio() != null ? user.getBio() : "",
                        "studentId", user.getStudentId() != null ? user.getStudentId() : "")))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody User updatedUser) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
        String email = principal.getName();
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(userService.updateUser(user.getId(), updatedUser)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        List<User> users = userService.searchUsersByName(query);
        return ResponseEntity.ok(users.stream().map(user -> Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "studentId", user.getStudentId() != null ? user.getStudentId() : "",
                "profileImage", user.getProfileImage() != null ? user.getProfileImage() : "")).toList());
    }

    @GetMapping("/{idOrStudentId}")
    public ResponseEntity<?> getUserByIdOrStudentId(@PathVariable String idOrStudentId) {
        // Try finding by studentId first
        var userOpt = userService.findByStudentId(idOrStudentId);

        // If not found, try finding by userId (UUID)
        if (userOpt.isEmpty()) {
            userOpt = userService.findById(idOrStudentId);
        }

        return userOpt.map(user -> ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "studentId", user.getStudentId() != null ? user.getStudentId() : "",
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "profileImage", user.getProfileImage() != null ? user.getProfileImage() : "",
                "dob", user.getDob() != null ? user.getDob() : "",
                "education", user.getEducation() != null ? user.getEducation() : "",
                "bio", user.getBio() != null ? user.getBio() : "")))
                .orElse(ResponseEntity.notFound().build());
    }
}