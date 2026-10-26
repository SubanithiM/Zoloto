package com.zoloto.model;

import java.time.LocalDateTime;

public class Wishlist {

    private int wishlistId;
    private int userId;
    private int productId;
    private LocalDateTime createdAt;

    // Default constructor
    public Wishlist() {
    }

    // Constructor without ID and createdAt
    public Wishlist(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    // Full constructor
    public Wishlist(int wishlistId,
                    int userId,
                    int productId,
                    LocalDateTime createdAt) {

        this.wishlistId = wishlistId;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlistId=" + wishlistId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", createdAt=" + createdAt +
                '}';
    }
}