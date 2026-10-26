package com.zoloto.service;

import com.zoloto.dao.UserDAO;
import com.zoloto.dao.UserDAOImpl;
import com.zoloto.model.User;
import com.zoloto.util.PasswordUtil;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Register a new customer.
     */
    public boolean registerUser(User user) {

        if (user == null) {
            return false;
        }

        // Validate required fields
        if (isBlank(user.getFirstName())
                || isBlank(user.getLastName())
                || isBlank(user.getEmail())
                || isBlank(user.getMobile())
                || isBlank(user.getPassword())) {

            return false;
        }

        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            return false;
        }

        // Validate mobile number
        if (!isValidMobile(user.getMobile())) {
            return false;
        }

        // Prevent duplicate email
        if (userDAO.existsByEmail(user.getEmail())) {
            return false;
        }

        // Prevent duplicate mobile
        if (userDAO.existsByMobile(user.getMobile())) {
            return false;
        }

        // Set default customer values
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        // Hash password before storing
        String hashedPassword =
                PasswordUtil.hashPassword(user.getPassword());

        user.setPassword(hashedPassword);

        return userDAO.createUser(user);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email != null
                && email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidMobile(String mobile) {
        return mobile != null
                && mobile.matches("\\d{10,15}");
    }
}