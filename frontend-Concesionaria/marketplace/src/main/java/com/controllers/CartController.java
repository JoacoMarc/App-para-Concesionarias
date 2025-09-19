package com.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Cart;
import com.entity.CartProduct;
import com.entity.Order;
import com.entity.Role;
import com.entity.User;
import com.entity.dto.AddProductToCartRequest;
import com.entity.dto.CartRequest;
import com.entity.dto.CreateOrderFromCartRequest;
import com.exceptions.CartNotFoundException;
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
@RequestMapping("carts")
public class CartController {

    @Autowired
    private CartService cartService;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    public void validateCartOwnership(UUID cartId) {
        User authenticatedUser = getAuthenticatedUser();
        Cart cart = cartService.getCartById(cartId)
                .orElseThrow(CartNotFoundException::new);
        
        if (!cart.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("No tienes permisos para acceder a este carrito");
        }
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable UUID cartId) throws CartNotFoundException {
        User authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Acceso denegado: Se requieren permisos de administrador");
        }
        Cart result = cartService.getCartById(cartId)
                .orElseThrow(CartNotFoundException::new);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<Cart>> getMyCart() {
        User authenticatedUser = getAuthenticatedUser();
        List<Cart> carts = cartService.findByUserId(authenticatedUser.getId());
        return ResponseEntity.ok(carts);
    }

    @PostMapping
    public ResponseEntity<Object> createCart(@RequestBody CartRequest cartRequest) {
        User authenticatedUser = getAuthenticatedUser();
        Cart result = cartService.createCart(authenticatedUser.getId(), cartRequest);
        return ResponseEntity.created(URI.create("/carts/" + result.getId())).body(result);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateCart(@PathVariable UUID cartId, @RequestBody CartRequest cartRequest)
            throws CartNotFoundException {
        validateCartOwnership(cartId);
        Cart result = cartService.updateCart(cartId, cartRequest);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Cart> deleteCart(@PathVariable UUID cartId)
            throws CartNotFoundException {
        validateCartOwnership(cartId);
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/product/{productId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable UUID cartId, @PathVariable UUID productId) {
        validateCartOwnership(cartId);
        Cart updatedCart = cartService.addProductToCart(cartId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable UUID cartId, @PathVariable UUID productId) {
        validateCartOwnership(cartId);
        Cart updatedCart = cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/{cartId}/products")
    public ResponseEntity<List<CartProduct>> getCartProducts(@PathVariable UUID cartId) {
        validateCartOwnership(cartId);
        List<CartProduct> cartProducts = cartService.getCartProducts(cartId);
        return ResponseEntity.ok(cartProducts);
    }

    @PostMapping("/{cartId}/products")
    public ResponseEntity<CartProduct> addProductToCartWithQuantity(
            @PathVariable UUID cartId, 
            @RequestBody AddProductToCartRequest request) {
        validateCartOwnership(cartId);
        CartProduct cartProduct = cartService.addProductToCartWithQuantity(
                cartId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cartProduct);
    }

    @PutMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartProduct> updateProductQuantity(
            @PathVariable UUID cartId, 
            @PathVariable UUID productId, 
            @RequestBody AddProductToCartRequest request) {
        validateCartOwnership(cartId);
        CartProduct cartProduct = cartService.updateProductQuantityInCart(
                cartId, productId, request.getQuantity());
        if (cartProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cartProduct);
    }

    
}
