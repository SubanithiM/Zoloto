package com.zoloto.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.zoloto.model.Coupon;

public interface CouponDAO {

    // Create
    boolean createCoupon(Coupon coupon);

    // Read
    Coupon findById(int couponId);

    Coupon findByCode(String couponCode);

    List<Coupon> findAll();

    List<Coupon> findByStatus(String status);

    List<Coupon> findActiveCoupons();

    // Search / filtering
    List<Coupon> findByDiscountType(String discountType);

    List<Coupon> findByMinimumOrderAmount(
            BigDecimal minimumOrderAmount);

    List<Coupon> findValidCoupons(LocalDateTime dateTime);

    // Existence
    boolean existsById(int couponId);

    boolean existsByCode(String couponCode);

    // Validation
    boolean isValidCoupon(
            String couponCode,
            BigDecimal orderAmount,
            LocalDateTime dateTime);

    // Update
    boolean updateCoupon(Coupon coupon);

    boolean updateStatus(int couponId, String status);

    boolean updateDiscountValue(
            int couponId,
            BigDecimal discountValue);

    boolean updateUsageLimit(
            int couponId,
            int usageLimit);

    boolean updateDates(
            int couponId,
            LocalDateTime startDate,
            LocalDateTime endDate);

    // Delete
    boolean deleteCoupon(int couponId);

    // Count
    int countAll();

    int countByStatus(String status);
}