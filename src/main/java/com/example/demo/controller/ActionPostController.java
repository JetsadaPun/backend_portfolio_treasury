package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.LikeRequest;
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

    // จัดการการไลค์ (ย้อนกลับได้)
    @PostMapping("/like")
    public ResponseEntity<Void> toggleLike(@Valid @RequestBody LikeRequest request) {
        actionPostService.toggleLike(request);
        return ResponseEntity.ok().build();
    }

    // ดูจำนวนไลค์
    @GetMapping("/like/count/{postId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        return ResponseEntity.ok(actionPostService.getLikeCount(postId));
    }

    // เช็คว่าผู้ใช้ไลค์หรือยัง
    @GetMapping("/like/status/{postId}")
    public ResponseEntity<Boolean> getLikeStatus(@PathVariable Long postId, @RequestParam String userId) {
        return ResponseEntity.ok(actionPostService.isLikedByUser(postId, userId));
    }
}
