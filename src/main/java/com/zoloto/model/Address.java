package com.zoloto.model;

public class Address {

    private int addressId;
    private int userId;
    private String fullName;
    private String mobile;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String addressType;
    private boolean defaultAddress;

    // Default constructor
    public Address() {
    }

    // Constructor without ID
    public Address(int userId,
                   String fullName,
                   String mobile,
                   String addressLine1,
                   String addressLine2,
                   String city,
                   String state,
                   String country,
                   String pincode,
                   String addressType,
                   boolean defaultAddress) {

        this.userId = userId;
        this.fullName = fullName;
        this.mobile = mobile;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.addressType = addressType;
        this.defaultAddress = defaultAddress;
    }

    // Full constructor
    public Address(int addressId,
                   int userId,
                   String fullName,
                   String mobile,
                   String addressLine1,
                   String addressLine2,
                   String city,
                   String state,
                   String country,
                   String pincode,
                   String addressType,
                   boolean defaultAddress) {

        this.addressId = addressId;
        this.userId = userId;
        this.fullName = fullName;
        this.mobile = mobile;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.addressType = addressType;
        this.defaultAddress = defaultAddress;
    }

    // Getters and Setters

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pincode='" + pincode + '\'' +
                ", addressType='" + addressType + '\'' +
                ", defaultAddress=" + defaultAddress +
                '}';
    }
}