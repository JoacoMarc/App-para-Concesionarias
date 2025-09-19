package com.grupo9.sistemaConcesionaria.exception;

/**
 * Excepción lanzada cuando se intenta registrar un usuario del sistema que ya existe
 */
public class DuplicateUsuarioException extends Exception {

    
    public DuplicateUsuarioException(String message) {
        super(message);
    }


    public DuplicateUsuarioException(String message, Throwable cause) {
        super(message, cause);
    }
} 