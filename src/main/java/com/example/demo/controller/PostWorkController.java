package com.example.demo.controller;

import com.example.demo.dto.PostWorkRequest;
import com.example.demo.dto.PostWorkUpdateRequest;
import com.example.demo.model.PostWork;
import com.example.demo.service.PostWorkService;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid; // ถ้าจะใช้ validation
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/posts")
public class PostWorkController {

    private PostWorkService postWorkService;

    public PostWorkController(PostWorkService postWorkService) {
        this.postWorkService = postWorkService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostWorkRequest request) {
        PostWork saved = postWorkService.createPost(request);
        return ResponseEntity.status(201).body(
                java.util.Map.of("postId", saved.getPostId(), "message", "Created")
        );
    }
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePostFull(@PathVariable Long postId,
                                            @Valid @RequestBody PostWorkRequest request) {
        PostWork updated = postWorkService.updatePostFull(postId, request);
        return ResponseEntity.ok(
                java.util.Map.of("postId", updated.getPostId(), "message", "Updated")
        );
    }

    // PATCH: อัปเดตบางฟิลด์
    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePostPartial(@PathVariable Long postId,
                                               @RequestBody PostWorkUpdateRequest request) {
        PostWork updated = postWorkService.updatePostPartial(postId, request);
        return ResponseEntity.ok(
                java.util.Map.of("postId", updated.getPostId(), "message", "Patched")
        );
    }

}
