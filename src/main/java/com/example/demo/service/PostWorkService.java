package com.example.demo.service;

import com.example.demo.dto.PostWorkRequest;
import com.example.demo.dto.PostWorkUpdateRequest;
import com.example.demo.model.PostWork;
import com.example.demo.model.User;
import com.example.demo.repository.PostWorkRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostWorkService {

    private final UserRepository userRepository;
    private final PostWorkRepository postWorkRepository;

    public PostWorkService(UserRepository userRepository,
                           PostWorkRepository postWorkRepository) {
        this.userRepository = userRepository;
        this.postWorkRepository = postWorkRepository;
    }

    @Transactional
    public PostWork createPost(PostWorkRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostWork post = new PostWork();
        post.setSubjectId(request.getSubjectId());
        post.setProjectName(request.getProjectName());
        post.setProjectDetail(request.getProjectDetail());
        post.setProjectImage(request.getProjectImage());
        post.setProjectLink(request.getProjectLink());
        post.setProjectDocs(request.getProjectDocs());
        post.setUser(user);

        return postWorkRepository.save(post);
    }

    @Transactional
    public PostWork updatePostFull(Long postId, PostWorkRequest request) {
        PostWork post = postWorkRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // (ออปชัน) ตรวจว่า userId ที่ส่งมาเป็นเจ้าของโพสต์หรือไม่
        if (request.getUserId() != null) {
            if (!post.getUser().getId().equals(request.getUserId())) {
                throw new SecurityException("You are not the owner of this post");
            }
        }

        // แทนค่าทั้งหมด (PUT)
        post.setSubjectId(request.getSubjectId());
        post.setProjectName(request.getProjectName());
        post.setProjectDetail(request.getProjectDetail());
        post.setProjectImage(request.getProjectImage());
        post.setProjectLink(request.getProjectLink());
        post.setProjectDocs(request.getProjectDocs());

        // updatedAt จะถูกเซ็ตเองใน @PreUpdate
        return postWorkRepository.save(post);
    }

    @Transactional
    public PostWork updatePostPartial(Long postId, PostWorkUpdateRequest req) {
        PostWork post = postWorkRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // (ออปชัน) ตรวจว่าเป็นเจ้าของ
        if (req.getUserId() != null) {
            if (!post.getUser().getId().equals(req.getUserId())) {
                throw new SecurityException("You are not the owner of this post");
            }
        }

        // อัปเดตเฉพาะฟิลด์ที่ส่งมา (PATCH)
        if (req.getSubjectId() != null) post.setSubjectId(req.getSubjectId());
        if (req.getProjectName() != null) post.setProjectName(req.getProjectName());
        if (req.getProjectDetail() != null) post.setProjectDetail(req.getProjectDetail());
        if (req.getProjectImage() != null) post.setProjectImage(req.getProjectImage());
        if (req.getProjectLink() != null) post.setProjectLink(req.getProjectLink());
        if (req.getProjectDocs() != null) post.setProjectDocs(req.getProjectDocs());

        return postWorkRepository.save(post);
    }
}