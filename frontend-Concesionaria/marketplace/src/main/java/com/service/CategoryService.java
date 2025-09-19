package com.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.entity.Category;
import com.exceptions.CategoryDuplicateException;
import com.exceptions.CategoryInUseException;

public interface CategoryService {
    List<Category> getCategories();
    Optional<Category> getCategoryById(UUID categoryId);
    Category createCategory(String description) throws CategoryDuplicateException;
    Category deleteCategory(UUID categoryId) throws CategoryInUseException;
    Category updateCategory(UUID categoryId, String description);
    Object getProductsByCategory(UUID categoryId);
}
