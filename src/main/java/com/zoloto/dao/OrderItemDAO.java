package com.zoloto.dao;

import java.math.BigDecimal;
import java.util.List;

import com.zoloto.model.OrderItem;

public interface OrderItemDAO {

    // Create
    boolean createOrderItem(OrderItem orderItem);

    // Read
    OrderItem findById(int orderItemId);

    OrderItem findByOrderIdAndProductId(
            int orderId,
            int productId);

    List<OrderItem> findByOrderId(int orderId);

    List<OrderItem> findByProductId(int productId);

    List<OrderItem> findAll();

    // Existence / count
    boolean existsById(int orderItemId);

    boolean existsByOrderIdAndProductId(
            int orderId,
            int productId);

    int countByOrderId(int orderId);

    // Calculations
    BigDecimal getTotalByOrderId(int orderId);

    // Update
    boolean updateOrderItem(OrderItem orderItem);

    boolean updateQuantity(
            int orderItemId,
            int quantity);

    boolean updateUnitPrice(
            int orderItemId,
            BigDecimal unitPrice);

    // Delete
    boolean deleteOrderItem(int orderItemId);

    boolean deleteByOrderIdAndProductId(
            int orderId,
            int productId);

    boolean deleteByOrderId(int orderId);
}