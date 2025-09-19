package com.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.entity.User;
import com.entity.Cart;
import com.entity.CartProduct;
import com.entity.Order;
import com.entity.Product;
import com.entity.dto.CartRequest;
import com.entity.dto.CreateOrderFromCartRequest;
import com.entity.dto.OrderProductItem;
import com.exceptions.CartDuplicateException;
import com.exceptions.CartNotFoundException;
import com.repository.CartRepository;
import com.repository.CartProductRepository;
import com.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final com.repository.ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Cart> getCarts() {
        return cartRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cart> getCartById(UUID cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    @Transactional
    public Cart createCart(UUID userId, CartRequest request) throws CartDuplicateException {
        User user = userService.getById(userId);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(java.time.LocalDateTime.now());
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart updateCart(UUID cartId, CartRequest request) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        // if (request.getProducts() != null) {
        // existingCart.setProducts(request.getProducts());
        // }
        return cartRepository.save(existingCart);
    }

    @Override
    @Transactional
    public Cart deleteCart(UUID cartId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        cartRepository.delete(existingCart);
        return existingCart;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cart> findByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cart> findByProductId(Long productId) {
        return cartRepository.findByProductsId(UUID.fromString(productId.toString()));
    }

    @Override
    @Transactional
    public Cart addProductToCart(UUID cartId, UUID productId) {
        addProductToCartWithQuantity(cartId, productId, 1);
        return cartRepository.findById(cartId)
                .orElseThrow(com.exceptions.CartNotFoundException::new);
    }

    @Override
    @Transactional
    public Cart removeProductFromCart(UUID cartId, UUID productId) {
        cartProductRepository.deleteByCartIdAndProductId(cartId, productId);
        return cartRepository.findById(cartId)
                .orElseThrow(com.exceptions.CartNotFoundException::new);
    }

    @Override
    @Transactional
    public CartProduct addProductToCartWithQuantity(UUID cartId, UUID productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(com.exceptions.CartNotFoundException::new);
        com.entity.Product product = productRepository.findById(productId)
                .orElseThrow(() -> new com.exceptions.ProductNotFoundException("Product not found"));
        
        // Verificar si el producto ya existe en el carrito
        Optional<CartProduct> existingCartProduct = cartProductRepository.findByCartIdAndProductId(cartId, productId);
        
        if (existingCartProduct.isPresent()) {
            // Si existe, sumar la cantidad
            CartProduct cartProduct = existingCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            return cartProductRepository.save(cartProduct);
        } else {
            // Si no existe, crear nuevo
            CartProduct cartProduct = new CartProduct(cart, product, quantity);
            return cartProductRepository.save(cartProduct);
        }
    }

    @Override
    @Transactional
    public CartProduct updateProductQuantityInCart(UUID cartId, UUID productId, Integer quantity) {
        CartProduct cartProduct = cartProductRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new com.exceptions.ProductNotFoundException("Product not found in cart"));
        
        if (quantity <= 0) {
            cartProductRepository.delete(cartProduct);
            return null;
        }
        
        cartProduct.setQuantity(quantity);
        return cartProductRepository.save(cartProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartProduct> getCartProducts(UUID cartId) {
        return cartProductRepository.findByCartId(cartId);
    }

    @Override
    @Transactional
    public Order createOrderFromCart(UUID cartId, CreateOrderFromCartRequest request) {
        // Obtener el carrito con sus productos
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Carrito no encontrado"));
        
        List<CartProduct> cartProducts = cartProductRepository.findByCartId(cartId);
        
        if (cartProducts.isEmpty()) {
            throw new RuntimeException("No se puede crear una orden con un carrito vacío");
        }
        
        // Crear la lista de productos para el JSON
        List<OrderProductItem> orderItems = cartProducts.stream()
                .map(cartProduct -> {
                    Product product = cartProduct.getProduct();
                    BigDecimal price = BigDecimal.valueOf(product.getPrice());
                    BigDecimal quantity = BigDecimal.valueOf(cartProduct.getQuantity());
                    BigDecimal subtotal = price.multiply(quantity);
                    
                    return new OrderProductItem(
                            product.getId(),
                            product.getName(),
                            price,
                            cartProduct.getQuantity(),
                            subtotal
                    );
                })
                .collect(Collectors.toList());
        
        // Calcular el total
        BigDecimal total = orderItems.stream()
                .map(OrderProductItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Convertir la lista de productos a JSON
        String productsJson;
        try {
            productsJson = objectMapper.writeValueAsString(orderItems);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir productos a JSON", e);
        }
        
        // Crear la orden
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setCarrito(cart);
        order.setStatus("PENDING");
        order.setProductsJson(productsJson);
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setIsPaid(request.getIsPaid());
        order.setTotal(total);
        order.setCreatedAt(java.time.LocalDateTime.now());
        
        return orderRepository.save(order);
    }
}
