package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.model.ActionPost;
import com.example.demo.service.ActionPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/actions")
public class ActionPostController {

    private final ActionPostService actionPostService;

    public ActionPostController(ActionPostService actionPostService) {
        this.actionPostService = actionPostService;
    }

    // เพิ่มคอมเมนต์
    @PostMapping("/comment")
    public ResponseEntity<ActionPost> addComment(@Valid @RequestBody CommentRequest request) {
        ActionPost saved = actionPostService.addComment(request);
        return ResponseEntity.status(201).body(saved);
    }

    // ดูคอมเมนต์ของโพสต์
    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<ActionPost>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(actionPostService.getCommentsByPost(postId));
    }
}
