package com.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Product;
import com.entity.dto.ProductRequest;
import com.exceptions.CategoryNotFoundException;
import com.exceptions.ProductDuplicateException;
import com.exceptions.ProductNotFoundException;
import com.service.ProductServiceImpl;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID productId) throws ProductNotFoundException {
        Optional<Product> result = productService.getProductById(productId);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            throw new ProductNotFoundException();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest) throws ProductDuplicateException, CategoryNotFoundException {
        Product result = productService.createProduct(
            productRequest.getName(), 
            productRequest.getDescription(), 
            productRequest.getPrice() != null ? productRequest.getPrice() : 0.0, 
            productRequest.getStock() != null ? productRequest.getStock() : 0L,
            productRequest.getCategoryId()
        );
        return ResponseEntity.created(URI.create("/products/" + result.getId())).body(result);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID productId) throws ProductNotFoundException {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequest productRequest) throws ProductNotFoundException, CategoryNotFoundException {
        Product result = productService.updateProduct(
            productId, 
            productRequest.getName(), 
            productRequest.getDescription(), 
            productRequest.getPrice(),  
            productRequest.getStock(),
            productRequest.getCategoryId()
        );
        return ResponseEntity.ok(result);
    }
    
}
