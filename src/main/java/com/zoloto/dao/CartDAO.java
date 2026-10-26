package com.zoloto.dao;

import java.util.List;

import com.zoloto.model.Cart;

public interface CartDAO {

    // Create
    boolean createCart(Cart cart);

    // Read
    Cart findById(int cartId);

    Cart findByUserId(int userId);

    List<Cart> findAll();

    // Existence
    boolean existsById(int cartId);

    boolean existsByUserId(int userId);

    // Cart management
    boolean updateCart(int cartId);

    boolean clearCart(int cartId);

    // Delete
    boolean deleteCart(int cartId);

    boolean deleteByUserId(int userId);
}