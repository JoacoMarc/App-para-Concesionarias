package com.grupo9.sistemaConcesionaria.model;

/**
 * Enum para definir las condiciones ante el IVA según la legislación argentina
 */
public enum CondicionIva {
    RESPONSABLE_INSCRIPTO("Responsable Inscripto", "RI", 21.0),
    MONOTRIBUTO("Monotributo", "M", 0.0),
    CONSUMIDOR_FINAL("Consumidor Final", "CF", 0.0),
    EXENTO("Exento", "EX", 0.0),
    NO_RESPONSABLE("No Responsable", "NR", 0.0),
    RESPONSABLE_NO_INSCRIPTO("Responsable No Inscripto", "RNI", 21.0),
    SUJETO_NO_CATEGORIZADO("Sujeto No Categorizado", "SNC", 0.0);

    private final String descripcion;
    private final String codigo;
    private final double porcentajeIva;

    CondicionIva(String descripcion, String codigo, double porcentajeIva) {
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.porcentajeIva = porcentajeIva;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getPorcentajeIva() {
        return porcentajeIva;
    }

    /**
     * Determina si esta condición está exenta del IVA
     * @return true si está exento del IVA
     */
    public boolean esExentoIva() {
        return porcentajeIva == 0.0;
    }

    /**
     * Determina si puede emitir facturas A
     * @return true si puede emitir facturas A
     */
    public boolean puedeEmitirFacturaA() {
        return this == RESPONSABLE_INSCRIPTO || this == EXENTO;
    }

    /**
     * Determina el tipo de factura que debe emitir
     * @return tipo de factura (A, B, C)
     */
    public String getTipoFactura() {
        if (puedeEmitirFacturaA()) {
            return "A";
        } else if (this == MONOTRIBUTO || this == RESPONSABLE_NO_INSCRIPTO) {
            return "B";
        } else {
            return "C";
        }
    }

    @Override
    public String toString() {
        return descripcion;
    }
} 