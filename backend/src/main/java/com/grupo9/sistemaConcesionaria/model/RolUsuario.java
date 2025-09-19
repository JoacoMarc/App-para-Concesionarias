package com.grupo9.sistemaConcesionaria.model;

/**
 * Enumeración que define los roles de usuario en el sistema
 */
public enum RolUsuario {
    ADMINISTRADOR("Administrador"),
    VENDEDOR("Vendedor"),
    CLIENTE("Cliente");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
} 