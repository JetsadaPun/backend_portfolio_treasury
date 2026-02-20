package com.example.demo.repository;

import com.example.demo.model.ActionPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActionPostRepository extends JpaRepository<ActionPost, Long> {
    List<ActionPost> findByPostIdAndActionTypeAndStatus(Long postId, String actionType, String status);

    java.util.Optional<ActionPost> findByPostIdAndUserIdAndActionType(Long postId, String userId, String actionType);

    long countByPostIdAndActionType(Long postId, String actionType);
}
