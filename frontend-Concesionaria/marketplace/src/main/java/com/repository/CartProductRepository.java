package com.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.CartProduct;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, UUID> {
    
    List<CartProduct> findByCartId(UUID cartId);
    
    Optional<CartProduct> findByCartIdAndProductId(UUID cartId, UUID productId);
    
    void deleteByCartIdAndProductId(UUID cartId, UUID productId);
    
    void deleteByCartId(UUID cartId);
}