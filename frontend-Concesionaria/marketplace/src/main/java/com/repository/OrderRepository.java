package com.repository;

import com.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Buscar todas las órdenes de un usuario
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findByUserId(UUID userId);

    // Buscar órdenes que contengan cierto producto
    @Query("SELECT o FROM Order o JOIN o.carrito c JOIN c.cartProducts cp WHERE cp.product.id = ?1")
    List<Order> findByProductsId(UUID productId);

}
