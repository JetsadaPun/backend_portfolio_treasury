package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class LikeRequest {
    @NotNull
    private Long postId;

    private String userId;
    private String userName;

    // getter & setter
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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
}
