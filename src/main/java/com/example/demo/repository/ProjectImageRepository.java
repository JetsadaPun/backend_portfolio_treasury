package com.example.demo.repository;

import com.example.demo.model.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    List<ProjectImage> findByPostId(Long postId);

    void deleteByPostId(Long postId);
}
