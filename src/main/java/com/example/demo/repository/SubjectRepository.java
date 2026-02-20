package com.example.demo.repository;

import com.example.demo.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsBySubjectNameId(String subjectNameId);

    java.util.Optional<Subject> findBySubjectNameId(String subjectNameId);

    @org.springframework.data.jpa.repository.Query("SELECT s FROM Subject s WHERE " +
            "LOWER(s.subjectNameId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.subjectNameTh) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.subjectNameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    org.springframework.data.domain.Page<Subject> searchSubjects(
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            org.springframework.data.domain.Pageable pageable);
}
