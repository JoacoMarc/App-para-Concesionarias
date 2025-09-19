package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

/**
 * Entidad DatosFacturacion - Sistema de Concesionaria
 * Almacena información fiscal detallada de los clientes para facturación
 * Complementa a la entidad Cliente con datos específicos de facturación
 */
@Entity
@Table(name = "datos_facturacion")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosFacturacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonProperty("cliente")
    private Cliente cliente;

    @NotBlank(message = "La razón social es obligatoria")
    @Column(nullable = false, length = 150)
    @JsonProperty("razonSocial")
    private String razonSocial;

    @NotBlank(message = "El CUIT/CUIL es obligatorio")
    @Pattern(regexp = "^(20|23|24|27|30|33|34)[-]?\\d{8}[-]?\\d{1}$", 
             message = "CUIT/CUIL debe tener el formato válido argentino (ej: 20-12345678-9)")
    @Column(unique = true, nullable = false, length = 15)
    @JsonProperty("cuitCuil")
    private String cuitCuil;

    @NotNull(message = "El tipo de facturación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("tipoFacturacion")
    private TipoFacturacion tipoFacturacion;

    @NotBlank(message = "La dirección de facturación es obligatoria")
    @Column(nullable = false, length = 200)
    @JsonProperty("direccionFacturacion")
    private String direccionFacturacion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false, length = 100)
    @JsonProperty("ciudadFacturacion")
    private String ciudadFacturacion;

    @NotBlank(message = "La provincia es obligatoria")
    @Column(nullable = false, length = 50)
    @JsonProperty("provinciaFacturacion")
    private String provinciaFacturacion;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^[A-Z]?\\d{4}[A-Z]{3}$", 
             message = "Código postal debe tener formato argentino (ej: C1425, 1425ABC)")
    @Column(nullable = false, length = 10)
    @JsonProperty("codigoPostalFacturacion")
    private String codigoPostalFacturacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("condicionIva")
    private CondicionIva condicionIva;

    @Column(length = 100)
    @JsonProperty("actividadPrincipal")
    private String actividadPrincipal;

    @Column(nullable = false)
    @JsonProperty("activo")
    private boolean activo = true;

    @Column(nullable = false)
    @JsonProperty("fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column
    @JsonProperty("fechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(length = 500)
    @JsonProperty("observaciones")
    private String observaciones;

    // Constructors
    public DatosFacturacion() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public DatosFacturacion(Cliente cliente, String razonSocial, String cuitCuil, 
                           TipoFacturacion tipoFacturacion, String direccionFacturacion, 
                           String ciudadFacturacion, String provinciaFacturacion, 
                           String codigoPostalFacturacion) {
        this();
        this.cliente = cliente;
        this.razonSocial = razonSocial;
        this.cuitCuil = cuitCuil;
        this.tipoFacturacion = tipoFacturacion;
        this.direccionFacturacion = direccionFacturacion;
        this.ciudadFacturacion = ciudadFacturacion;
        this.provinciaFacturacion = provinciaFacturacion;
        this.codigoPostalFacturacion = codigoPostalFacturacion;
        
        // Determinar condición IVA según el tipo de facturación
        determinarCondicionIva();
    }

    // Business Methods
    public String getDireccionCompleta() {
        return String.format("%s, %s, %s (%s)", 
                direccionFacturacion, ciudadFacturacion, 
                provinciaFacturacion, codigoPostalFacturacion);
    }

    public boolean requiereFacturaA() {
        return tipoFacturacion.requiereFacturaA();
    }

    public void determinarCondicionIva() {
        switch (tipoFacturacion) {
            case RESPONSABLE_INSCRIPTO:
                this.condicionIva = CondicionIva.RESPONSABLE_INSCRIPTO;
                break;
            case MONOTRIBUTISTA:
                this.condicionIva = CondicionIva.MONOTRIBUTO;
                break;
            case CONSUMIDOR_FINAL:
                this.condicionIva = CondicionIva.CONSUMIDOR_FINAL;
                break;
            case EXENTO:
                this.condicionIva = CondicionIva.EXENTO;
                break;
            default:
                this.condicionIva = CondicionIva.CONSUMIDOR_FINAL;
        }
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
        actualizarModificacion();
    }

    public String getCuitCuil() {
        return cuitCuil;
    }

    public void setCuitCuil(String cuitCuil) {
        this.cuitCuil = cuitCuil;
        actualizarModificacion();
    }

    public TipoFacturacion getTipoFacturacion() {
        return tipoFacturacion;
    }

    public void setTipoFacturacion(TipoFacturacion tipoFacturacion) {
        this.tipoFacturacion = tipoFacturacion;
        determinarCondicionIva();
        actualizarModificacion();
    }

    public String getDireccionFacturacion() {
        return direccionFacturacion;
    }

    public void setDireccionFacturacion(String direccionFacturacion) {
        this.direccionFacturacion = direccionFacturacion;
        actualizarModificacion();
    }

    public String getCiudadFacturacion() {
        return ciudadFacturacion;
    }

    public void setCiudadFacturacion(String ciudadFacturacion) {
        this.ciudadFacturacion = ciudadFacturacion;
        actualizarModificacion();
    }

    public String getProvinciaFacturacion() {
        return provinciaFacturacion;
    }

    public void setProvinciaFacturacion(String provinciaFacturacion) {
        this.provinciaFacturacion = provinciaFacturacion;
        actualizarModificacion();
    }

    public String getCodigoPostalFacturacion() {
        return codigoPostalFacturacion;
    }

    public void setCodigoPostalFacturacion(String codigoPostalFacturacion) {
        this.codigoPostalFacturacion = codigoPostalFacturacion;
        actualizarModificacion();
    }

    public CondicionIva getCondicionIva() {
        return condicionIva;
    }

    public void setCondicionIva(CondicionIva condicionIva) {
        this.condicionIva = condicionIva;
        actualizarModificacion();
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public void setActividadPrincipal(String actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        actualizarModificacion();
    }

    @Override
    public String toString() {
        return "DatosFacturacion{" +
                "id=" + id +
                ", clienteId=" + (cliente != null ? cliente.getId() : null) +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuitCuil='" + cuitCuil + '\'' +
                ", tipoFacturacion=" + tipoFacturacion +
                ", condicionIva=" + condicionIva +
                ", direccionCompleta='" + getDireccionCompleta() + '\'' +
                ", activo=" + activo +
                '}';
    }
} 