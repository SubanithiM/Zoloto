package com.zoloto.dao;

import java.util.List;

import com.zoloto.model.CartItem;

public interface CartItemDAO {

    // Create
    boolean addCartItem(CartItem cartItem);

    // Read
    CartItem findById(int cartItemId);

    CartItem findByCartIdAndProductId(int cartId, int productId);

    List<CartItem> findByCartId(int cartId);

    List<CartItem> findByProductId(int productId);

    // Existence / count
    boolean existsById(int cartItemId);

    boolean existsByCartIdAndProductId(int cartId, int productId);

    int countByCartId(int cartId);

    // Update
    boolean updateQuantity(int cartItemId, int quantity);

    boolean incrementQuantity(int cartItemId, int amount);

    boolean decrementQuantity(int cartItemId, int amount);

    // Delete
    boolean deleteById(int cartItemId);

    boolean deleteByCartIdAndProductId(int cartId, int productId);

    boolean clearCart(int cartId);

    // Cart totals
    int getTotalQuantity(int cartId);
}