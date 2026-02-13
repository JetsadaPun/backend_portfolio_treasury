package com.example.demo.controller;

import com.example.demo.model.Subject;
import com.example.demo.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<Subject> addSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectService.addSubject(subject));
    }

    @PostMapping("/import-csv")
    public ResponseEntity<?> importCSV(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            subjectService.importCSV(file.getInputStream());
            return ResponseEntity.ok(Map.of("message", "CSV imported successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSubjects() {
        try {
            return ResponseEntity.ok(subjectService.getAllSubjects());
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace to see why it fails
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getSubjectByCode(@PathVariable String code) {
        return subjectService.getSubjectByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(Map.of("message", "Subject deleted successfully"));
    }
}
