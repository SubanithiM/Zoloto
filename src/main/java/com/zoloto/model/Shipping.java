package com.zoloto.model;

import java.time.LocalDateTime;

public class Shipping {

    private int shippingId;
    private int orderId;
    private String courierName;
    private String trackingNumber;
    private LocalDateTime shippingDate;
    private LocalDateTime expectedDelivery;
    private String deliveryStatus;

    // Default constructor
    public Shipping() {
    }

    // Constructor without ID
    public Shipping(int orderId,
                    String courierName,
                    String trackingNumber,
                    LocalDateTime shippingDate,
                    LocalDateTime expectedDelivery,
                    String deliveryStatus) {

        this.orderId = orderId;
        this.courierName = courierName;
        this.trackingNumber = trackingNumber;
        this.shippingDate = shippingDate;
        this.expectedDelivery = expectedDelivery;
        this.deliveryStatus = deliveryStatus;
    }

    // Full constructor
    public Shipping(int shippingId,
                    int orderId,
                    String courierName,
                    String trackingNumber,
                    LocalDateTime shippingDate,
                    LocalDateTime expectedDelivery,
                    String deliveryStatus) {

        this.shippingId = shippingId;
        this.orderId = orderId;
        this.courierName = courierName;
        this.trackingNumber = trackingNumber;
        this.shippingDate = shippingDate;
        this.expectedDelivery = expectedDelivery;
        this.deliveryStatus = deliveryStatus;
    }

    // Getters and Setters

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDateTime getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public LocalDateTime getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDateTime expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    @Override
    public String toString() {
        return "Shipping{" +
                "shippingId=" + shippingId +
                ", orderId=" + orderId +
                ", courierName='" + courierName + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", shippingDate=" + shippingDate +
                ", expectedDelivery=" + expectedDelivery +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                '}';
    }
}