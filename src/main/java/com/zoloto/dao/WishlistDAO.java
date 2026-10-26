package com.zoloto.dao;

import java.util.List;

import com.zoloto.model.Wishlist;

public interface WishlistDAO {

    // Create
    boolean addToWishlist(Wishlist wishlist);

    // Read
    Wishlist findById(int wishlistId);

    Wishlist findByUserIdAndProductId(
            int userId,
            int productId);

    List<Wishlist> findByUserId(int userId);

    List<Wishlist> findByProductId(int productId);

    List<Wishlist> findAll();

    // Existence / count
    boolean exists(
            int userId,
            int productId);

    boolean existsById(int wishlistId);

    int countByUserId(int userId);

    // Delete
    boolean removeById(int wishlistId);

    boolean removeByUserIdAndProductId(
            int userId,
            int productId);

    boolean removeAllByUserId(int userId);

    boolean removeAllByProductId(int productId);
}