package com.zoloto.dao;

import java.util.List;

import com.zoloto.model.ProductImage;

public interface ProductImageDAO {

    // Create
    boolean createProductImage(ProductImage productImage);

    // Read
    ProductImage findById(int imageId);

    List<ProductImage> findByProductId(int productId);

    ProductImage findPrimaryImageByProductId(int productId);

    List<ProductImage> findPrimaryImages();

    // Existence / count
    boolean existsById(int imageId);

    int countByProductId(int productId);

    // Update
    boolean updateProductImage(ProductImage productImage);

    boolean setPrimaryImage(int productId, int imageId);

    boolean clearPrimaryImages(int productId);

    // Delete
    boolean deleteProductImage(int imageId);

    boolean deleteByProductId(int productId);

    // Reordering / management
    boolean updateImageUrl(int imageId, String imageUrl);

    boolean updatePrimaryStatus(int imageId, boolean primary);
}