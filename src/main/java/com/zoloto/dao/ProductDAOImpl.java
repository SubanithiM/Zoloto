
package com.zoloto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Product;
import com.zoloto.util.DBConnection;

public class ProductDAOImpl implements ProductDAO {

    // Fetch the product's primary image from product_images.
    private static final String BASE_SELECT =
            "SELECT product_id, category_id, product_name, description, " +
            "brand, sku, price, discount, stock_quantity, rating, status, created_at, " +
            "(SELECT pi.image_url FROM product_images pi " +
            "WHERE pi.product_id = products.product_id " +
            "AND pi.is_primary = 1 " +
            "ORDER BY pi.image_id LIMIT 1) AS image_url " +
            "FROM products ";

    private static final String INSERT_PRODUCT_SQL =
            "INSERT INTO products " +
            "(category_id, product_name, description, brand, sku, price, " +
            "discount, stock_quantity, rating, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            BASE_SELECT + "WHERE product_id = ?";

    private static final String FIND_BY_SKU_SQL =
            BASE_SELECT + "WHERE sku = ?";

    private static final String FIND_ALL_SQL =
            BASE_SELECT + "ORDER BY product_id";

    private static final String FIND_BY_CATEGORY_SQL =
            BASE_SELECT + "WHERE category_id = ? ORDER BY product_id";

    private static final String FIND_BY_BRAND_SQL =
            BASE_SELECT + "WHERE brand = ? ORDER BY product_id";

    private static final String FIND_BY_STATUS_SQL =
            BASE_SELECT + "WHERE status = ? ORDER BY product_id";

    private static final String SEARCH_BY_NAME_SQL =
            BASE_SELECT +
            "WHERE LOWER(product_name) LIKE ? ORDER BY product_id";

    private static final String SEARCH_BY_BRAND_SQL =
            BASE_SELECT +
            "WHERE LOWER(brand) LIKE ? ORDER BY product_id";

    private static final String SEARCH_SQL =
            BASE_SELECT +
            "WHERE LOWER(product_name) LIKE ? " +
            "OR LOWER(description) LIKE ? " +
            "OR LOWER(brand) LIKE ? " +
            "OR LOWER(sku) LIKE ? " +
            "ORDER BY product_id";

    private static final String FIND_BY_PRICE_RANGE_SQL =
            BASE_SELECT +
            "WHERE price BETWEEN ? AND ? ORDER BY price";

    private static final String FIND_BY_MINIMUM_RATING_SQL =
            BASE_SELECT +
            "WHERE rating >= ? ORDER BY rating DESC";

    private static final String FIND_IN_STOCK_SQL =
            BASE_SELECT +
            "WHERE stock_quantity > 0 ORDER BY product_id";

    private static final String FIND_OUT_OF_STOCK_SQL =
            BASE_SELECT +
            "WHERE stock_quantity <= 0 ORDER BY product_id";

    private static final String UPDATE_PRODUCT_SQL =
            "UPDATE products SET " +
            "category_id = ?, product_name = ?, description = ?, " +
            "brand = ?, sku = ?, price = ?, discount = ?, " +
            "stock_quantity = ?, rating = ?, status = ? " +
            "WHERE product_id = ?";

    private static final String UPDATE_STOCK_SQL =
            "UPDATE products SET stock_quantity = ? WHERE product_id = ?";

    private static final String UPDATE_PRICE_SQL =
            "UPDATE products SET price = ? WHERE product_id = ?";

    private static final String UPDATE_DISCOUNT_SQL =
            "UPDATE products SET discount = ? WHERE product_id = ?";

    private static final String UPDATE_RATING_SQL =
            "UPDATE products SET rating = ? WHERE product_id = ?";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE products SET status = ? WHERE product_id = ?";

    private static final String DELETE_PRODUCT_SQL =
            "DELETE FROM products WHERE product_id = ?";

    private static final String DEACTIVATE_PRODUCT_SQL =
            "UPDATE products SET status = 'INACTIVE' WHERE product_id = ?";

    private static final String COUNT_ALL_SQL =
            "SELECT COUNT(*) FROM products";

    private static final String COUNT_BY_CATEGORY_SQL =
            "SELECT COUNT(*) FROM products WHERE category_id = ?";

    private static final String COUNT_SEARCH_SQL =
            "SELECT COUNT(*) FROM products " +
            "WHERE LOWER(product_name) LIKE ? " +
            "OR LOWER(description) LIKE ? " +
            "OR LOWER(brand) LIKE ? " +
            "OR LOWER(sku) LIKE ?";

