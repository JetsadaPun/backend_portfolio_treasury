package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public int saveUser(User user) {
        // Generate UUID if not set
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String sql = "INSERT INTO users (user_id, name, email, password, role, status, token_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getId(),
                user.getName(),
                user.getEmail(),
                encodedPassword,
                user.getRole(),
                user.getStatus(),
                user.getTokenId()
        );
    }

    public List<String> findAllNames() {
        String sql = "SELECT name FROM users";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        List<User> users = jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getString("user_id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setStatus(rs.getString("status"));
            user.setTokenId(rs.getString("token_id"));
            return user;
        });

        return users.stream().findFirst();
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public int updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ?, status = ?, token_id = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getTokenId(),
                user.getId()
        );
    }

    public int updatePassword(String userId, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql, encodedPassword, userId);
    }
}
