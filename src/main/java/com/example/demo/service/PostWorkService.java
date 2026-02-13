package com.example.demo.service;

import com.example.demo.dto.PostWorkRequest;
import com.example.demo.dto.PostWorkResponse;
import com.example.demo.dto.PostWorkUpdateRequest;
import com.example.demo.model.PostWork;
import com.example.demo.model.User;
import com.example.demo.repository.PostWorkRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostWorkService {

    private final UserRepository userRepository;
    private final PostWorkRepository postWorkRepository;
    private final SubjectRepository subjectRepository;

    public PostWorkService(UserRepository userRepository,
            PostWorkRepository postWorkRepository,
            SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.postWorkRepository = postWorkRepository;
        this.subjectRepository = subjectRepository;
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

        if (request.getUserId() != null) {
            if (!post.getUser().getUserId().equals(request.getUserId())) {
                throw new SecurityException("You are not the owner of this post");
            }
        }

        post.setSubjectId(request.getSubjectId());
        post.setProjectName(request.getProjectName());
        post.setProjectDetail(request.getProjectDetail());
        post.setProjectImage(request.getProjectImage());
        post.setProjectLink(request.getProjectLink());
        post.setProjectDocs(request.getProjectDocs());

        return postWorkRepository.save(post);
    }

    @Transactional
    public PostWork updatePostPartial(Long postId, PostWorkUpdateRequest req) {
        PostWork post = postWorkRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (req.getUserId() != null) {
            if (!post.getUser().getUserId().equals(req.getUserId())) {
                throw new SecurityException("You are not the owner of this post");
            }
        }

        if (req.getSubjectId() != null)
            post.setSubjectId(req.getSubjectId());
        if (req.getProjectName() != null)
            post.setProjectName(req.getProjectName());
        if (req.getProjectDetail() != null)
            post.setProjectDetail(req.getProjectDetail());
        if (req.getProjectImage() != null)
            post.setProjectImage(req.getProjectImage());
        if (req.getProjectLink() != null)
            post.setProjectLink(req.getProjectLink());
        if (req.getProjectDocs() != null)
            post.setProjectDocs(req.getProjectDocs());

        return postWorkRepository.save(post);
    }

    public List<PostWorkResponse> getPostsBySubject(Long subjectId) {
        return postWorkRepository.findBySubjectId(subjectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PostWorkResponse> getPostsByUser(String userId) {
        return postWorkRepository.findByUser_UserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PostWorkResponse> searchPostsByProjectName(String query) {
        return postWorkRepository.findByProjectNameContainingIgnoreCase(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PostWorkResponse mapToResponse(PostWork post) {
        PostWorkResponse resp = new PostWorkResponse();
        resp.setPostId(post.getPostId());
        resp.setSubjectId(post.getSubjectId() != null ? post.getSubjectId().longValue() : null);
        resp.setProjectName(post.getProjectName());
        resp.setProjectDetail(post.getProjectDetail());
        resp.setProjectImage(post.getProjectImage());
        resp.setProjectLink(post.getProjectLink());
        resp.setProjectDocs(post.getProjectDocs());
        resp.setCreatedAt(post.getCreatedAt());

        if (post.getUser() != null) {
            resp.setUserId(post.getUser().getUserId());
            resp.setUserName(post.getUser().getName());
        }

        if (post.getSubjectId() != null) {
            subjectRepository.findById(post.getSubjectId().longValue()).ifPresent(s -> {
                resp.setSubjectNameId(s.getSubjectNameId());
                resp.setSubjectNameTh(s.getSubjectNameTh());
            });
        }

        return resp;
    }
}