package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entidad Administrador - Hereda de Usuario
 * Representa a los administradores del sistema con permisos completos
 */
@Entity
@Table(name = "administradores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Administrador extends Usuario {

    @Column(length = 100)
    @JsonProperty("area")
    @Size(max = 100, message = "El área no puede exceder 100 caracteres")
    private String area;

    // Constructors
    public Administrador() {
        super();
        setRol(RolUsuario.ADMINISTRADOR);
    }

    public Administrador(String nombre, String apellido, String mail, String password, String area) {
        super(nombre, apellido, mail, password, RolUsuario.ADMINISTRADOR);
        this.area = area;
    }

    public Administrador(String nombre, String apellido, String mail, String area) {
        super(nombre, apellido, mail, RolUsuario.ADMINISTRADOR);
        this.area = area;
    }

    // Business methods
    public void gestionarClientes() {
        System.out.println("Administrador " + getNombre() + ": Gestionando clientes...");
    }

    public void gestionarVehiculos() {
        System.out.println("Administrador " + getNombre() + ": Gestionando vehículos...");
    }

    public void gestionarPedidos() {
        System.out.println("Administrador " + getNombre() + ": Gestionando pedidos...");
    }

    public void generarInformes() {
        System.out.println("Administrador " + getNombre() + ": Generando informes...");
    }

    public void configurarSistema() {
        System.out.println("Administrador " + getNombre() + ": Configurando sistema...");
    }

    // Getters and Setters
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "idUsuario=" + getIdUsuario() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", mail='" + getMail() + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
} 