package com.example.demo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "Subject_name_thai")
    private String subjectNameTh;

    @Column(name = "Subject_Name_eng")
    private String subjectNameEn;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Subject_name_id", nullable = false, unique = true)
    private String subjectNameId;

    @Column(name = "properties", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> properties = new HashMap<>();

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

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
