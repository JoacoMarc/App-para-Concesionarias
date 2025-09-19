package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entidad Accesorio
 * Representa accesorios adicionales para los vehículos
 */
@Entity
@Table(name = "accesorios")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Accesorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "El nombre del accesorio es obligatorio")
    @Column(nullable = false, length = 100)
    @JsonProperty("nombre")
    private String nombre;

    @Column(length = 500)
    @JsonProperty("descripcion")
    private String descripcion;

    @NotNull(message = "El precio del accesorio es obligatorio")
    @Positive(message = "El precio del accesorio debe ser positivo")
    @Column(nullable = false)
    @JsonProperty("precio")
    private Double precio;

    @NotNull(message = "La categoría es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("categoria")
    private CategoriaAccesorio categoria;

    @Column(nullable = false)
    @JsonProperty("disponible")
    private boolean disponible = true;

    @Column(length = 50)
    @JsonProperty("marca")
    private String marca;

    @Column(length = 50)
    @JsonProperty("modelo")
    private String modelo;

    @Column(length = 100)
    @JsonProperty("material")
    private String material;

    @Column(length = 50)
    @JsonProperty("color")
    private String color;

    @Column(nullable = false)
    @JsonProperty("requiereInstalacion")
    private boolean requiereInstalacion = false;

    @Column(length = 200)
    @JsonProperty("compatibilidad")
    private String compatibilidad;

    // Constructors
    public Accesorio() {
    }

    public Accesorio(String nombre, String descripcion, Double precio, CategoriaAccesorio categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Accesorio(String nombre, String descripcion, Double precio, CategoriaAccesorio categoria, 
                     String marca, String modelo, String material, String color) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca;
        this.modelo = modelo;
        this.material = material;
        this.color = color;
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

    public CategoriaAccesorio getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAccesorio categoria) {
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isRequiereInstalacion() {
        return requiereInstalacion;
    }

    public void setRequiereInstalacion(boolean requiereInstalacion) {
        this.requiereInstalacion = requiereInstalacion;
    }

    public String getCompatibilidad() {
        return compatibilidad;
    }

    public void setCompatibilidad(String compatibilidad) {
        this.compatibilidad = compatibilidad;
    }

    @Override
    public String toString() {
        return "Accesorio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", categoria=" + categoria +
                ", disponible=" + disponible +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", material='" + material + '\'' +
                ", color='" + color + '\'' +
                ", requiereInstalacion=" + requiereInstalacion +
                ", compatibilidad='" + compatibilidad + '\'' +
                '}';
    }
} 