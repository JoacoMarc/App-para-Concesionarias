package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Entidad Vehículo - Sistema de Concesionaria
 * Cumple con las consignas: id, marca, modelo, tipo, color, precioBase, características, chasis, motor
 * Ahora con soporte para persistencia JSON
 */
@Entity
@Table(name = "vehiculos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehiculo implements IVehiculo {

    @Id
    @JsonProperty("numeroChasis")
    private String numeroChasis;

    @NotBlank(message = "La marca es obligatoria")
    @Column(nullable = false, length = 50)
    @JsonProperty("marca")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Column(nullable = false, length = 50)
    @JsonProperty("modelo")
    private String modelo;

    @Column(length = 30)
    @JsonProperty("color")
    private String color;

    @Column(length = 50)
    @JsonProperty("numeroMotor")
    private String numeroMotor;

    @NotNull(message = "El precio base es obligatorio")
    @Positive(message = "El precio base debe ser positivo")
    @Column(nullable = false)
    @JsonProperty("precioBase")
    private Double precioBase;

    @Column(length = 500)
    @JsonProperty("caracteristicas")
    private String caracteristicas;

    @NotNull(message = "El tipo de vehículo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("tipo")
    private TipoVehiculo tipo;

    @Column(nullable = false)
    @JsonProperty("disponibleVenta")
    private boolean disponibleVenta = true;

    // Relación con pedidos
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Pedido> pedidos;

    // Constructors
    public Vehiculo() {
    }

    public Vehiculo(String marca, String modelo, String numeroChasis, String numeroMotor, Double precioBase, TipoVehiculo tipo) {
        this.marca = marca;
        this.modelo = modelo;
        this.numeroChasis = numeroChasis;
        this.numeroMotor = numeroMotor;
        this.precioBase = precioBase;
        this.tipo = tipo;
    }

    // Getters and Setters
    public String getNumeroChasis() {
        return numeroChasis;
    }

    public void setNumeroChasis(String numeroChasis) {
        this.numeroChasis = numeroChasis;
    }

    @Override
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumeroMotor() {
        return numeroMotor;
    }

    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    public boolean isDisponibleVenta() {
        return disponibleVenta;
    }

    public void setDisponibleVenta(boolean disponibleVenta) {
        this.disponibleVenta = disponibleVenta;
    }

    @Override
    public double getPrecioBase() {
        return precioBase != null ? precioBase : 0.0;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "numeroChasis='" + numeroChasis + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tipo=" + tipo +
                ", color='" + color + '\'' +
                ", numeroMotor='" + numeroMotor + '\'' +
                ", disponibleVenta=" + disponibleVenta +
                ", precioBase=" + precioBase +
                '}';
    }
} 