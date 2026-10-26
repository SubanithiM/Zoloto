package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Wishlist;
import com.zoloto.util.DBConnection;

public class WishlistDAOImpl implements WishlistDAO {

    private static final String INSERT_WISHLIST_SQL =
            "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT wishlist_id, user_id, product_id, created_at " +
            "FROM wishlist WHERE wishlist_id = ?";

    private static final String FIND_BY_USER_AND_PRODUCT_SQL =
            "SELECT wishlist_id, user_id, product_id, created_at " +
            "FROM wishlist WHERE user_id = ? AND product_id = ?";

    private static final String FIND_BY_USER_ID_SQL =
            "SELECT wishlist_id, user_id, product_id, created_at " +
            "FROM wishlist " +
            "WHERE user_id = ? ORDER BY wishlist_id";

    private static final String FIND_BY_PRODUCT_ID_SQL =
            "SELECT wishlist_id, user_id, product_id, created_at " +
            "FROM wishlist " +
            "WHERE product_id = ? ORDER BY wishlist_id";

    private static final String FIND_ALL_SQL =
            "SELECT wishlist_id, user_id, product_id, created_at " +
            "FROM wishlist ORDER BY wishlist_id";

    private static final String EXISTS_SQL =
            "SELECT COUNT(*) FROM wishlist " +
            "WHERE user_id = ? AND product_id = ?";

    private static final String EXISTS_BY_ID_SQL =
            "SELECT COUNT(*) FROM wishlist WHERE wishlist_id = ?";

    private static final String COUNT_BY_USER_ID_SQL =
            "SELECT COUNT(*) FROM wishlist WHERE user_id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM wishlist WHERE wishlist_id = ?";

    private static final String DELETE_BY_USER_AND_PRODUCT_SQL =
            "DELETE FROM wishlist " +
            "WHERE user_id = ? AND product_id = ?";

    private static final String DELETE_BY_USER_ID_SQL =
            "DELETE FROM wishlist WHERE user_id = ?";

    private static final String DELETE_BY_PRODUCT_ID_SQL =
            "DELETE FROM wishlist WHERE product_id = ?";


    @Override
    public boolean addToWishlist(Wishlist wishlist) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_WISHLIST_SQL)) {

            statement.setInt(1, wishlist.getUserId());
            statement.setInt(2, wishlist.getProductId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Wishlist findById(int wishlistId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, wishlistId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToWishlist(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Wishlist findByUserIdAndProductId(
            int userId,
            int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_USER_AND_PRODUCT_SQL)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToWishlist(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Wishlist> findByUserId(int userId) {

        List<Wishlist> wishlists = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    wishlists.add(
                            mapResultSetToWishlist(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wishlists;
    }


    @Override
    public List<Wishlist> findByProductId(int productId) {

        List<Wishlist> wishlists = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_PRODUCT_ID_SQL)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    wishlists.add(
                            mapResultSetToWishlist(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wishlists;
    }


    @Override
    public List<Wishlist> findAll() {

        List<Wishlist> wishlists = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                wishlists.add(
                        mapResultSetToWishlist(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wishlists;
    }


    @Override
    public boolean exists(
            int userId,
            int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_SQL)) {

            statement.setInt(1, userId);
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
    public boolean existsById(int wishlistId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_ID_SQL)) {

            statement.setInt(1, wishlistId);

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
    public int countByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             COUNT_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);

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
    public boolean removeById(int wishlistId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_BY_ID_SQL)) {

            statement.setInt(1, wishlistId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean removeByUserIdAndProductId(
            int userId,
            int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_BY_USER_AND_PRODUCT_SQL)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean removeAllByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean removeAllByProductId(int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_BY_PRODUCT_ID_SQL)) {

            statement.setInt(1, productId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private Wishlist mapResultSetToWishlist(
            ResultSet resultSet)
            throws SQLException {

        Wishlist wishlist = new Wishlist();

        wishlist.setWishlistId(
                resultSet.getInt("wishlist_id"));

        wishlist.setUserId(
                resultSet.getInt("user_id"));

        wishlist.setProductId(
                resultSet.getInt("product_id"));

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            wishlist.setCreatedAt(
                    createdAt.toLocalDateTime());
        }

        return wishlist;
    }
}

