package com.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart carrito;

    @Column(name = "status", nullable = false)
    private String status;

    // Información de productos en formato JSON
    @Column(name = "products_json", columnDefinition = "TEXT")
    private String productsJson;

    // Información del usuario para la orden
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "billing_address", nullable = false)
    private String billingAddress;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    // Total de la orden
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = java.time.LocalDateTime.now();
        }
    }
}
