package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UnifiedOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UnifiedOauth2UserService(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) {
        OAuth2User oauthUser = super.loadUser(req);

        String provider = req.getClientRegistration().getRegistrationId(); // "google" | "facebook" ...
        Map<String, Object> attrs = oauthUser.getAttributes();

        // ดึง email/name ตาม provider
        String email = null;
        String name  = null;

        final String finalName = name;

        if ("google".equals(provider)) {
            email = (String) attrs.get("email");
            name  = (String) attrs.getOrDefault("name", email);
        } else if ("facebook".equals(provider)) {
            email = (String) attrs.get("email");
            name  = (String) attrs.getOrDefault("name", email);
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required from provider: " + provider);
        }

        final String normalized = email.trim().toLowerCase();

        // upsert ผู้ใช้ในระบบคุณ
        userRepository.findByEmail(normalized).ifPresentOrElse(existing -> {
            if (finalName != null && !finalName.equals(existing.getName())) {
                existing.setName(finalName);
                userRepository.save(existing);
            }
        }, () -> {
            User u = new User();
            u.setEmail(normalized);
            u.setName(finalName != null ? finalName : normalized);
            u.setPassword(passwordEncoder.encode("oauth:" + normalized));
            u.setStatus("active");
            if (u.getRole() == null) u.setRole("USER");
            userRepository.save(u);
        });

        return oauthUser; // ส่งต่อให้ Security จัดการ principal
    }
}
