
package com.zoloto.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    private int productId;
    private int categoryId;
    private String productName;
    private String description;
    private String brand;
    private String sku;
    private BigDecimal price;
    private BigDecimal discount;
    private int stockQuantity;
    private BigDecimal rating;
    private String status;
    private LocalDateTime createdAt;

    // Image path retrieved from the product_images database table.
    private String imageUrl;

    // Default constructor
    public Product() {
    }

    // Constructor without ID and createdAt
    public Product(int categoryId,
                   String productName,
                   String description,
                   String brand,
                   String sku,
                   BigDecimal price,
                   BigDecimal discount,
                   int stockQuantity,
                   BigDecimal rating,
                   String status) {

        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.sku = sku;
        this.price = price;
        this.discount = discount;
        this.stockQuantity = stockQuantity;
        this.rating = rating;
        this.status = status;
    }

    // Full constructor
    public Product(int productId,
                   int categoryId,
                   String productName,
                   String description,
                   String brand,
                   String sku,
                   BigDecimal price,
                   BigDecimal discount,
                   int stockQuantity,
                   BigDecimal rating,
                   String status,
                   LocalDateTime createdAt) {

        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.sku = sku;
        this.price = price;
        this.discount = discount;
        this.stockQuantity = stockQuantity;
        this.rating = rating;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Image URL getter and setter

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", stockQuantity=" + stockQuantity +
                ", rating=" + rating +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
