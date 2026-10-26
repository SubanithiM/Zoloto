package com.zoloto.util;

public class PasswordUtilTest {

    public static void main(String[] args) {

        String password = "password123";

        String hashedPassword =
                PasswordUtil.hashPassword(password);

        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashedPassword);

        boolean valid =
                PasswordUtil.checkPassword(password, hashedPassword);

        System.out.println("Password verification: " + valid);
    }
}