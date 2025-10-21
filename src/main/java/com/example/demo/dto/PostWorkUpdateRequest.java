package com.example.demo.dto;

import jakarta.validation.constraints.Size;

public class PostWorkUpdateRequest {
    private Integer subjectId;

    @Size(max = 255)
    private String projectName;

    private String projectDetail;

    @Size(max = 2048)
    private String projectImage;

    @Size(max = 2048)
    private String projectLink;

    @Size(max = 2048)
    private String projectDocs;

    // ถ้าจะบังคับว่าผู้โพสต์เท่านั้นที่แก้ได้ ให้ส่ง userId มาด้วยเพื่อตรวจเจ้าของ
    private String userId;

    // getters/setters...
    public Integer getSubjectId() { return subjectId; }
    public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getProjectDetail() { return projectDetail; }
    public void setProjectDetail(String projectDetail) { this.projectDetail = projectDetail; }
    public String getProjectImage() { return projectImage; }
    public void setProjectImage(String projectImage) { this.projectImage = projectImage; }
    public String getProjectLink() { return projectLink; }
    public void setProjectLink(String projectLink) { this.projectLink = projectLink; }
    public String getProjectDocs() { return projectDocs; }
    public void setProjectDocs(String projectDocs) { this.projectDocs = projectDocs; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}