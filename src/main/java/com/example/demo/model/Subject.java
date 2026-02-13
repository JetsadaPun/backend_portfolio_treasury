package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subjectname_th")
    private String subjectNameTh;

    @Column(name = "subjectname_en")
    private String subjectNameEn;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "subjectname_id", nullable = false, unique = true)
    private String subjectNameId;

    // ===== Getter & Setter =====

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectNameTh() {
        return subjectNameTh;
    }

    public void setSubjectNameTh(String subjectNameTh) {
        this.subjectNameTh = subjectNameTh;
    }

    public String getSubjectNameEn() {
        return subjectNameEn;
    }

    public void setSubjectNameEn(String subjectNameEn) {
        this.subjectNameEn = subjectNameEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubjectNameId() {
        return subjectNameId;
    }

    public void setSubjectNameId(String subjectNameId) {
        this.subjectNameId = subjectNameId;
    }
}
