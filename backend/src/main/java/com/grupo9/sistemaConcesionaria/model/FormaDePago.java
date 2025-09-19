package com.grupo9.sistemaConcesionaria.model;

/**
 * Enum FormaDePago - Formas de pago disponibles
 * Según consignas: Contado | Transferencia | Tarjeta
 */
public enum FormaDePago {
    CONTADO("Contado", "Pago en efectivo"),
    TRANSFERENCIA("Transferencia", "Transferencia bancaria"),
    TARJETA("Tarjeta", "Tarjeta de crédito");

    private final String codigo;
    private final String descripcion;

    FormaDePago(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Verifica si la forma de pago requiere validación adicional
     * @return true si requiere validación
     */
    public boolean requiereValidacion() {
        return this == TARJETA || this == TRANSFERENCIA;
    }

    /**
     * Obtiene el tiempo de procesamiento estimado en días
     * @return días estimados para procesamiento
     */
    public int getTiempoProcesamiento() {
        switch (this) {
            case CONTADO: return 0; // Inmediato
            case TRANSFERENCIA: return 1; // 1 día hábil
            case TARJETA: return 2; // 2 días hábiles
            default: return 0;
        }
    }
} 