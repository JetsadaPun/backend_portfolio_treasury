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

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> createPost(
            @RequestPart("data") @Valid PostWorkRequest request,
            @RequestPart(value = "images", required = false) org.springframework.web.multipart.MultipartFile[] images,
            @RequestPart(value = "document", required = false) org.springframework.web.multipart.MultipartFile document) {

        PostWork saved = postWorkService.createPostWithImages(request, images, document);

        return ResponseEntity.status(201).body(
                java.util.Map.of("postId", saved.getPostId(), "message", "Created with files"));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postWorkService.getPostById(postId));
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
