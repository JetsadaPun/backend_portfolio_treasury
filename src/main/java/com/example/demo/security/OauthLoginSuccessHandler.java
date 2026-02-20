package com.example.demo.security;

import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OauthLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @org.springframework.beans.factory.annotation.Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public OauthLoginSuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = String.valueOf(principal.getAttributes().get("email")).toLowerCase();

        userRepository.findByEmail(email).ifPresent(user -> {
            String role = user.getRole() != null ? user.getRole() : "USER";
            String token = jwtUtil.generateToken(email, role, user.getUserId(), user.getName());

            user.setTokenId(token);
            userRepository.save(user);

            try {
                response.sendRedirect(frontendBaseUrl + "/oauth-success?token=" + token);
            } catch (Exception e) {
                System.err.println("Error redirecting after OAuth success: " + e.getMessage());
            }
        });
    }
}
