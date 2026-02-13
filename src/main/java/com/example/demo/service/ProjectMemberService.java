package com.example.demo.service;

import com.example.demo.dto.MemberAddRequest;
import com.example.demo.model.PostWork;
import com.example.demo.model.ProjectMember;
import com.example.demo.model.User;
import com.example.demo.repository.PostWorkRepository;
import com.example.demo.repository.ProjectMemberRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final PostWorkRepository postWorkRepository;
    private final UserRepository userRepository;

    public ProjectMemberService(ProjectMemberRepository projectMemberRepository,
                                PostWorkRepository postWorkRepository,
                                UserRepository userRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.postWorkRepository = postWorkRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProjectMember addMember(MemberAddRequest req) {
        PostWork post = postWorkRepository.findById(req.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (projectMemberRepository.existsByPost_PostIdAndUser_UserId(post.getPostId(), user.getId())) {
            throw new IllegalStateException("User is already a member of this post");
        }

        ProjectMember m = new ProjectMember();
        m.setPost(post);
        m.setUser(user);
        m.setRole( (req.getRole() == null || req.getRole().isBlank()) ? "MEMBER" : req.getRole() );
        m.setStatus("active");

        if (req.getAddedBy() != null && !req.getAddedBy().isBlank()) {
            User actor = userRepository.findById(req.getAddedBy())
                    .orElseThrow(() -> new IllegalArgumentException("addedBy user not found"));
            m.setAddedBy(actor);
        }

        return projectMemberRepository.save(m);
    }

    @Transactional(readOnly = true)
    public List<ProjectMember> listMembers(Long postId) {
        return projectMemberRepository.findByPost_PostIdAndStatus(postId, "active");
    }

    @Transactional
    public void removeMember(Long postId, String userId) {
        ProjectMember m = projectMemberRepository.findByPost_PostIdAndUser_UserId(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found for this post"));
        m.setStatus("removed");   // soft delete
        projectMemberRepository.save(m);
        // หรือ projectMemberRepository.delete(m); ถ้าต้องการ hard delete
    }
}