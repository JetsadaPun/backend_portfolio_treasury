package com.example.demo.repository;

import com.example.demo.model.PostWork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostWorkRepository extends JpaRepository<PostWork, Long> {
    java.util.List<PostWork> findBySubjectId(Long subjectId);

    java.util.List<PostWork> findByUser_UserId(String userId);

    java.util.List<PostWork> findByProjectNameContainingIgnoreCase(String projectName);
}
