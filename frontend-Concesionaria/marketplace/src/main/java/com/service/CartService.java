package com.service;

import com.entity.Cart;
import com.entity.CartProduct;
import com.entity.Order;
import com.entity.dto.AddProductToCartRequest;
import com.entity.dto.CartRequest;
import com.entity.dto.CreateOrderFromCartRequest;
import com.exceptions.CartDuplicateException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartService {
    List<Cart> getCarts();

    Optional<Cart> getCartById(UUID cartId);

    Cart createCart(UUID userId, CartRequest request) throws CartDuplicateException;

    Cart updateCart(UUID cartId, CartRequest request);

    Cart deleteCart(UUID cartId);

    List<Cart> findByUserId(UUID userId);

    List<Cart> findByProductId(Long productId);

    Cart addProductToCart(UUID cartId, UUID productId);

    Cart removeProductFromCart(UUID cartId, UUID productId);
    
    CartProduct addProductToCartWithQuantity(UUID cartId, UUID productId, Integer quantity);
    
    CartProduct updateProductQuantityInCart(UUID cartId, UUID productId, Integer quantity);
    
    List<CartProduct> getCartProducts(UUID cartId);
    
    // Método para crear orden desde carrito
    Order createOrderFromCart(UUID cartId, CreateOrderFromCartRequest request);
}
