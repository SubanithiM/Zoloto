package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.CartItem;
import com.zoloto.util.DBConnection;

public class CartItemDAOImpl implements CartItemDAO {

    private static final String INSERT_CART_ITEM_SQL =
            "INSERT INTO cart_items (cart_id, product_id, quantity) " +
            "VALUES (?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT cart_item_id, cart_id, product_id, quantity " +
            "FROM cart_items WHERE cart_item_id = ?";

    private static final String FIND_BY_CART_AND_PRODUCT_SQL =
            "SELECT cart_item_id, cart_id, product_id, quantity " +
            "FROM cart_items " +
            "WHERE cart_id = ? AND product_id = ?";

    private static final String FIND_BY_CART_ID_SQL =
            "SELECT cart_item_id, cart_id, product_id, quantity " +
            "FROM cart_items " +
            "WHERE cart_id = ? " +
            "ORDER BY cart_item_id";

    private static final String FIND_BY_PRODUCT_ID_SQL =
            "SELECT cart_item_id, cart_id, product_id, quantity " +
            "FROM cart_items " +
            "WHERE product_id = ? " +
            "ORDER BY cart_item_id";

    private static final String EXISTS_BY_ID_SQL =
            "SELECT COUNT(*) FROM cart_items WHERE cart_item_id = ?";

    private static final String EXISTS_BY_CART_AND_PRODUCT_SQL =
            "SELECT COUNT(*) FROM cart_items " +
            "WHERE cart_id = ? AND product_id = ?";

    private static final String COUNT_BY_CART_ID_SQL =
            "SELECT COUNT(*) FROM cart_items WHERE cart_id = ?";

    private static final String UPDATE_QUANTITY_SQL =
            "UPDATE cart_items SET quantity = ? " +
            "WHERE cart_item_id = ?";

    private static final String INCREMENT_QUANTITY_SQL =
            "UPDATE cart_items SET quantity = quantity + ? " +
            "WHERE cart_item_id = ?";

    private static final String DECREMENT_QUANTITY_SQL =
            "UPDATE cart_items SET quantity = quantity - ? " +
            "WHERE cart_item_id = ? AND quantity >= ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM cart_items WHERE cart_item_id = ?";

    private static final String DELETE_BY_CART_AND_PRODUCT_SQL =
            "DELETE FROM cart_items " +
            "WHERE cart_id = ? AND product_id = ?";

    private static final String CLEAR_CART_SQL =
            "DELETE FROM cart_items WHERE cart_id = ?";

    private static final String TOTAL_QUANTITY_SQL =
            "SELECT COALESCE(SUM(quantity), 0) " +
            "FROM cart_items WHERE cart_id = ?";


    @Override
    public boolean addCartItem(CartItem cartItem) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_CART_ITEM_SQL)) {

            statement.setInt(1, cartItem.getCartId());
            statement.setInt(2, cartItem.getProductId());
            statement.setInt(3, cartItem.getQuantity());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public CartItem findById(int cartItemId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, cartItemId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToCartItem(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public CartItem findByCartIdAndProductId(
            int cartId,
            int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_CART_AND_PRODUCT_SQL)) {

            statement.setInt(1, cartId);
            statement.setInt(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToCartItem(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<CartItem> findByCartId(int cartId) {

        List<CartItem> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_CART_ID_SQL)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapResultSetToCartItem(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }


    @Override
    public List<CartItem> findByProductId(int productId) {

        List<CartItem> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_PRODUCT_ID_SQL)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapResultSetToCartItem(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }


    @Override
    public boolean existsById(int cartItemId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_ID_SQL)) {

            statement.setInt(1, cartItemId);

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
    public boolean existsByCartIdAndProductId(
            int cartId,
            int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             EXISTS_BY_CART_AND_PRODUCT_SQL)) {

            statement.setInt(1, cartId);
            statement.setInt(2, productId);

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
    public int countByCartId(int cartId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(COUNT_BY_CART_ID_SQL)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    public boolean updateQuantity(
            int cartItemId,
            int quantity) {

        if (quantity <= 0) {
            return false;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_QUANTITY_SQL)) {

            statement.setInt(1, quantity);
            statement.setInt(2, cartItemId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean incrementQuantity(
            int cartItemId,
            int amount) {

        if (amount <= 0) {
            return false;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INCREMENT_QUANTITY_SQL)) {

            statement.setInt(1, amount);
            statement.setInt(2, cartItemId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean decrementQuantity(
            int cartItemId,
            int amount) {

        if (amount <= 0) {
            return false;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DECREMENT_QUANTITY_SQL)) {

            statement.setInt(1, amount);
            statement.setInt(2, cartItemId);
            statement.setInt(3, amount);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteById(int cartItemId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_BY_ID_SQL)) {

            statement.setInt(1, cartItemId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteByCartIdAndProductId(
            int cartId,
            int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_BY_CART_AND_PRODUCT_SQL)) {

            statement.setInt(1, cartId);
            statement.setInt(2, productId);

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

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public int getTotalQuantity(int cartId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(TOTAL_QUANTITY_SQL)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private CartItem mapResultSetToCartItem(
            ResultSet resultSet)
            throws SQLException {

        CartItem cartItem = new CartItem();

        cartItem.setCartItemId(
                resultSet.getInt("cart_item_id"));

        cartItem.setCartId(
                resultSet.getInt("cart_id"));

        cartItem.setProductId(
                resultSet.getInt("product_id"));

        cartItem.setQuantity(
                resultSet.getInt("quantity"));

        return cartItem;
    }
}