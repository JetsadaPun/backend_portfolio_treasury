package com.example.demo.service;

import com.example.demo.dto.PostWorkRequest;
import com.example.demo.dto.PostWorkResponse;
import com.example.demo.dto.PostWorkUpdateRequest;
import com.example.demo.model.PostWork;
import com.example.demo.model.ProjectImage;
import com.example.demo.model.User;
import com.example.demo.repository.PostWorkRepository;
import com.example.demo.repository.ProjectImageRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PostWorkService {

    private final UserRepository userRepository;
    private final PostWorkRepository postWorkRepository;
    private final SubjectRepository subjectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final FileStorageService fileStorageService;

    public PostWorkService(UserRepository userRepository,
            PostWorkRepository postWorkRepository,
            SubjectRepository subjectRepository,
            ProjectImageRepository projectImageRepository,
            FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.postWorkRepository = postWorkRepository;
        this.subjectRepository = subjectRepository;
        this.projectImageRepository = projectImageRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public PostWork createPostWithImages(PostWorkRequest request, MultipartFile[] images, MultipartFile document) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostWork post = new PostWork();
        post.setSubjectId(request.getSubjectId());
        post.setProjectName(request.getProjectName());
        post.setProjectDetail(request.getProjectDetail());
        post.setProjectLink(request.getProjectLink());
        post.setUser(user);

        // Handle document upload
        if (document != null && !document.isEmpty()) {
            String docFileName = fileStorageService.storeFile(document);
            post.setProjectDocs("http://localhost:8080/uploads/" + docFileName);
        }

        // First, save the post
        PostWork savedPost = postWorkRepository.save(post);

        // Handle uploaded images
        if (images != null && images.length > 0) {
            List<ProjectImage> projectImages = new ArrayList<>();
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String fileName = fileStorageService.storeFile(file);
                    // Public URL format
                    String fileUrl = "http://localhost:8080/uploads/" + fileName;
                    projectImages.add(new ProjectImage(savedPost.getPostId(), fileUrl));
                }
            }
            if (!projectImages.isEmpty()) {
                projectImageRepository.saveAll(projectImages);
                // Set the first image as the main projectImage
                savedPost.setProjectImage(projectImages.get(0).getImageUrl());
                postWorkRepository.save(savedPost);
            }
        }

        return savedPost;
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

        PostWork updatedPost = postWorkRepository.save(post);

        // Update images
        projectImageRepository.deleteByPostId(postId);
        if (request.getProjectImages() != null && !request.getProjectImages().isEmpty()) {
            List<ProjectImage> images = request.getProjectImages().stream()
                    .map(url -> new ProjectImage(postId, url))
                    .collect(Collectors.toList());
            projectImageRepository.saveAll(images);
        }

        return updatedPost;
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

    public PostWorkResponse getPostById(Long postId) {
        return postWorkRepository.findById(postId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    private PostWorkResponse mapToResponse(PostWork post) {
        PostWorkResponse resp = new PostWorkResponse();
        resp.setPostId(post.getPostId());
        resp.setSubjectId(post.getSubjectId());
        resp.setProjectName(post.getProjectName());
        resp.setProjectDetail(post.getProjectDetail());
        resp.setProjectImage(post.getProjectImage());
        resp.setProjectLink(post.getProjectLink());
        resp.setProjectDocs(post.getProjectDocs());
        resp.setCreatedAt(post.getCreatedAt());

        // Map images
        List<String> imageUrls = post.getImages().stream()
                .map(ProjectImage::getImageUrl)
                .collect(Collectors.toList());
        resp.setProjectImages(imageUrls);

        if (post.getUser() != null) {
            resp.setUserId(post.getUser().getUserId());
            resp.setUserName(post.getUser().getName());
            resp.setStudentId(post.getUser().getStudentId());
        }

        if (post.getSubjectId() != null) {
            subjectRepository.findById(post.getSubjectId()).ifPresent(s -> {
                resp.setSubjectNameId(s.getSubjectNameId());
                resp.setSubjectNameTh(s.getSubjectNameTh());
            });
        }

        return resp;
    }
}