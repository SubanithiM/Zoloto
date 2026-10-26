package com.zoloto.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.zoloto.model.Order;

public interface OrderDAO {

    // =====================================================
    // CREATE
    // =====================================================

    boolean createOrder(Order order);


    // =====================================================
    // READ
    // =====================================================

    Order findById(int orderId);

    Order findByOrderNumber(String orderNumber);

    List<Order> findAll();

    List<Order> findByUserId(int userId);

    List<Order> findByStatus(String status);

    List<Order> findByPaymentStatus(String paymentStatus);

    List<Order> findByPaymentMethod(String paymentMethod);


    // =====================================================
    // SEARCH / FILTER
    // =====================================================

    List<Order> findByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate);

    List<Order> findByUserIdAndStatus(
            int userId,
            String status);

    List<Order> findByUserIdAndDateRange(
            int userId,
            LocalDateTime startDate,
            LocalDateTime endDate);

    List<Order> findByTotalRange(
            BigDecimal minTotal,
            BigDecimal maxTotal);


    // =====================================================
    // EXISTENCE
    // =====================================================

    boolean existsById(int orderId);

    boolean existsByOrderNumber(String orderNumber);


    // =====================================================
    // COUNT / CALCULATIONS
    // =====================================================

    int countAll();

    int countByUserId(int userId);

    int countByStatus(String status);

    BigDecimal getTotalByUserId(int userId);

    BigDecimal getTotalByStatus(String status);


    // =====================================================
    // UPDATE
    // =====================================================

    boolean updateOrder(Order order);

    boolean updateStatus(
            int orderId,
            String status);

    boolean updatePaymentStatus(
            int orderId,
            String paymentStatus);

    boolean updatePaymentMethod(
            int orderId,
            String paymentMethod);

    boolean updateTotalAmount(
            int orderId,
            BigDecimal totalAmount);


    // =====================================================
    // DELETE
    // =====================================================

    boolean deleteOrder(int orderId);

    boolean deleteByUserId(int userId);
}