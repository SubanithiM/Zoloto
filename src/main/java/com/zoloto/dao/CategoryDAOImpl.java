package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Category;
import com.zoloto.util.DBConnection;

public class CategoryDAOImpl implements CategoryDAO {

    // Insert a category, including its image URL
    private static final String INSERT_CATEGORY_SQL =
            "INSERT INTO categories " +
            "(category_name, description, status, image_url) " +
            "VALUES (?, ?, ?, ?)";

    // Find a category by its ID
    private static final String FIND_BY_ID_SQL =
            "SELECT category_id, category_name, description, status, " +
            "image_url, created_at " +
            "FROM categories WHERE category_id = ?";

    // Find a category by its name
    private static final String FIND_BY_NAME_SQL =
            "SELECT category_id, category_name, description, status, " +
            "image_url, created_at " +
            "FROM categories WHERE category_name = ?";

    // Get all categories, including their image URLs
    private static final String FIND_ALL_SQL =
            "SELECT category_id, category_name, description, status, " +
            "image_url, created_at " +
            "FROM categories ORDER BY category_id";

    // Get categories by status
    private static final String FIND_BY_STATUS_SQL =
            "SELECT category_id, category_name, description, status, " +
            "image_url, created_at " +
            "FROM categories WHERE status = ? ORDER BY category_id";

    // Check whether a category name already exists
    private static final String EXISTS_BY_NAME_SQL =
            "SELECT COUNT(*) FROM categories WHERE category_name = ?";

    // Update category details and image URL
    private static final String UPDATE_CATEGORY_SQL =
            "UPDATE categories SET " +
            "category_name = ?, description = ?, status = ?, image_url = ? " +
            "WHERE category_id = ?";

    // Update category status
    private static final String UPDATE_STATUS_SQL =
            "UPDATE categories SET status = ? WHERE category_id = ?";

    // Delete a category
    private static final String DELETE_CATEGORY_SQL =
            "DELETE FROM categories WHERE category_id = ?";


    @Override
    public boolean createCategory(Category category) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_CATEGORY_SQL)) {

            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());
            statement.setString(3, category.getStatus());
            statement.setString(4, category.getImageUrl());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Category findById(int categoryId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCategory(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Category findByName(String categoryName) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_NAME_SQL)) {

            statement.setString(1, categoryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCategory(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Category> findAll() {

        List<Category> categories = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }


    @Override
    public List<Category> findByStatus(String status) {

        List<Category> categories = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_STATUS_SQL)) {

            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    categories.add(mapResultSetToCategory(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }


    @Override
    public boolean existsByName(String categoryName) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_NAME_SQL)) {

            statement.setString(1, categoryName);

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
    public boolean updateCategory(Category category) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_CATEGORY_SQL)) {

            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());
            statement.setString(3, category.getStatus());
            statement.setString(4, category.getImageUrl());
            statement.setInt(5, category.getCategoryId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateStatus(int categoryId, String status) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_STATUS_SQL)) {

            statement.setString(1, status);
            statement.setInt(2, categoryId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteCategory(int categoryId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_CATEGORY_SQL)) {

            statement.setInt(1, categoryId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Convert a database row into a Category object
    private Category mapResultSetToCategory(ResultSet resultSet)
            throws SQLException {

        Category category = new Category();

        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        category.setDescription(resultSet.getString("description"));
        category.setStatus(resultSet.getString("status"));

        // This line retrieves the image URL from MySQL
        category.setImageUrl(resultSet.getString("image_url"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            category.setCreatedAt(createdAt.toLocalDateTime());
        }

        return category;
    }
}