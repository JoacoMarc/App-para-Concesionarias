package com.grupo9.sistemaConcesionaria.exception;

/**
 * Excepción lanzada cuando se intenta registrar un cliente que ya existe
 */
public class DuplicateClienteException extends Exception {


    public DuplicateClienteException(String message) {
        super(message);
    }


    public DuplicateClienteException(String message, Throwable cause) {
        super(message, cause);
    }
} 