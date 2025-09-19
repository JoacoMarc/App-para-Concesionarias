package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Clase HistorialEstado - Registro del historial de estados de un pedido
 * Según consignas: fecha, áreaResponsable, estado
 * Ahora con soporte para persistencia JSON
 */
@Embeddable
public class HistorialEstado {

    @Column(nullable = false)
    @JsonProperty("fecha")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("estado")
    private EstadoPedido estado;

    @Column(nullable = false, length = 100)
    @JsonProperty("areaResponsable")
    private String areaResponsable;

    @Column(length = 500)
    @JsonProperty("observaciones")
    private String observaciones;

    // Constructors
    public HistorialEstado() {
        this.fecha = LocalDateTime.now();
    }

    public HistorialEstado(EstadoPedido estado, String areaResponsable) {
        this();
        this.estado = estado;
        this.areaResponsable = areaResponsable;
    }

    public HistorialEstado(EstadoPedido estado, String areaResponsable, String observaciones) {
        this(estado, areaResponsable);
        this.observaciones = observaciones;
    }

    // Getters and Setters
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(String areaResponsable) {
        this.areaResponsable = areaResponsable;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "HistorialEstado{" +
                "fecha=" + fecha +
                ", estado=" + estado +
                ", areaResponsable='" + areaResponsable + '\'' +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
} 