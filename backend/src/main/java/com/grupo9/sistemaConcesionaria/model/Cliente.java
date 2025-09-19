package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entidad Cliente - Hereda de Usuario
 * Representa a los clientes de la concesionaria que también son usuarios del sistema
 * Implementa ICliente para compatibilidad con el sistema existente
 * Incluye datos de facturación: razón social, CUIT/CUIL, dirección de facturación
 */
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Cliente extends Usuario implements ICliente {

    @NotBlank(message = "El documento es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    @JsonProperty("documento")
    private String documento;

    @Column(length = 200)
    @JsonProperty("direccion")
    private String direccion;

    // ===== DATOS DE FACTURACIÓN =====
    
    @Column(length = 150)
    @JsonProperty("razonSocial")
    private String razonSocial;

    @Pattern(regexp = "^(20|23|24|27|30|33|34)[-]?\\d{8}[-]?\\d{1}$", 
             message = "CUIT/CUIL debe tener el formato válido argentino (ej: 20-12345678-9)")
    @Column(unique = true, length = 15)
    @JsonProperty("cuitCuil")
    private String cuitCuil;

    @Column(length = 200)
    @JsonProperty("direccionFacturacion")
    private String direccionFacturacion;

    @Column(length = 100)
    @JsonProperty("ciudadFacturacion")
    private String ciudadFacturacion;

    @Column(length = 50)
    @JsonProperty("provinciaFacturacion")
    private String provinciaFacturacion;

    @Pattern(regexp = "^[A-Z]?\\d{4}[A-Z]{3}$", 
             message = "Código postal debe tener formato argentino (ej: C1425, 1425ABC)")
    @Column(length = 10)
    @JsonProperty("codigoPostalFacturacion")
    private String codigoPostalFacturacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("tipoFacturacion")
    private TipoFacturacion tipoFacturacion = TipoFacturacion.CONSUMIDOR_FINAL;

    @Column(nullable = false)
    @JsonProperty("requiereFacturaA")
    private boolean requiereFacturaA = false;

    // Constructors
    public Cliente() {
        super();
        setRol(RolUsuario.CLIENTE);
    }

    public Cliente(String nombre, String apellido, String mail, String documento, String telefono) {
        super(nombre, apellido, mail, RolUsuario.CLIENTE);
        this.documento = documento;
        setTelefono(telefono);
    }

    public Cliente(String nombre, String apellido, String mail, String password, String documento, String telefono) {
        super(nombre, apellido, mail, password, RolUsuario.CLIENTE);
        this.documento = documento;
        setTelefono(telefono);
    }

    public Cliente(String nombre, String apellido, String mail, String documento, String telefono, 
                   String cuitCuil, String razonSocial) {
        this(nombre, apellido, mail, documento, telefono);
        this.cuitCuil = cuitCuil;
        this.razonSocial = razonSocial;
        // Si tiene CUIT/CUIL, probablemente necesite factura A
        if (cuitCuil != null && !cuitCuil.isEmpty()) {
            this.requiereFacturaA = true;
            this.tipoFacturacion = TipoFacturacion.RESPONSABLE_INSCRIPTO;
        }
    }

    public Cliente(String nombre, String apellido, String mail, String password, String documento, String telefono, 
                   String cuitCuil, String razonSocial) {
        this(nombre, apellido, mail, password, documento, telefono);
        this.cuitCuil = cuitCuil;
        this.razonSocial = razonSocial;
        // Si tiene CUIT/CUIL, probablemente necesite factura A
        if (cuitCuil != null && !cuitCuil.isEmpty()) {
            this.requiereFacturaA = true;
            this.tipoFacturacion = TipoFacturacion.RESPONSABLE_INSCRIPTO;
        }
    }

    // Business methods
    public void verEstadoPedidos() {
        System.out.println("Cliente " + getNombre() + ": Consultando estado de pedidos...");
    }

    public void consultarCatalogoVehiculos() {
        System.out.println("Cliente " + getNombre() + ": Consultando catálogo de vehículos...");
    }

    // Métodos de facturación
    public boolean esResponsableInscripto() {
        return tipoFacturacion == TipoFacturacion.RESPONSABLE_INSCRIPTO;
    }

    public String getDireccionFacturacionCompleta() {
        StringBuilder direccionCompleta = new StringBuilder();
        if (direccionFacturacion != null) direccionCompleta.append(direccionFacturacion);
        if (ciudadFacturacion != null) direccionCompleta.append(", ").append(ciudadFacturacion);
        if (provinciaFacturacion != null) direccionCompleta.append(", ").append(provinciaFacturacion);
        if (codigoPostalFacturacion != null) direccionCompleta.append(" (").append(codigoPostalFacturacion).append(")");
        return direccionCompleta.toString();
    }

    public void configurarDatosFacturacion(String cuitCuil, String razonSocial, TipoFacturacion tipo) {
        this.cuitCuil = cuitCuil;
        this.razonSocial = razonSocial;
        this.tipoFacturacion = tipo;
        this.requiereFacturaA = (tipo == TipoFacturacion.RESPONSABLE_INSCRIPTO);
    }

    // ICliente implementation - delegamos a los métodos heredados de Usuario
    @Override
    public int getIdCliente() {
        return getIdUsuario() != null ? getIdUsuario().intValue() : 0;
    }

    @Override
    public String getCorreoElectronico() {
        return getMail();
    }

    // Getters and Setters específicos de Cliente
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Getters and Setters para datos de facturación
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuitCuil() {
        return cuitCuil;
    }

    public void setCuitCuil(String cuitCuil) {
        this.cuitCuil = cuitCuil;
    }

    public String getDireccionFacturacion() {
        return direccionFacturacion;
    }

    public void setDireccionFacturacion(String direccionFacturacion) {
        this.direccionFacturacion = direccionFacturacion;
    }

    public String getCiudadFacturacion() {
        return ciudadFacturacion;
    }

    public void setCiudadFacturacion(String ciudadFacturacion) {
        this.ciudadFacturacion = ciudadFacturacion;
    }

    public String getProvinciaFacturacion() {
        return provinciaFacturacion;
    }

    public void setProvinciaFacturacion(String provinciaFacturacion) {
        this.provinciaFacturacion = provinciaFacturacion;
    }

    public String getCodigoPostalFacturacion() {
        return codigoPostalFacturacion;
    }

    public void setCodigoPostalFacturacion(String codigoPostalFacturacion) {
        this.codigoPostalFacturacion = codigoPostalFacturacion;
    }

    public TipoFacturacion getTipoFacturacion() {
        return tipoFacturacion;
    }

    public void setTipoFacturacion(TipoFacturacion tipoFacturacion) {
        this.tipoFacturacion = tipoFacturacion;
    }

    public boolean isRequiereFacturaA() {
        return requiereFacturaA;
    }

    public void setRequiereFacturaA(boolean requiereFacturaA) {
        this.requiereFacturaA = requiereFacturaA;
    }

    // Métodos de conveniencia para compatibilidad con JSON y APIs
    public Long getId() {
        return getIdUsuario();
    }

    public void setId(Long id) {
        setIdUsuario(id);
    }

    public void setCorreoElectronico(String correoElectronico) {
        setMail(correoElectronico);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idUsuario=" + getIdUsuario() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", documento='" + documento + '\'' +
                ", mail='" + getMail() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", direccion='" + direccion + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuitCuil='" + cuitCuil + '\'' +
                ", tipoFacturacion=" + tipoFacturacion +
                ", requiereFacturaA=" + requiereFacturaA +
                '}';
    }
} 