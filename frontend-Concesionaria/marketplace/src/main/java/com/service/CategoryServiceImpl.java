package com.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.entity.Category;
import com.entity.Product;
import com.exceptions.CategoryDuplicateException;
import com.exceptions.CategoryInUseException;
import com.exceptions.CategoryNotFoundException;
import com.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category createCategory(String description) throws CategoryDuplicateException {
        List<Category> categories = categoryRepository.findAll();
        if (categories.stream().anyMatch(category -> category.getDescription().equals(description))) {
            throw new CategoryDuplicateException();
        }
        return categoryRepository.save(new Category(description));
    }

    public List<Product> getProductsByCategory(UUID categoryId) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());
        return category.getProducts();
    }

    public Category deleteCategory(UUID categoryId) throws CategoryInUseException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());
        
        // Verificar si la categoría tiene productos asociados
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new CategoryInUseException();
        }
        
        categoryRepository.deleteById(categoryId);
        return category;
    }

    public Category updateCategory(UUID categoryId, String description) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    
        
}
