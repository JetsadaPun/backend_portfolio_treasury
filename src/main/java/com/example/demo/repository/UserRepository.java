package com.example.demo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findAllNames() {
        String sql = "SELECT name FROM users";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
