package com.grupo9.sistemaConcesionaria.model;

/**
 * Categorizar los accesorios
 */
public enum CategoriaAccesorio {
    EXTERIOR("Exterior"),
    INTERIOR("Interior"),
    PROTECCION("Protección"),
    TRANSPORTE("Transporte y Carga"),
    ILUMINACION("Iluminación"),
    AUDIO("Audio y Video"),
    DECORATIVO("Decorativo"),
    FUNCIONAL("Funcional"),
    SEGURIDAD("Seguridad"),
    DEPORTIVO("Deportivo");

    private final String descripcion;

    CategoriaAccesorio(String descripcion) {
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