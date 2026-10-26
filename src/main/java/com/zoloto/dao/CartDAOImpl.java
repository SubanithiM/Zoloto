package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Cart;
import com.zoloto.util.DBConnection;

public class CartDAOImpl implements CartDAO {

    private static final String INSERT_CART_SQL =
            "INSERT INTO carts (user_id) VALUES (?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT cart_id, user_id, created_at, updated_at " +
            "FROM carts WHERE cart_id = ?";

    private static final String FIND_BY_USER_ID_SQL =
            "SELECT cart_id, user_id, created_at, updated_at " +
            "FROM carts WHERE user_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT cart_id, user_id, created_at, updated_at " +
            "FROM carts ORDER BY cart_id";

    private static final String EXISTS_BY_ID_SQL =
            "SELECT COUNT(*) FROM carts WHERE cart_id = ?";

    private static final String EXISTS_BY_USER_ID_SQL =
            "SELECT COUNT(*) FROM carts WHERE user_id = ?";

    private static final String UPDATE_CART_SQL =
            "UPDATE carts SET updated_at = CURRENT_TIMESTAMP " +
            "WHERE cart_id = ?";

    private static final String CLEAR_CART_SQL =
            "DELETE FROM cart_items WHERE cart_id = ?";

    private static final String DELETE_CART_SQL =
            "DELETE FROM carts WHERE cart_id = ?";

    private static final String DELETE_BY_USER_ID_SQL =
            "DELETE FROM carts WHERE user_id = ?";


    @Override
    public boolean createCart(Cart cart) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_CART_SQL)) {

            statement.setInt(1, cart.getUserId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Cart findById(int cartId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToCart(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Cart findByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToCart(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Cart> findAll() {

        List<Cart> carts = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                carts.add(mapResultSetToCart(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carts;
    }


    @Override
    public boolean existsById(int cartId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_ID_SQL)) {

            statement.setInt(1, cartId);

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


    @Override
    public boolean existsByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);

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


    @Override
    public boolean updateCart(int cartId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_CART_SQL)) {

            statement.setInt(1, cartId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean clearCart(int cartId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(CLEAR_CART_SQL)) {

            statement.setInt(1, cartId);

            statement.executeUpdate();

            // Updating the cart timestamp after clearing its items
            try (PreparedStatement updateStatement =
                         connection.prepareStatement(
                                 UPDATE_CART_SQL)) {

                updateStatement.setInt(1, cartId);
                updateStatement.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteCart(int cartId) {

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // Delete cart items first
            try (PreparedStatement itemStatement =
                         connection.prepareStatement(CLEAR_CART_SQL);
                 PreparedStatement cartStatement =
                         connection.prepareStatement(DELETE_CART_SQL)) {

                itemStatement.setInt(1, cartId);
                itemStatement.executeUpdate();

                cartStatement.setInt(1, cartId);

                int rowsDeleted =
                        cartStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    connection.commit();
                    return true;
                }

                connection.rollback();
                return false;

            } catch (SQLException e) {

                connection.rollback();
                throw e;

            } finally {

                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public boolean deleteByUserId(int userId) {

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            Cart cart = findByUserId(userId);

            if (cart == null) {
                connection.rollback();
                return false;
            }

            try (PreparedStatement itemStatement =
                         connection.prepareStatement(CLEAR_CART_SQL);
                 PreparedStatement cartStatement =
                         connection.prepareStatement(
                                 DELETE_BY_USER_ID_SQL)) {

                itemStatement.setInt(1, cart.getCartId());
                itemStatement.executeUpdate();

                cartStatement.setInt(1, userId);

                int rowsDeleted =
                        cartStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    connection.commit();
                    return true;
                }

                connection.rollback();
                return false;

            } catch (SQLException e) {

                connection.rollback();
                throw e;

            } finally {

                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private Cart mapResultSetToCart(
            ResultSet resultSet)
            throws SQLException {

        Cart cart = new Cart();

        cart.setCartId(
                resultSet.getInt("cart_id"));

        cart.setUserId(
                resultSet.getInt("user_id"));

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            cart.setCreatedAt(
                    createdAt.toLocalDateTime());
        }

        Timestamp updatedAt =
                resultSet.getTimestamp("updated_at");

        if (updatedAt != null) {
            cart.setUpdatedAt(
                    updatedAt.toLocalDateTime());
        }

        return cart;
    }
}