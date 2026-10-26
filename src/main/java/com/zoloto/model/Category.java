package com.zoloto.model;

import java.time.LocalDateTime;

public class Category {

    private int categoryId;
    private String categoryName;
    private String description;
    private String status;
    private String imageUrl;
    private LocalDateTime createdAt;

    // Default constructor
    public Category() {
    }

    // Constructor without ID and createdAt
    public Category(String categoryName, String description, String status) {
        this.categoryName = categoryName;
        this.description = description;
        this.status = status;
    }

    // Constructor including the image URL
    public Category(
            String categoryName,
            String description,
            String status,
            String imageUrl) {

        this.categoryName = categoryName;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    // Full constructor
    public Category(
            int categoryId,
            String categoryName,
            String description,
            String status,
            LocalDateTime createdAt) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Full constructor including the image URL
    public Category(
            int categoryId,
            String categoryName,
            String description,
            String status,
            String imageUrl,
            LocalDateTime createdAt) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Get image URL from the database
    public String getImageUrl() {
        return imageUrl;
    }

    // Set image URL from the database
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}