package com.service;

import com.entity.Order;
import com.entity.dto.OrderRequest;
import com.exceptions.OrderDuplicateException;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    List<Order> getOrders();
    Optional<Order> getOrderById(UUID orderId);
    Order createOrder(OrderRequest request) throws OrderDuplicateException;
    Order updateOrder(UUID orderId, OrderRequest request);
    Order deleteOrder(UUID orderId);
    List<Order> findByUserId(UUID userId);
    List<Order> findByProductId(UUID productId);
}
