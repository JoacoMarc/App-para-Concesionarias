package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entidad EquipamientoExtra - Sistema de Concesionaria
 * Representa equipamiento adicional que se puede agregar a los vehículos
 */
@Entity
@Table(name = "equipamiento_extra")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipamientoExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "El nombre del equipamiento es obligatorio")
    @Column(nullable = false, length = 100)
    @JsonProperty("nombre")
    private String nombre;

    @Column(length = 500)
    @JsonProperty("descripcion")
    private String descripcion;

    @NotNull(message = "El precio del equipamiento es obligatorio")
    @Positive(message = "El precio del equipamiento debe ser positivo")
    @Column(nullable = false)
    @JsonProperty("precio")
    private Double precio;

    @NotNull(message = "La categoría es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("categoria")
    private CategoriaEquipamiento categoria;

    @Column(nullable = false)
    @JsonProperty("disponible")
    private boolean disponible = true;

    @Column(length = 50)
    @JsonProperty("marca")
    private String marca;

    @Column(length = 50)
    @JsonProperty("modelo")
    private String modelo;

    // Constructors
    public EquipamientoExtra() {
    }

    public EquipamientoExtra(String nombre, String descripcion, Double precio, CategoriaEquipamiento categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }

    public EquipamientoExtra(String nombre, String descripcion, Double precio, CategoriaEquipamiento categoria, String marca, String modelo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca;
        this.modelo = modelo;
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

    public CategoriaEquipamiento getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEquipamiento categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "EquipamientoExtra{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", categoria=" + categoria +
                ", disponible=" + disponible +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                '}';
    }
} 