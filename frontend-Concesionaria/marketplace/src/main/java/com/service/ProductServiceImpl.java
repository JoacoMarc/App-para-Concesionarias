package com.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Category;
import com.entity.Product;
import com.exceptions.CategoryNotFoundException;
import com.exceptions.ProductDuplicateException;
import com.exceptions.ProductNotFoundException;
import com.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(UUID productId) {
        return productRepository.findById(productId);
    }


    public Product createProduct(String name, String description, double price, long stock, UUID categoryId) throws ProductDuplicateException, CategoryNotFoundException {
        List<Product> products = productRepository.findAll();
        if (products.stream().anyMatch(product -> 
            Objects.equals(product.getName(), name))) {
            throw new ProductDuplicateException();
        }
        
        Category category = categoryService.getCategoryById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException());
        
        return productRepository.save(new Product(name, description, price, stock, category));
    }

    public Product deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());
        productRepository.deleteById(productId);
        return product;
    }

    public Product updateProduct(UUID productId, String name, String description, Double price, Long stock, UUID categoryId) throws ProductNotFoundException, CategoryNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());
        
        // Solo actualizar campos que no sean null
        if (name != null) {
            product.setName(name);
        }
        if (description != null) {
            product.setDescription(description);
        }
        if (price != null) {
            product.setPrice(price);
        }
        if (stock != null) {
            product.setStock(stock);
        }
        if (categoryId != null && !categoryId.equals(product.getCategory().getId())) {
            Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException());
            product.setCategory(category);
        }
        
        return productRepository.save(product);
    }

    public List<Product> getProductsByCategory(UUID categoryId) throws CategoryNotFoundException {
        categoryService.getCategoryById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException());
        
        return productRepository.findAll().stream()
            .filter(product -> product.getCategory() != null && 
                    product.getCategory().getId().equals(categoryId))
            .collect(java.util.stream.Collectors.toList());
    }

}
