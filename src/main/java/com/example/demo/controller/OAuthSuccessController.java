package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OAuthSuccessController {

    @GetMapping("/oauth-success")
    public ResponseEntity<?> success(@RequestParam String token) {
        return ResponseEntity.ok(
                java.util.Map.of("message", "OAuth login successful", "token", token)
        );
    }
}
