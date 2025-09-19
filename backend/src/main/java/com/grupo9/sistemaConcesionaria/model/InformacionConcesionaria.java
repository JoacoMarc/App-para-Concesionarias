package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

/**
 * Entidad InformacionConcesionaria - Sistema de Concesionaria
 * Almacena los datos de la empresa para informes y pedidos
 * Implementa patrón Singleton a nivel de aplicación
 */
@Entity
@Table(name = "informacion_concesionaria")
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformacionConcesionaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "El nombre de la concesionaria es obligatorio")
    @Column(nullable = false, length = 100)
    @JsonProperty("nombre")
    private String nombre;

    @NotBlank(message = "El CUIT de la concesionaria es obligatorio")
    @Pattern(regexp = "^(30|33|34)[-]?\\d{8}[-]?\\d{1}$", 
             message = "CUIT debe tener el formato válido argentino para empresas (ej: 30-12345678-9)")
    @Column(unique = true, nullable = false, length = 15)
    @JsonProperty("cuit")
    private String cuit;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false, length = 200)
    @JsonProperty("direccion")
    private String direccion;

    @Column(length = 100)
    @JsonProperty("ciudad")
    private String ciudad;

    @Column(length = 50)
    @JsonProperty("provincia")
    private String provincia;

    @Pattern(regexp = "^[A-Z]?\\d{4}[A-Z]{3}$", 
             message = "Código postal debe tener formato argentino (ej: C1425, 1425ABC)")
    @Column(length = 10)
    @JsonProperty("codigoPostal")
    private String codigoPostal;

    @Column(length = 20)
    @JsonProperty("telefono")
    private String telefono;

    @Column(length = 100)
    @JsonProperty("email")
    private String email;

    @Column(length = 100)
    @JsonProperty("sitioWeb")
    private String sitioWeb;

    @Column(length = 50)
    @JsonProperty("numeroHabilitacion")
    private String numeroHabilitacion;

    @Column(nullable = false)
    @JsonProperty("activo")
    private boolean activo = true;

    @Column(nullable = false)
    @JsonProperty("fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column
    @JsonProperty("fechaModificacion")
    private LocalDateTime fechaModificacion;

    // Constructors
    public InformacionConcesionaria() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public InformacionConcesionaria(String nombre, String cuit, String direccion) {
        this();
        this.nombre = nombre;
        this.cuit = cuit;
        this.direccion = direccion;
    }

    // Business Methods
    public String getDireccionCompleta() {
        StringBuilder direccionCompleta = new StringBuilder();
        direccionCompleta.append(direccion);
        if (ciudad != null) direccionCompleta.append(", ").append(ciudad);
        if (provincia != null) direccionCompleta.append(", ").append(provincia);
        if (codigoPostal != null) direccionCompleta.append(" (").append(codigoPostal).append(")");
        return direccionCompleta.toString();
    }

    public String getEncabezadoFactura() {
        return String.format("%s\nCUIT: %s\n%s", nombre, cuit, getDireccionCompleta());
    }

    public String getDatosContacto() {
        StringBuilder contacto = new StringBuilder();
        if (telefono != null) contacto.append("Tel: ").append(telefono);
        if (email != null) {
            if (contacto.length() > 0) contacto.append(" | ");
            contacto.append("Email: ").append(email);
        }
        if (sitioWeb != null) {
            if (contacto.length() > 0) contacto.append(" | ");
            contacto.append("Web: ").append(sitioWeb);
        }
        return contacto.toString();
    }

    public void actualizarModificacion() {
        this.fechaModificacion = LocalDateTime.now();
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
        actualizarModificacion();
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
        actualizarModificacion();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
        actualizarModificacion();
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
        actualizarModificacion();
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
        actualizarModificacion();
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
        actualizarModificacion();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
        actualizarModificacion();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        actualizarModificacion();
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
        actualizarModificacion();
    }

    public String getNumeroHabilitacion() {
        return numeroHabilitacion;
    }

    public void setNumeroHabilitacion(String numeroHabilitacion) {
        this.numeroHabilitacion = numeroHabilitacion;
        actualizarModificacion();
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        actualizarModificacion();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public String toString() {
        return "InformacionConcesionaria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cuit='" + cuit + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                '}';
    }
} 