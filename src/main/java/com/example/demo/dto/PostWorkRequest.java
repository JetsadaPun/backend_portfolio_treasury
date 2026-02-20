package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class PostWorkRequest {

    @NotNull
    private Long subjectId;

    @NotBlank
    @Size(max = 255)
    private String projectName;

    @NotBlank
    private String projectDetail;

    @Size(max = 2048)
    private String projectImage;

    @Size(max = 2048)
    private String projectLink;

    @Size(max = 2048)
    private String projectDocs;

    @NotBlank
    private String userId;

    private List<String> projectImages;

    public List<String> getProjectImages() {
        return projectImages;
    }

    public void setProjectImages(List<String> projectImages) {
        this.projectImages = projectImages;
    }

    // getters และ setters สำหรับทุก field รวมถึง userId

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(String projectDetail) {
        this.projectDetail = projectDetail;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getProjectDocs() {
        return projectDocs;
    }

    public void setProjectDocs(String projectDocs) {
        this.projectDocs = projectDocs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}