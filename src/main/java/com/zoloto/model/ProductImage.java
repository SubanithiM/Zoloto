package com.zoloto.model;

public class ProductImage {

    private int imageId;
    private int productId;
    private String imageUrl;
    private boolean primary;

    // Default constructor
    public ProductImage() {
    }

    // Constructor without ID
    public ProductImage(int productId, String imageUrl, boolean primary) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.primary = primary;
    }

    // Full constructor
    public ProductImage(int imageId, int productId,
                         String imageUrl, boolean primary) {
        this.imageId = imageId;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.primary = primary;
    }

    // Getters and Setters

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "imageId=" + imageId +
                ", productId=" + productId +
                ", imageUrl='" + imageUrl + '\'' +
                ", primary=" + primary +
                '}';
    }
}