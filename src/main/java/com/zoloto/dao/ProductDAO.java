package com.zoloto.dao;

import java.math.BigDecimal;
import java.util.List;

import com.zoloto.model.Product;

public interface ProductDAO {

    // Create
    boolean createProduct(Product product);

    // Read
    Product findById(int productId);

    Product findBySku(String sku);

    List<Product> findAll();

    List<Product> findByCategory(int categoryId);

    List<Product> findByBrand(String brand);

    List<Product> findByStatus(String status);

    // Search
    List<Product> searchByName(String keyword);

    List<Product> searchByBrand(String keyword);

    List<Product> search(String keyword);

    // Price filters
    List<Product> findByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice);

    // Rating filters
    List<Product> findByMinimumRating(BigDecimal minimumRating);

    // Stock filters
    List<Product> findInStock();

    List<Product> findOutOfStock();

    // Combined filtering
    List<Product> filterProducts(
            Integer categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            BigDecimal minimumRating,
            String status);

    // Sorting and pagination
    List<Product> findAllSorted(
            String sortBy,
            String sortDirection);

    List<Product> findPage(
            int pageNumber,
            int pageSize);

    List<Product> searchAndFilter(
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
            int pageSize);

    // Count
    int countAll();

    int countByCategory(int categoryId);

    int countSearchResults(String keyword);

    // Update
    boolean updateProduct(Product product);

    boolean updateStock(int productId, int stockQuantity);

    boolean updatePrice(int productId, BigDecimal price);

    boolean updateDiscount(int productId, BigDecimal discount);

    boolean updateRating(int productId, BigDecimal rating);

    boolean updateStatus(int productId, String status);

    // Delete
    boolean deleteProduct(int productId);

    boolean deactivateProduct(int productId);
}