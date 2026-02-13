package com.example.demo.repository;

import com.example.demo.model.VerifyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerifyUserRepository extends JpaRepository<VerifyUser, UUID> {
    Optional<VerifyUser> findTopByEmailIgnoreCaseAndStatusOrderByRequestedAtDesc(String email, String status);
}