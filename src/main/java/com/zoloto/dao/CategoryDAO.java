package com.zoloto.dao;

import java.util.List;

import com.zoloto.model.Category;

public interface CategoryDAO {

    // Create
    boolean createCategory(Category category);

    // Read
    Category findById(int categoryId);

    Category findByName(String categoryName);

    List<Category> findAll();

    List<Category> findByStatus(String status);

    // Existence check
    boolean existsByName(String categoryName);

    // Update
    boolean updateCategory(Category category);

    boolean updateStatus(int categoryId, String status);

    // Delete
    boolean deleteCategory(int categoryId);
}