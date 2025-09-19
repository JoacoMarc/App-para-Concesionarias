package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entidad GarantiaExtendida - Sistema de Concesionaria
 * Representa planes de garantía adicionales para los vehículos
 */
@Entity
@Table(name = "garantia_extendida")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GarantiaExtendida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "El nombre del plan de garantía es obligatorio")
    @Column(nullable = false, length = 100)
    @JsonProperty("nombre")
    private String nombre;

    @Column(length = 500)
    @JsonProperty("descripcion")
    private String descripcion;

    @NotNull(message = "El precio de la garantía es obligatorio")
    @Positive(message = "El precio de la garantía debe ser positivo")
    @Column(nullable = false)
    @JsonProperty("precio")
    private Double precio;

    @NotNull(message = "La duración en meses es obligatoria")
    @Min(value = 1, message = "La duración mínima es 1 mes")
    @Column(nullable = false)
    @JsonProperty("duracionMeses")
    private Integer duracionMeses;

    @NotNull(message = "El límite de kilómetros es obligatorio")
    @Positive(message = "El límite de kilómetros debe ser positivo")
    @Column(nullable = false)
    @JsonProperty("limiteKilometros")
    private Integer limiteKilometros;

    @NotNull(message = "La cobertura es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("tipoCobertura")
    private TipoCobertura tipoCobertura;

    @Column(length = 1000)
    @JsonProperty("cobertura")
    private String cobertura;

    @Column(length = 1000)
    @JsonProperty("exclusiones")
    private String exclusiones;

    @Column(nullable = false)
    @JsonProperty("disponible")
    private boolean disponible = true;

    @Column(length = 100)
    @JsonProperty("proveedor")
    private String proveedor;

    // Constructors
    public GarantiaExtendida() {
    }

    public GarantiaExtendida(String nombre, String descripcion, Double precio, Integer duracionMeses, 
                            Integer limiteKilometros, TipoCobertura tipoCobertura) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMeses = duracionMeses;
        this.limiteKilometros = limiteKilometros;
        this.tipoCobertura = tipoCobertura;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public Integer getLimiteKilometros() {
        return limiteKilometros;
    }

    public void setLimiteKilometros(Integer limiteKilometros) {
        this.limiteKilometros = limiteKilometros;
    }

    public TipoCobertura getTipoCobertura() {
        return tipoCobertura;
    }

    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        this.tipoCobertura = tipoCobertura;
    }

    public String getCobertura() {
        return cobertura;
    }

    public void setCobertura(String cobertura) {
        this.cobertura = cobertura;
    }

    public String getExclusiones() {
        return exclusiones;
    }

    public void setExclusiones(String exclusiones) {
        this.exclusiones = exclusiones;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        return "GarantiaExtendida{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", duracionMeses=" + duracionMeses +
                ", limiteKilometros=" + limiteKilometros +
                ", tipoCobertura=" + tipoCobertura +
                ", disponible=" + disponible +
                ", proveedor='" + proveedor + '\'' +
                '}';
    }
} 