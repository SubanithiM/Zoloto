package com.zoloto.dao;

import java.util.List;

import com.zoloto.model.User;

public interface UserDAO {

    // Create
    boolean createUser(User user);

    // Read
    User findById(int userId);

    User findByEmail(String email);

    User findByMobile(String mobile);

    List<User> findAll();

    List<User> findByRole(String role);

    List<User> findByStatus(String status);

    // Existence checks
    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    // Update
    boolean updateUser(User user);

    boolean updatePassword(int userId, String hashedPassword);

    boolean updateStatus(int userId, String status);

    boolean updateRole(int userId, String role);

    // Delete / deactivate
    boolean deleteUser(int userId);

    boolean deactivateUser(int userId);
}