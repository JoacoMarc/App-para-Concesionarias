package com.grupo9.sistemaConcesionaria.model;

/**
 * Enum para definir los tipos de facturación según la legislación argentina
 */
public enum TipoFacturacion {
    CONSUMIDOR_FINAL("Consumidor Final"),
    RESPONSABLE_INSCRIPTO("Responsable Inscripto"),
    MONOTRIBUTISTA("Monotributista"),
    EXENTO("Exento"),
    NO_RESPONSABLE("No Responsable"),
    RESPONSABLE_NO_INSCRIPTO("Responsable No Inscripto"),
    EXTERIOR("Exterior");

    private final String descripcion;

    TipoFacturacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Determina si este tipo de facturación requiere factura A
     * @return true si requiere factura A, false si requiere factura B o C
     */
    public boolean requiereFacturaA() {
        return this == RESPONSABLE_INSCRIPTO || this == EXENTO;
    }

    /**
     * Determina si este tipo de facturación requiere CUIT/CUIL
     * @return true si requiere CUIT/CUIL obligatorio
     */
    public boolean requiereCuitCuil() {
        return this != CONSUMIDOR_FINAL;
    }

    @Override
    public String toString() {
        return descripcion;
    }
} 