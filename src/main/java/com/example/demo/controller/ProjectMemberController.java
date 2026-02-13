package com.example.demo.controller;

import com.example.demo.dto.MemberAddRequest;
import com.example.demo.model.ProjectMember;
import com.example.demo.service.ProjectMemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    // เพิ่มสมาชิก 1 คน
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody MemberAddRequest req) {
        ProjectMember saved = projectMemberService.addMember(req);
        return ResponseEntity.status(201).body(
                java.util.Map.of("memberId", saved.getMemberId(), "message", "Member added")
        );
    }

    // ดูสมาชิกทั้งหมดของโพสต์
    @GetMapping("/{postId}")
    public ResponseEntity<List<ProjectMember>> list(@PathVariable Long postId) {
        return ResponseEntity.ok(projectMemberService.listMembers(postId));
    }

    // ลบสมาชิกออกจากโพสต์ (soft delete หรือ hard delete ตามที่ implement)
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<?> remove(@PathVariable Long postId, @PathVariable String userId) {
        projectMemberService.removeMember(postId, userId);
        return ResponseEntity.ok(java.util.Map.of("message", "Member removed"));
    }
}