    @Override
    public boolean createProduct(Product product) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_PRODUCT_SQL)) {

            statement.setInt(1, product.getCategoryId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getBrand());
            statement.setString(5, product.getSku());
            statement.setBigDecimal(6, product.getPrice());
            statement.setBigDecimal(7, product.getDiscount());
            statement.setInt(8, product.getStockQuantity());
            statement.setBigDecimal(9, product.getRating());
            statement.setString(10, product.getStatus());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product findById(int productId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProduct(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product findBySku(String sku) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_SKU_SQL)) {

            statement.setString(1, sku);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProduct(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        return executeProductListQuery(FIND_ALL_SQL);
    }

    @Override
    public List<Product> findByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_CATEGORY_SQL)) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> findByBrand(String brand) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_BRAND_SQL)) {

            statement.setString(1, brand);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> findByStatus(String status) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_STATUS_SQL)) {

            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> searchByName(String keyword) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SEARCH_BY_NAME_SQL)) {

            statement.setString(1, "%" + keyword.toLowerCase() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> searchByBrand(String keyword) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SEARCH_BY_BRAND_SQL)) {

            statement.setString(1, "%" + keyword.toLowerCase() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> search(String keyword) {
        List<Product> products = new ArrayList<>();
        String pattern = "%" + keyword.toLowerCase() + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SEARCH_SQL)) {

            for (int i = 1; i <= 4; i++) {
                statement.setString(i, pattern);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> findByPriceRange(
            BigDecimal minPrice, BigDecimal maxPrice) {

        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_PRICE_RANGE_SQL)) {

            statement.setBigDecimal(1, minPrice);
            statement.setBigDecimal(2, maxPrice);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> findByMinimumRating(BigDecimal minimumRating) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_MINIMUM_RATING_SQL)) {

            statement.setBigDecimal(1, minimumRating);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> findInStock() {
        return executeProductListQuery(FIND_IN_STOCK_SQL);
    }

    @Override
    public List<Product> findOutOfStock() {
        return executeProductListQuery(FIND_OUT_OF_STOCK_SQL);
    }

    @Override
    public List<Product> filterProducts(
            Integer categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            BigDecimal minimumRating,
            String status) {

        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> parameters = new ArrayList<>();
        boolean hasCondition = false;

        if (categoryId != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("category_id = ?");
            parameters.add(categoryId);
            hasCondition = true;
        }

        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("brand = ?");
            parameters.add(brand);
            hasCondition = true;
        }

        if (minPrice != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("price >= ?");
            parameters.add(minPrice);
            hasCondition = true;
        }

        if (maxPrice != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("price <= ?");
            parameters.add(maxPrice);
            hasCondition = true;
        }

        if (minimumRating != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("rating >= ?");
            parameters.add(minimumRating);
            hasCondition = true;
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("status = ?");
            parameters.add(status);
        }

        sql.append(" ORDER BY product_id");
        return executeDynamicProductQuery(sql.toString(), parameters);
    }

    @Override
    public List<Product> findAllSorted(String sortBy, String sortDirection) {
        String sql = BASE_SELECT + "ORDER BY "
                + getSafeSortColumn(sortBy) + " "
                + getSafeSortDirection(sortDirection);

        return executeProductListQuery(sql);
    }

    @Override
    public List<Product> findPage(int pageNumber, int pageSize) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        int offset = (pageNumber - 1) * pageSize;
        String sql = BASE_SELECT + "ORDER BY product_id LIMIT ? OFFSET ?";
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pageSize);
            statement.setInt(2, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> searchAndFilter(
            String keyword,
            Integer categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            BigDecimal minimumRating,
            String status,
            String sortBy,
            String sortDirection,
            int pageNumber,
            int pageSize) {

        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> parameters = new ArrayList<>();
        boolean hasCondition = false;

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("WHERE (")
               .append("LOWER(product_name) LIKE ? ")
               .append("OR LOWER(description) LIKE ? ")
               .append("OR LOWER(brand) LIKE ? ")
               .append("OR LOWER(sku) LIKE ?)");
            String pattern = "%" + keyword.toLowerCase() + "%";
            for (int i = 0; i < 4; i++) {
                parameters.add(pattern);
            }
            hasCondition = true;
        }

        if (categoryId != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("category_id = ?");
            parameters.add(categoryId);
            hasCondition = true;
        }

        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("brand = ?");
            parameters.add(brand);
            hasCondition = true;
        }

        if (minPrice != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("price >= ?");
            parameters.add(minPrice);
            hasCondition = true;
        }

        if (maxPrice != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("price <= ?");
            parameters.add(maxPrice);
            hasCondition = true;
        }

        if (minimumRating != null) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("rating >= ?");
            parameters.add(minimumRating);
            hasCondition = true;
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(hasCondition ? " AND " : "WHERE ");
            sql.append("status = ?");
            parameters.add(status);
        }

        sql.append(" ORDER BY ")
           .append(getSafeSortColumn(sortBy))
           .append(" ")
           .append(getSafeSortDirection(sortDirection));

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        sql.append(" LIMIT ? OFFSET ?");
        parameters.add(pageSize);
        parameters.add((pageNumber - 1) * pageSize);

        return executeDynamicProductQuery(sql.toString(), parameters);
    }

    @Override
    public int countAll() {
        return executeCountQuery(COUNT_ALL_SQL);
    }

    @Override
    public int countByCategory(int categoryId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(COUNT_BY_CATEGORY_SQL)) {

            statement.setInt(1, categoryId);

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
    public int countSearchResults(String keyword) {
        String pattern = "%" + keyword.toLowerCase() + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(COUNT_SEARCH_SQL)) {

            for (int i = 1; i <= 4; i++) {
                statement.setString(i, pattern);
            }

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
    public boolean updateProduct(Product product) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_PRODUCT_SQL)) {

            statement.setInt(1, product.getCategoryId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getBrand());
            statement.setString(5, product.getSku());
            statement.setBigDecimal(6, product.getPrice());
            statement.setBigDecimal(7, product.getDiscount());
            statement.setInt(8, product.getStockQuantity());
            statement.setBigDecimal(9, product.getRating());
            statement.setString(10, product.getStatus());
            statement.setInt(11, product.getProductId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStock(int productId, int stockQuantity) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_STOCK_SQL)) {

            statement.setInt(1, stockQuantity);
            statement.setInt(2, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePrice(int productId, BigDecimal price) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_PRICE_SQL)) {

            statement.setBigDecimal(1, price);
            statement.setInt(2, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateDiscount(int productId, BigDecimal discount) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_DISCOUNT_SQL)) {

            statement.setBigDecimal(1, discount);
            statement.setInt(2, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRating(int productId, BigDecimal rating) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_RATING_SQL)) {

            statement.setBigDecimal(1, rating);
            statement.setInt(2, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatus(int productId, String status) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_STATUS_SQL)) {

            statement.setString(1, status);
            statement.setInt(2, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_PRODUCT_SQL)) {

            statement.setInt(1, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deactivateProduct(int productId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DEACTIVATE_PRODUCT_SQL)) {

            statement.setInt(1, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Product> executeProductListQuery(String sql) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private List<Product> executeDynamicProductQuery(
            String sql, List<Object> parameters) {

        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private int executeCountQuery(String sql) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getSafeSortColumn(String sortBy) {
        if (sortBy == null) {
            return "product_id";
        }

        return switch (sortBy.toLowerCase()) {
            case "name", "product_name" -> "product_name";
            case "price" -> "price";
            case "discount" -> "discount";
            case "rating" -> "rating";
            case "stock", "stock_quantity" -> "stock_quantity";
            case "brand" -> "brand";
            case "created", "created_at" -> "created_at";
            default -> "product_id";
        };
    }

    private String getSafeSortDirection(String sortDirection) {
        return "desc".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";
    }

    // Map database columns into the Product object, including image_url.
    private Product mapResultSetToProduct(ResultSet resultSet)
            throws SQLException {

        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setCategoryId(resultSet.getInt("category_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setDescription(resultSet.getString("description"));
        product.setBrand(resultSet.getString("brand"));
        product.setSku(resultSet.getString("sku"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setDiscount(resultSet.getBigDecimal("discount"));
        product.setStockQuantity(resultSet.getInt("stock_quantity"));
        product.setRating(resultSet.getBigDecimal("rating"));
        product.setStatus(resultSet.getString("status"));

        // This reads the primary image path from product_images.
        product.setImageUrl(resultSet.getString("image_url"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }

        return product;
    }
}
