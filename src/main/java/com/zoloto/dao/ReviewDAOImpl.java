package com.zoloto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Review;
import com.zoloto.util.DBConnection;

public class ReviewDAOImpl implements ReviewDAO {

    // Create
    @Override
    public boolean createReview(Review review) {

        String sql = "INSERT INTO reviews "
                   + "(user_id, product_id, order_id, rating, comment) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());

            if (review.getOrderId() != null) {
                ps.setInt(3, review.getOrderId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        review.setReviewId(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Read
    @Override
    public Review findById(int reviewId) {

        String sql = "SELECT * FROM reviews WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reviewId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToReview(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Review findByUserIdAndProductId(
            int userId,
            int productId) {

        String sql = "SELECT * FROM reviews "
                   + "WHERE user_id = ? AND product_id = ? "
                   + "ORDER BY review_date DESC "
                   + "LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToReview(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Review> findByProductId(int productId) {

        String sql = "SELECT * FROM reviews "
                   + "WHERE product_id = ? "
                   + "ORDER BY review_date DESC";

        List<Review> reviews = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    public List<Review> findByUserId(int userId) {

        String sql = "SELECT * FROM reviews "
                   + "WHERE user_id = ? "
                   + "ORDER BY review_date DESC";

        List<Review> reviews = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    public List<Review> findAll() {

        String sql = "SELECT * FROM reviews "
                   + "ORDER BY review_date DESC";

        List<Review> reviews = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    // Rating / filtering
    @Override
    public List<Review> findByRating(int rating) {

        String sql = "SELECT * FROM reviews "
                   + "WHERE rating = ? "
                   + "ORDER BY review_date DESC";

        List<Review> reviews = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rating);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    public List<Review> findByProductIdAndRating(
            int productId,
            int rating) {

        String sql = "SELECT * FROM reviews "
                   + "WHERE product_id = ? AND rating = ? "
                   + "ORDER BY review_date DESC";

        List<Review> reviews = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, rating);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    public BigDecimal getAverageRating(int productId) {

        String sql = "SELECT AVG(rating) AS average_rating "
                   + "FROM reviews "
                   + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    BigDecimal average = rs.getBigDecimal("average_rating");

                    if (average != null) {
                        return average;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    @Override
    public int countByProductId(int productId) {

        String sql = "SELECT COUNT(*) AS total "
                   + "FROM reviews "
                   + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Existence
    @Override
    public boolean existsById(int reviewId) {

        String sql = "SELECT 1 FROM reviews "
                   + "WHERE review_id = ? "
                   + "LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reviewId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean existsByUserIdAndProductId(
            int userId,
            int productId) {

        String sql = "SELECT 1 FROM reviews "
                   + "WHERE user_id = ? AND product_id = ? "
                   + "LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update
    @Override
    public boolean updateReview(Review review) {

        String sql = "UPDATE reviews SET "
                   + "user_id = ?, "
                   + "product_id = ?, "
                   + "order_id = ?, "
                   + "rating = ?, "
                   + "comment = ? "
                   + "WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());

            if (review.getOrderId() != null) {
                ps.setInt(3, review.getOrderId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.setInt(6, review.getReviewId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateRating(
            int reviewId,
            int rating) {

        String sql = "UPDATE reviews "
                   + "SET rating = ? "
                   + "WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rating);
            ps.setInt(2, reviewId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateComment(
            int reviewId,
            String comment) {

        String sql = "UPDATE reviews "
                   + "SET comment = ? "
                   + "WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, comment);
            ps.setInt(2, reviewId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete
    @Override
    public boolean deleteReview(int reviewId) {

        String sql = "DELETE FROM reviews "
                   + "WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reviewId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteByUserIdAndProductId(
            int userId,
            int productId) {

        String sql = "DELETE FROM reviews "
                   + "WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteByProductId(int productId) {

        String sql = "DELETE FROM reviews "
                   + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteByUserId(int userId) {

        String sql = "DELETE FROM reviews "
                   + "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Helper method
    private Review mapResultSetToReview(ResultSet rs)
            throws SQLException {

        Review review = new Review();

        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setProductId(rs.getInt("product_id"));

        int orderId = rs.getInt("order_id");

        if (rs.wasNull()) {
            review.setOrderId(null);
        } else {
            review.setOrderId(orderId);
        }

        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));

        Timestamp timestamp = rs.getTimestamp("review_date");

        if (timestamp != null) {
            review.setReviewDate(timestamp.toLocalDateTime());
        } else {
            review.setReviewDate(null);
        }

        return review;
    }
}