package com.repository;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE p.description = :description")
    List<Product> findByDescription(@Param("description") String description);

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findByName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.price = :price")
    List<Product> findByPrice(@Param("price") double price);

    @Query("SELECT p FROM Product p WHERE p.stock = :stock")
    List<Product> findByStock(@Param("stock") long stock);
    
}
