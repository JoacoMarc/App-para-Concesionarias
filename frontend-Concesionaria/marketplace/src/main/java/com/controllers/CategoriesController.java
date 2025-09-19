package com.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Category;
import com.entity.Product;
import com.entity.dto.CategoryRequest;
import com.exceptions.CategoryDuplicateException;
import com.exceptions.CategoryInUseException;
import com.exceptions.CategoryNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.service.CategoryService;
import com.service.ProductService;


@RestController
@RequestMapping("categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID categoryId) throws CategoryNotFoundException {
        Category result = categoryService.getCategoryById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable UUID categoryId) throws CategoryNotFoundException {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(categoryRequest.getDescription());
        return ResponseEntity.created(URI.create("/categories/" + result.getId())).body(result);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable UUID categoryId)
            throws CategoryNotFoundException, CategoryInUseException {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryRequest categoryRequest) throws CategoryNotFoundException {
        Category result = categoryService.updateCategory(categoryId, categoryRequest.getDescription());
        return ResponseEntity.ok(result);
    }
}
