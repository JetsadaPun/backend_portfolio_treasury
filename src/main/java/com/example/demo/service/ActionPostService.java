package com.example.demo.service;

import com.example.demo.dto.CommentRequest;
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
        comment.setStatus("active");
        return actionPostRepository.save(comment);
    }

    public List<ActionPost> getCommentsByPost(Long postId) {
        return actionPostRepository.findByPostIdAndActionTypeAndStatus(postId, "comment", "active");
    }
}
