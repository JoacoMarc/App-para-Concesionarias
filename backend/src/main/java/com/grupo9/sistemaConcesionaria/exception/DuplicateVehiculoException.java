package com.grupo9.sistemaConcesionaria.exception;

/**
 * Exception thrown when an attempt is made to add a vehicle that already exists.
 */
public class DuplicateVehiculoException extends ConcesionariaException {

    public DuplicateVehiculoException(String message) {
        super(message);
    }
} 