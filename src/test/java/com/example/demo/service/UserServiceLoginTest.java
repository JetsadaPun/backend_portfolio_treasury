package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User loginRequest;

    @BeforeEach
    public void setUp() {
        // Setup User for Repository Response
        testUser = new User();
        testUser.setUserId("user-123");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");
        testUser.setRole("USER");

        // Setup Login Request
        loginRequest = new User();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    public void testLoginSuccess() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn("mockToken");

        // Act
        Map<String, Object> result = userService.login(loginRequest);

        // Assert
        assertTrue((Boolean) result.get("success"));
        assertEquals("Login successful", result.get("message"));
        assertEquals("mockToken", result.get("token"));
        verify(userRepository, times(1)).save(any(User.class)); // Token should be saved
    }

    @Test
    public void testLoginFailure_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act
        Map<String, Object> result = userService.login(loginRequest);

        // Assert
        assertFalse((Boolean) result.get("success"));
        assertEquals("User not found", result.get("error"));
    }

    @Test
    public void testLoginFailure_WrongPassword() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // Act
        Map<String, Object> result = userService.login(loginRequest);

        // Assert
        assertFalse((Boolean) result.get("success"));
        assertEquals("Invalid password", result.get("error"));
    }

    @Test
    public void testLoginFailure_MissingEmail() {
        // Arrange
        User emptyRequest = new User();
        emptyRequest.setPassword("password123");

        // Act
        Map<String, Object> result = userService.login(emptyRequest);

        // Assert
        assertFalse((Boolean) result.get("success"));
        assertEquals("Email and password are required", result.get("error"));
    }
}
