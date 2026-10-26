package com.zoloto.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.zoloto.model.Payment;

public interface PaymentDAO {

    // Create
    boolean createPayment(Payment payment);

    // Read
    Payment findById(int paymentId);

    Payment findByOrderId(int orderId);

    List<Payment> findAll();

    List<Payment> findByUserId(int userId);

    List<Payment> findByStatus(String paymentStatus);

    List<Payment> findByMethod(String paymentMethod);

    List<Payment> findByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate);

    // Search / filtering
    List<Payment> findByUserIdAndStatus(
            int userId,
            String paymentStatus);

    List<Payment> findByUserIdAndMethod(
            int userId,
            String paymentMethod);

    // Existence
    boolean existsById(int paymentId);

    boolean existsByOrderId(int orderId);

    // Count / calculation
    int countByUserId(int userId);

    int countByStatus(String paymentStatus);

    BigDecimal getTotalPaidByUserId(int userId);

    BigDecimal getTotalPaidByOrderId(int orderId);

    // Update
    boolean updatePayment(Payment payment);

    boolean updateStatus(
            int paymentId,
            String paymentStatus);

    boolean updateTransactionId(
            int paymentId,
            String transactionId);

    boolean updatePaymentMethod(
            int paymentId,
            String paymentMethod);

    boolean updateAmount(
            int paymentId,
            BigDecimal amount);

    // Delete
    boolean deletePayment(int paymentId);

    boolean deleteByOrderId(int orderId);

    boolean deleteByUserId(int userId);
}