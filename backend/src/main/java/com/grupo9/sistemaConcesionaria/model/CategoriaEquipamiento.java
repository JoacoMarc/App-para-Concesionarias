package com.grupo9.sistemaConcesionaria.model;

/**
 * Enum para categorizar el equipamiento extra
 */
public enum CategoriaEquipamiento {
    SEGURIDAD("Seguridad"),
    CONFORT("Confort"),
    TECNOLOGIA("Tecnología"),
    ENTRETENIMIENTO("Entretenimiento"),
    ESTETICO("Estético"),
    RENDIMIENTO("Rendimiento"),
    ILUMINACION("Iluminación"),
    CLIMATIZACION("Climatización");

    private final String descripcion;

    CategoriaEquipamiento(String descripcion) {
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