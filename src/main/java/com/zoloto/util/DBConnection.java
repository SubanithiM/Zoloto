
package com.zoloto.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = DBConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input != null) {
                PROPERTIES.load(input);
            }

        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    "Failed to load database configuration: " + e.getMessage());
        }
    }

    private DBConnection() {
        // Prevent object creation
    }

    public static Connection getConnection() throws SQLException {

        String url = getConfig("DB_URL", "db.url");
        String username = getConfig("DB_USERNAME", "db.username");
        String password = getConfig("DB_PASSWORD", "db.password");

        if (url == null || username == null || password == null) {
            throw new SQLException(
                    "Database configuration is missing. "
                    + "Check db.properties or the DB_URL, DB_USERNAME, "
                    + "and DB_PASSWORD environment variables.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver not found.", e);
        }

        return DriverManager.getConnection(url, username, password);
    }

    private static String getConfig(
            String environmentVariable, String propertyName) {

        String value = System.getenv(environmentVariable);

        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        value = PROPERTIES.getProperty(propertyName);

        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        return null;
    }
}
