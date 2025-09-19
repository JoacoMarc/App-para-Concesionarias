package com.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.entity.Cart;
import com.entity.User;
import com.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.entity.Order;
import com.entity.dto.OrderRequest;
import com.exceptions.OrderDuplicateException;
import com.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequest request) throws OrderDuplicateException {
        User user = userService.getById(request.getUserId());

        Cart carrito = cartService.getCartById(request.getCartId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        Order order = new Order();
        order.setUser(user);
        order.setCarrito(carrito);
        order.setStatus("CREATED");
        order.setCreatedAt(java.time.LocalDateTime.now());

        return orderRepository.save(order);
    }


    @Override
    @Transactional
    public Order updateOrder(UUID orderId, OrderRequest request) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        if (request.getCartId() != null) {
            Cart cart = cartService.getCartById(request.getCartId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
            existingOrder.setCarrito(cart);

        }

        if (request.getStatus() != null) {
            existingOrder.setStatus(request.getStatus());
        }

        return orderRepository.save(existingOrder);
    }


    @Override
    @Transactional
    public Order deleteOrder(UUID orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        orderRepository.delete(existingOrder);
        return existingOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUserId(UUID userId) {
        return orderRepository.findByUserId(UUID.fromString(userId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByProductId(UUID productId) {
        return orderRepository.findByProductsId(UUID.fromString(productId.toString()));
    }

}
