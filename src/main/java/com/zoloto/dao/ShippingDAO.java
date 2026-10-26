package com.zoloto.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.zoloto.model.Shipping;

public interface ShippingDAO {

    // Create
    boolean createShipping(Shipping shipping);

    // Read
    Shipping findById(int shippingId);

    Shipping findByOrderId(int orderId);

    Shipping findByTrackingNumber(String trackingNumber);

    List<Shipping> findAll();

    List<Shipping> findByStatus(String deliveryStatus);

    List<Shipping> findByCourier(String courierName);

    // Search / filtering
    List<Shipping> findByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate);

    List<Shipping> findByStatusAndDateRange(
            String deliveryStatus,
            LocalDateTime startDate,
            LocalDateTime endDate);

    // Existence
    boolean existsById(int shippingId);

    boolean existsByOrderId(int orderId);

    boolean existsByTrackingNumber(String trackingNumber);

    // Update
    boolean updateShipping(Shipping shipping);

    boolean updateCourier(
            int shippingId,
            String courierName);

    boolean updateTrackingNumber(
            int shippingId,
            String trackingNumber);

    boolean updateShippingDate(
            int shippingId,
            LocalDateTime shippingDate);

    boolean updateExpectedDelivery(
            int shippingId,
            LocalDateTime expectedDelivery);

    boolean updateDeliveryStatus(
            int shippingId,
            String deliveryStatus);

    // Delete
    boolean deleteShipping(int shippingId);

    boolean deleteByOrderId(int orderId);

    // Count
    int countAll();

    int countByStatus(String deliveryStatus);
}