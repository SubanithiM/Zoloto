package com.zoloto.service;

import com.zoloto.model.User;

public class UserServiceTest {

    public static void main(String[] args) {

        String email = "david.finaltest@example.com";
        String mobile = "9876543216";

        User user = new User(
                "David",
                "Kumar",
                email,
                mobile,
                "password123",
                null,
                null
        );

        UserService userService = new UserService();

        boolean result = userService.registerUser(user);

        if (result) {
            System.out.println("User registration through service successful!");
        } else {
            System.out.println("User registration through service failed!");
        }
    }
}