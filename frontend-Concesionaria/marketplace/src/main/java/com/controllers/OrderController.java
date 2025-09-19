package com.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Order;
import com.entity.Role;
import com.entity.User;
import com.entity.dto.CreateOrderFromCartRequest;
import com.entity.dto.OrderRequest;
import com.exceptions.OrderNotFoundException;
import com.service.OrderService;
import com.service.CartService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        User authenticatedUser = getAuthenticatedUser();
        
        // Solo los administradores pueden ver todas las órdenes
        if (authenticatedUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Acceso denegado: Se requieren permisos de administrador");
        }
        
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) throws OrderNotFoundException {
        Order result = orderService.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders() {
        User authenticatedUser = getAuthenticatedUser();
        List<Order> orders = orderService.findByUserId(authenticatedUser.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable UUID userId) {
        User authenticatedUser = getAuthenticatedUser();
        
        // Solo los administradores pueden consultar órdenes de cualquier usuario
        if (authenticatedUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Acceso denegado: Se requieren permisos de administrador. Usa /my-orders para ver tus órdenes");
        }
        
        List<Order> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Order>> getOrdersByProductId(@PathVariable UUID productId) {
        List<Order> orders = orderService.findByProductId(productId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequest orderRequest) {
        Order result = orderService.createOrder(orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + result.getId())).body(result);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable UUID orderId, @RequestBody OrderRequest orderRequest)
            throws OrderNotFoundException {
        User authenticatedUser = getAuthenticatedUser();
        
        // Solo los administradores pueden actualizar órdenes
        if (authenticatedUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Acceso denegado: Se requieren permisos de administrador para actualizar órdenes");
        }
        
        Order result = orderService.updateOrder(orderId, orderRequest);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Order> deleteOrder(@PathVariable UUID orderId)
            throws OrderNotFoundException {
        User authenticatedUser = getAuthenticatedUser();
        
        // Solo los administradores pueden eliminar órdenes
        if (authenticatedUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Acceso denegado: Se requieren permisos de administrador para eliminar órdenes");
        }
        
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

   
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new RuntimeException("Usuario no autenticado");
    }


    private void validateCartOwnership(UUID cartId) {
        User authenticatedUser = getAuthenticatedUser();
        com.entity.Cart cart = cartService.getCartById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        
        if (!cart.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("No tienes permisos para acceder a este carrito");
        }
    }

   
    @PostMapping("/{cartId}")
    public ResponseEntity<Order> createOrderFromCart(
            @PathVariable UUID cartId, 
            @RequestBody CreateOrderFromCartRequest request) {
        validateCartOwnership(cartId);
        Order order = cartService.createOrderFromCart(cartId, request);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).body(order);
    }

    
}
