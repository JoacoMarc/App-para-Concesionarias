package com.grupo9.sistemaConcesionaria.exception;

/**
 * Excepción lanzada cuando no se encuentra un vehículo solicitado
 */
public class VehiculoNotFoundException extends Exception {


    public VehiculoNotFoundException(String message) {
        super(message);
    }

    public VehiculoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 