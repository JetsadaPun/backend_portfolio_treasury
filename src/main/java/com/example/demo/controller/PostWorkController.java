package com.example.demo.controller;

import com.example.demo.dto.PostWorkRequest;
import com.example.demo.dto.PostWorkUpdateRequest;
import com.example.demo.model.PostWork;
import com.example.demo.service.PostWorkService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostWorkController {

    private final PostWorkService postWorkService;

    public PostWorkController(PostWorkService postWorkService) {
        this.postWorkService = postWorkService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostWorkRequest request) {
        PostWork saved = postWorkService.createPost(request);
        return ResponseEntity.status(201).body(
                java.util.Map.of("postId", saved.getPostId(), "message", "Created"));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePostFull(@PathVariable Long postId,
            @Valid @RequestBody PostWorkRequest request) {
        PostWork updated = postWorkService.updatePostFull(postId, request);
        return ResponseEntity.ok(
                java.util.Map.of("postId", updated.getPostId(), "message", "Updated"));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePostPartial(@PathVariable Long postId,
            @RequestBody PostWorkUpdateRequest request) {
        PostWork updated = postWorkService.updatePostPartial(postId, request);
        return ResponseEntity.ok(
                java.util.Map.of("postId", updated.getPostId(), "message", "Patched"));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<?> getPostsBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(postWorkService.getPostsBySubject(subjectId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(postWorkService.getPostsByUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(@RequestParam String query) {
        return ResponseEntity.ok(postWorkService.searchPostsByProjectName(query));
    }
}
