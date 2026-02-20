package com.example.demo.controller;

import com.example.demo.model.ReportPost;
import com.example.demo.service.ReportPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportPostController {

    private final ReportPostService reportPostService;

    public ReportPostController(ReportPostService reportPostService) {
        this.reportPostService = reportPostService;
    }

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody Map<String, Object> payload, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Long postId = Long.valueOf(payload.get("postId").toString());
        String reason = payload.get("reason").toString();

        ReportPost report = reportPostService.reportPost(postId, principal.getName(), reason);
        return ResponseEntity.ok(Map.of("message", "Report submitted", "id", report.getReportId()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReports() {
        return ResponseEntity.ok(reportPostService.getAllReports().stream().map(r -> {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", r.getReportId());
            map.put("postId", r.getPost().getPostId());
            map.put("projectName", r.getPost().getProjectName());
            map.put("reporterName", r.getReporter().getName());
            map.put("reporterEmail", r.getReporter().getEmail());
            map.put("ownerName", r.getPost().getUser().getName());
            map.put("ownerEmail", r.getPost().getUser().getEmail());
            map.put("reason", r.getReason());
            map.put("status", r.getStatus());
            map.put("actionTaken", r.getActionTaken());
            map.put("createdAt", r.getCreatedAt());
            return map;
        }).collect(Collectors.toList()));
    }

    @PutMapping("/{id}/respond")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> respondToReport(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String action = payload.get("action"); // CLOSE, VERIFY, NEED_FIX
        String comment = payload.get("comment");

        ReportPost report = reportPostService.handleReport(id, action, comment);
        return ResponseEntity.ok(Map.of("message", "Action applied: " + report.getActionTaken()));
    }
}
