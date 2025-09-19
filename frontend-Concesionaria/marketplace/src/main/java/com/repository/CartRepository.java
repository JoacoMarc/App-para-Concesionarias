package com.repository;

import com.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    // Buscar todos los carritos de un usuario
    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    List<Cart> findByUserId(UUID userId);

    // Buscar carritos que contengan cierto producto
    @Query("SELECT c FROM Cart c JOIN c.cartProducts cp WHERE cp.product.id = ?1")
    List<Cart> findByProductsId(UUID productId);
}
