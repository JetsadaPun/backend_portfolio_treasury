package com.example.demo.repository;

import com.example.demo.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByPost_PostIdAndStatus(Long postId, String status);
    Optional<ProjectMember> findByPost_PostIdAndUser_UserId(Long postId, String userId);
    boolean existsByPost_PostIdAndUser_UserId(Long postId, String userId);
}