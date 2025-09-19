package com.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE c.description = ?1")
    List<Category> findByDescription(String description);

    @Query("SELECT c FROM Category c WHERE c.id = ?1")
    Optional<Category> findById(UUID id);
    
}
