package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.User;
import com.zoloto.util.DBConnection;

public class UserDAOImpl implements UserDAO {

    private static final String INSERT_USER_SQL =
            "INSERT INTO users "
            + "(first_name, last_name, email, mobile, password, role, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT user_id, first_name, last_name, email, mobile, password, "
            + "role, status, created_at "
            + "FROM users WHERE user_id = ?";

    private static final String FIND_BY_EMAIL_SQL =
            "SELECT user_id, first_name, last_name, email, mobile, password, "
            + "role, status, created_at "
            + "FROM users WHERE email = ?";

    private static final String FIND_BY_MOBILE_SQL =
            "SELECT user_id, first_name, last_name, email, mobile, password, "
            + "role, status, created_at "
            + "FROM users WHERE mobile = ?";

    private static final String FIND_ALL_SQL =
            "SELECT user_id, first_name, last_name, email, mobile, password, "
            + "role, status, created_at "
            + "FROM users ORDER BY user_id";

    private static final String FIND_BY_ROLE_SQL =
            "SELECT user_id, first_name, last_name, email, mobile, password, "
            + "role, status, created_at "
            + "FROM users WHERE role = ? ORDER BY user_id";

    private static final String FIND_BY_STATUS_SQL =
            "SELECT user_id, first_name, last_name, email, mobile, password, "
            + "role, status, created_at "
            + "FROM users WHERE status = ? ORDER BY user_id";

    private static final String EXISTS_BY_EMAIL_SQL =
            "SELECT COUNT(*) FROM users WHERE email = ?";

    private static final String EXISTS_BY_MOBILE_SQL =
            "SELECT COUNT(*) FROM users WHERE mobile = ?";

    private static final String UPDATE_USER_SQL =
            "UPDATE users SET "
            + "first_name = ?, "
            + "last_name = ?, "
            + "email = ?, "
            + "mobile = ?, "
            + "role = ?, "
            + "status = ? "
            + "WHERE user_id = ?";

    private static final String UPDATE_PASSWORD_SQL =
            "UPDATE users SET password = ? WHERE user_id = ?";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE users SET status = ? WHERE user_id = ?";

    private static final String UPDATE_ROLE_SQL =
            "UPDATE users SET role = ? WHERE user_id = ?";

    private static final String DELETE_USER_SQL =
            "DELETE FROM users WHERE user_id = ?";

    private static final String DEACTIVATE_USER_SQL =
            "UPDATE users SET status = 'INACTIVE' WHERE user_id = ?";


    // =========================================================
    // CREATE USER
    // =========================================================

    @Override
    public boolean createUser(User user) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_USER_SQL)) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getMobile());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getRole());
            statement.setString(7, user.getStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            // Print the REAL database error in Eclipse Console
            System.out.println(
                    "========== USER REGISTRATION SQL ERROR ==========");

            System.out.println(
                    "Error Code    : " + e.getErrorCode());

            System.out.println(
                    "SQL State     : " + e.getSQLState());

            System.out.println(
                    "Error Message : " + e.getMessage());

            System.out.println(
                    "=================================================");

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // FIND USER BY ID
    // =========================================================

    @Override
    public User findById(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // FIND USER BY EMAIL
    // =========================================================

    @Override
    public User findByEmail(String email) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_EMAIL_SQL)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // FIND USER BY MOBILE
    // =========================================================

    @Override
    public User findByMobile(String mobile) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_MOBILE_SQL)) {

            statement.setString(1, mobile);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // FIND ALL USERS
    // =========================================================

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    // =========================================================
    // FIND USERS BY ROLE
    // =========================================================

    @Override
    public List<User> findByRole(String role) {

        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ROLE_SQL)) {

            statement.setString(1, role);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(mapResultSetToUser(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    // =========================================================
    // FIND USERS BY STATUS
    // =========================================================

    @Override
    public List<User> findByStatus(String status) {

        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_STATUS_SQL)) {

            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(mapResultSetToUser(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    // =========================================================
    // CHECK EMAIL
    // =========================================================

    @Override
    public boolean existsByEmail(String email) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_EMAIL_SQL)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =========================================================
    // CHECK MOBILE
    // =========================================================

    @Override
    public boolean existsByMobile(String mobile) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_MOBILE_SQL)) {

            statement.setString(1, mobile);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    @Override
    public boolean updateUser(User user) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_USER_SQL)) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getMobile());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());
            statement.setInt(7, user.getUserId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =========================================================
    // UPDATE PASSWORD
    // =========================================================

    @Override
    public boolean updatePassword(int userId, String hashedPassword) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_PASSWORD_SQL)) {

            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =========================================================
    // UPDATE STATUS
    // =========================================================

    @Override
    public boolean updateStatus(int userId, String status) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_STATUS_SQL)) {

            statement.setString(1, status);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =========================================================
    // UPDATE ROLE
    // =========================================================

    @Override
    public boolean updateRole(int userId, String role) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_ROLE_SQL)) {

            statement.setString(1, role);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Override
    public boolean deleteUser(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_USER_SQL)) {

            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =========================================================
    // DEACTIVATE USER
    // =========================================================

    @Override
    public boolean deactivateUser(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DEACTIVATE_USER_SQL)) {

            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =========================================================
    // MAP RESULT SET TO USER
    // =========================================================

    private User mapResultSetToUser(ResultSet resultSet)
            throws SQLException {

        User user = new User();

        user.setUserId(resultSet.getInt("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setMobile(resultSet.getString("mobile"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role"));
        user.setStatus(resultSet.getString("status"));

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        return user;
    }
}