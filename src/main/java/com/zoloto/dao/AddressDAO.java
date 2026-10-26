package com.zoloto.dao;

import java.util.List;

import com.zoloto.model.Address;

public interface AddressDAO {

    // Create
    boolean createAddress(Address address);

    // Read
    Address findById(int addressId);

    List<Address> findByUserId(int userId);

    List<Address> findByUserIdAndType(int userId, String addressType);

    Address findDefaultAddressByUserId(int userId);

    List<Address> findAll();

    // Existence / count
    boolean existsById(int addressId);

    int countByUserId(int userId);

    // Update
    boolean updateAddress(Address address);

    boolean updateAddressType(int addressId, String addressType);

    boolean setDefaultAddress(int userId, int addressId);

    boolean clearDefaultAddress(int userId);

    // Delete
    boolean deleteAddress(int addressId);

    boolean deleteAllByUserId(int userId);
}