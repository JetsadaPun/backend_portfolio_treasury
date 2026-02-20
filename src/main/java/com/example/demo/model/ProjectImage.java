package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "project_images")
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "image_url", length = 2048, nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private PostWork postWork;

    // Constructors
    public ProjectImage() {
    }

    public ProjectImage(Long postId, String imageUrl) {
        this.postId = postId;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PostWork getPostWork() {
        return postWork;
    }

    public void setPostWork(PostWork postWork) {
        this.postWork = postWork;
    }
}
