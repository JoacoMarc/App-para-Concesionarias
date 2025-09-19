package com.grupo9.sistemaConcesionaria.exception;

/**
 * Excepción lanzada cuando no se encuentra un pedido específico
 */
public class PedidoNotFoundException extends Exception {

    public PedidoNotFoundException(String message) {
        super(message);
    }

    public PedidoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 