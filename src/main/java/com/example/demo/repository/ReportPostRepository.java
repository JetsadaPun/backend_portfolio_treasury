package com.example.demo.repository;

import com.example.demo.model.ReportPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
    List<ReportPost> findAllByOrderByCreatedAtDesc();
}
