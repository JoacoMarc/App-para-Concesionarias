package com.grupo9.sistemaConcesionaria.exception;

/**
 * Excepción lanzada cuando no se encuentra un cliente solicitado
 */
public class ClienteNotFoundException extends Exception {

    public ClienteNotFoundException(String message) {
        super(message);
    }

  
    public ClienteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 