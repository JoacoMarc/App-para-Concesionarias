package com.grupo9.sistemaConcesionaria.model;

/**
 * Enum EstadoPedido - Estados del flujo de pedidos
 * Según consignas: Pendiente → Ventas → Cobranzas → Impuestos → Embarque → Logística → Entrega
 */
public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    VENTAS("Ventas"),
    COBRANZAS("Cobranzas"),
    IMPUESTOS("Impuestos"),
    EMBARQUE("Embarque"),
    LOGISTICA("Logística"),
    ENTREGA("Entrega"),
    COMPLETADO("Completado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el siguiente estado en el flujo normal
     * @return siguiente estado o null si es el último
     */
    public EstadoPedido getSiguienteEstado() {
        switch (this) {
            case VENTAS: return COBRANZAS;
            case COBRANZAS: return IMPUESTOS;
            case IMPUESTOS: return EMBARQUE;
            case EMBARQUE: return LOGISTICA;
            case LOGISTICA: return ENTREGA;
            case ENTREGA: return COMPLETADO;
            default: return null; // Estados finales
        }
    }

    /**
     * Verifica si es un estado final
     * @return true si el estado es final
     */
    public boolean isFinal() {
        return this == COMPLETADO || this == CANCELADO;
    }
} 