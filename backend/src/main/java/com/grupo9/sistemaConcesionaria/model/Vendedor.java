package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entidad Vendedor - Hereda de Usuario
 * Representa a los vendedores del sistema con permisos limitados
 */
@Entity
@Table(name = "vendedores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Vendedor extends Usuario {

    @Column(length = 100)
    @JsonProperty("sucursal")
    @Size(max = 100, message = "La sucursal no puede exceder 100 caracteres")
    private String sucursal;

    @Column(name = "ventas_totales")
    @JsonProperty("ventasTotales")
    @Min(value = 0, message = "Las ventas totales no pueden ser negativas")
    private Integer ventasTotales = 0;

    // Constructors
    public Vendedor() {
        super();
        setRol(RolUsuario.VENDEDOR);
    }

    public Vendedor(String nombre, String apellido, String mail, String password, String sucursal) {
        super(nombre, apellido, mail, password, RolUsuario.VENDEDOR);
        this.sucursal = sucursal;
        this.ventasTotales = 0;
    }

    public Vendedor(String nombre, String apellido, String mail, String sucursal) {
        super(nombre, apellido, mail, RolUsuario.VENDEDOR);
        this.sucursal = sucursal;
        this.ventasTotales = 0;
    }

    // Business methods
    public void verVehiculosDisponibles() {
        System.out.println("Vendedor " + getNombre() + ": Consultando vehículos disponibles...");
    }

    public void verificarDisponibilidad(String chasis) {
        System.out.println("Vendedor " + getNombre() + ": Verificando disponibilidad del vehículo " + chasis);
    }

    public void incrementarVentas() {
        this.ventasTotales++;
        System.out.println("Vendedor " + getNombre() + ": Ventas totales: " + this.ventasTotales);
    }

    // Getters and Setters
    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public Integer getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(Integer ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "idUsuario=" + getIdUsuario() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", mail='" + getMail() + '\'' +
                ", sucursal='" + sucursal + '\'' +
                ", ventasTotales=" + ventasTotales +
                '}';
    }
} 