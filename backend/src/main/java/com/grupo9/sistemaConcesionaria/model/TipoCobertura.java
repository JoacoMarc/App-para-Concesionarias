package com.grupo9.sistemaConcesionaria.model;

/**
 * Enum para definir los tipos de cobertura de garantía
 */
public enum TipoCobertura {
    BASICA("Cobertura Básica"),
    EXTENDIDA("Cobertura Extendida"),
    COMPLETA("Cobertura Completa"),
    MECANICA("Cobertura Mecánica"),
    ELECTRICA("Cobertura Eléctrica"),
    CARROCERIA("Cobertura de Carrocería"),
    TODO_RIESGO("Todo Riesgo"),
    PREMIUM("Cobertura Premium");

    private final String descripcion;

    TipoCobertura(String descripcion) {
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