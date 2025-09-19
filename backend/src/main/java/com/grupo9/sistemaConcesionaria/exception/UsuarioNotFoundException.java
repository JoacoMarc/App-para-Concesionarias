package com.grupo9.sistemaConcesionaria.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario del sistema solicitado
 */
public class UsuarioNotFoundException extends Exception {


    public UsuarioNotFoundException(String message) {
        super(message);
    }

    public UsuarioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 