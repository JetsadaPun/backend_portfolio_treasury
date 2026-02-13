package com.example.demo.dto;

import java.time.LocalDateTime;

public class PostWorkResponse {
    private Long postId;
    private Long subjectId;
    private String subjectNameId;
    private String subjectNameTh;
    private String projectName;
    private String projectDetail;
    private String projectImage;
    private String projectLink;
    private String projectDocs;
    private String userId;
    private String userName;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectNameId() {
        return subjectNameId;
    }

    public void setSubjectNameId(String subjectNameId) {
        this.subjectNameId = subjectNameId;
    }

    public String getSubjectNameTh() {
        return subjectNameTh;
    }

    public void setSubjectNameTh(String subjectNameTh) {
        this.subjectNameTh = subjectNameTh;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
