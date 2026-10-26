package com.zoloto.dao;

import java.math.BigDecimal;
import java.util.List;

import com.zoloto.model.Review;

public interface ReviewDAO {

    // Create
    boolean createReview(Review review);

    // Read
    Review findById(int reviewId);

    Review findByUserIdAndProductId(
            int userId,
            int productId);

    List<Review> findByProductId(int productId);

    List<Review> findByUserId(int userId);

    List<Review> findAll();

    // Rating / filtering
    List<Review> findByRating(
            int rating);

    List<Review> findByProductIdAndRating(
            int productId,
            int rating);

    BigDecimal getAverageRating(
            int productId);

    int countByProductId(
            int productId);

    // Existence
    boolean existsById(int reviewId);

    boolean existsByUserIdAndProductId(
            int userId,
            int productId);

    // Update
    boolean updateReview(Review review);

    boolean updateRating(
            int reviewId,
            int rating);

    boolean updateComment(
            int reviewId,
            String comment);

    // Delete
    boolean deleteReview(int reviewId);

    boolean deleteByUserIdAndProductId(
            int userId,
            int productId);

    boolean deleteByProductId(
            int productId);

    boolean deleteByUserId(
            int userId);
}