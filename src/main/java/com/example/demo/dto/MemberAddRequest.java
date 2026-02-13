package com.example.demo.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MemberAddRequest {
    @NotNull
    private Long postId;

    @NotBlank
    private String userId;   // user ที่จะถูกเพิ่ม

    private String role;     // ออปชัน: ถ้าไม่ส่ง จะ default = MEMBER

    private String addedBy;  // ออปชัน: ผู้ที่กระทำ (ตรวจสิทธิ์ภายหลัง)

    // getters/setters
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAddedBy() { return addedBy; }
    public void setAddedBy(String addedBy) { this.addedBy = addedBy; }
}