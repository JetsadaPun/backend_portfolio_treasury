package com.example.demo.service;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.LikeRequest;
import com.example.demo.model.ActionPost;
import com.example.demo.repository.ActionPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActionPostService {

    private final ActionPostRepository actionPostRepository;

    public ActionPostService(ActionPostRepository actionPostRepository) {
        this.actionPostRepository = actionPostRepository;
    }

    @Transactional
    public ActionPost addComment(CommentRequest request) {
        ActionPost comment = new ActionPost();
        comment.setPostId(request.getPostId());
        comment.setActionType("comment");
        comment.setContent(request.getContent());
        comment.setUserId(request.getUserId());
        comment.setUserName(request.getUserName());
        comment.setParentId(request.getParentId());
        comment.setStatus("active");
        return actionPostRepository.save(comment);
    }

    @Transactional
    public void toggleLike(LikeRequest request) {
        java.util.Optional<ActionPost> existing = actionPostRepository
                .findByPostIdAndUserIdAndActionType(request.getPostId(), request.getUserId(), "like");

        if (existing.isPresent()) {
            actionPostRepository.delete(existing.get());
        } else {
            ActionPost like = new ActionPost();
            like.setPostId(request.getPostId());
            like.setUserId(request.getUserId());
            like.setUserName(request.getUserName());
            like.setActionType("like");
            like.setStatus("active");
            actionPostRepository.save(like);
        }
    }

    public long getLikeCount(Long postId) {
        return actionPostRepository.countByPostIdAndActionType(postId, "like");
    }

    public boolean isLikedByUser(Long postId, String userId) {
        return actionPostRepository.findByPostIdAndUserIdAndActionType(postId, userId, "like").isPresent();
    }

    public List<ActionPost> getCommentsByPost(Long postId) {
        return actionPostRepository.findByPostIdAndActionTypeAndStatus(postId, "comment", "active");
    }
}
