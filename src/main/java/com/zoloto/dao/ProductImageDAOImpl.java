package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.ProductImage;
import com.zoloto.util.DBConnection;

public class ProductImageDAOImpl implements ProductImageDAO {

    private static final String INSERT_IMAGE_SQL =
            "INSERT INTO product_images (product_id, image_url, is_primary) " +
            "VALUES (?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT image_id, product_id, image_url, is_primary " +
            "FROM product_images WHERE image_id = ?";

    private static final String FIND_BY_PRODUCT_ID_SQL =
            "SELECT image_id, product_id, image_url, is_primary " +
            "FROM product_images " +
            "WHERE product_id = ? " +
            "ORDER BY image_id";

    private static final String FIND_PRIMARY_IMAGE_SQL =
            "SELECT image_id, product_id, image_url, is_primary " +
            "FROM product_images " +
            "WHERE product_id = ? AND is_primary = true " +
            "LIMIT 1";

    private static final String FIND_PRIMARY_IMAGES_SQL =
            "SELECT image_id, product_id, image_url, is_primary " +
            "FROM product_images " +
            "WHERE is_primary = true " +
            "ORDER BY image_id";

    private static final String EXISTS_BY_ID_SQL =
            "SELECT COUNT(*) FROM product_images WHERE image_id = ?";

    private static final String COUNT_BY_PRODUCT_SQL =
            "SELECT COUNT(*) FROM product_images WHERE product_id = ?";

    private static final String UPDATE_IMAGE_SQL =
            "UPDATE product_images SET image_url = ?, is_primary = ? " +
            "WHERE image_id = ?";

    private static final String UPDATE_IMAGE_URL_SQL =
            "UPDATE product_images SET image_url = ? WHERE image_id = ?";

    private static final String UPDATE_PRIMARY_STATUS_SQL =
            "UPDATE product_images SET is_primary = ? WHERE image_id = ?";

    private static final String CLEAR_PRIMARY_IMAGES_SQL =
            "UPDATE product_images SET is_primary = false " +
            "WHERE product_id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM product_images WHERE image_id = ?";

    private static final String DELETE_BY_PRODUCT_ID_SQL =
            "DELETE FROM product_images WHERE product_id = ?";


    @Override
    public boolean createProductImage(ProductImage productImage) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_IMAGE_SQL)) {

            statement.setInt(1, productImage.getProductId());
            statement.setString(2, productImage.getImageUrl());
            statement.setBoolean(3, productImage.isPrimary());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public ProductImage findById(int imageId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, imageId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToProductImage(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<ProductImage> findByProductId(int productId) {

        List<ProductImage> images = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_PRODUCT_ID_SQL)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    images.add(mapResultSetToProductImage(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return images;
    }


    @Override
    public ProductImage findPrimaryImageByProductId(int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_PRIMARY_IMAGE_SQL)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToProductImage(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<ProductImage> findPrimaryImages() {

        List<ProductImage> images = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_PRIMARY_IMAGES_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                images.add(mapResultSetToProductImage(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return images;
    }


    @Override
    public boolean existsById(int imageId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_ID_SQL)) {

            statement.setInt(1, imageId);

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
    public int countByProductId(int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(COUNT_BY_PRODUCT_SQL)) {

            statement.setInt(1, productId);

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
    public boolean updateProductImage(ProductImage productImage) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_IMAGE_SQL)) {

            statement.setString(1, productImage.getImageUrl());
            statement.setBoolean(2, productImage.isPrimary());
            statement.setInt(3, productImage.getImageId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean setPrimaryImage(
            int productId,
            int imageId) {

        String setPrimarySql =
                "UPDATE product_images " +
                "SET is_primary = true " +
                "WHERE image_id = ? AND product_id = ?";

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement clearStatement =
                         connection.prepareStatement(
                                 CLEAR_PRIMARY_IMAGES_SQL);
                 PreparedStatement setStatement =
                         connection.prepareStatement(
                                 setPrimarySql)) {

                // First remove primary flag from all images
                clearStatement.setInt(1, productId);
                clearStatement.executeUpdate();

                // Then make the requested image primary
                setStatement.setInt(1, imageId);
                setStatement.setInt(2, productId);

                int rowsUpdated =
                        setStatement.executeUpdate();

                if (rowsUpdated > 0) {
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
    public boolean clearPrimaryImages(int productId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             CLEAR_PRIMARY_IMAGES_SQL)) {

            statement.setInt(1, productId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteProductImage(int imageId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_BY_ID_SQL)) {

            statement.setInt(1, imageId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteByProductId(int productId) {

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


    @Override
    public boolean updateImageUrl(
            int imageId,
            String imageUrl) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_IMAGE_URL_SQL)) {

            statement.setString(1, imageUrl);
            statement.setInt(2, imageId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updatePrimaryStatus(
            int imageId,
            boolean primary) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_PRIMARY_STATUS_SQL)) {

            statement.setBoolean(1, primary);
            statement.setInt(2, imageId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private ProductImage mapResultSetToProductImage(
            ResultSet resultSet)
            throws SQLException {

        ProductImage productImage = new ProductImage();

        productImage.setImageId(
                resultSet.getInt("image_id"));

        productImage.setProductId(
                resultSet.getInt("product_id"));

        productImage.setImageUrl(
                resultSet.getString("image_url"));

        productImage.setPrimary(
                resultSet.getBoolean("is_primary"));

        return productImage;
    }
}