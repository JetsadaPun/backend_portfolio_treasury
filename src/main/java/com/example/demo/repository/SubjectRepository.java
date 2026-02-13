package com.example.demo.repository;

import com.example.demo.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsBySubjectNameId(String subjectNameId);

    java.util.Optional<Subject> findBySubjectNameId(String subjectNameId);
}
