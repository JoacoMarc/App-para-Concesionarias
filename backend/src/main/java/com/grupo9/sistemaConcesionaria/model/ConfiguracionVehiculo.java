package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad ConfiguracionVehiculo - Sistema de Concesionaria
 * Representa la configuración adicional de un vehículo con equipamiento, garantías y accesorios
 */
@Entity
@Table(name = "configuracion_vehiculo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfiguracionVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotNull(message = "El vehículo es obligatorio")
    @OneToOne
    @JoinColumn(name = "vehiculo_numero_chasis", nullable = false)
    @JsonProperty("vehiculo")
    private Vehiculo vehiculo;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "configuracion_equipamiento",
        joinColumns = @JoinColumn(name = "configuracion_id"),
        inverseJoinColumns = @JoinColumn(name = "equipamiento_id")
    )
    @JsonProperty("equipamientosExtra")
    private List<EquipamientoExtra> equipamientosExtra = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "configuracion_garantia",
        joinColumns = @JoinColumn(name = "configuracion_id"),
        inverseJoinColumns = @JoinColumn(name = "garantia_id")
    )
    @JsonProperty("garantiasExtendidas")
    private List<GarantiaExtendida> garantiasExtendidas = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
        name = "configuracion_accesorio",
        joinColumns = @JoinColumn(name = "configuracion_id"),
        inverseJoinColumns = @JoinColumn(name = "accesorio_id")
    )
    @JsonProperty("accesorios")
    private List<Accesorio> accesorios = new ArrayList<>();

    @Column(nullable = false)
    @JsonProperty("precioTotal")
    private Double precioTotal = 0.0;

    @Column(nullable = false)
    @JsonProperty("activa")
    private boolean activa = true;

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
    public ConfiguracionVehiculo() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public ConfiguracionVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        this.fechaCreacion = LocalDateTime.now();
        this.precioTotal = vehiculo.getPrecioBase();
    }

    // Business Methods
    public void agregarEquipamiento(EquipamientoExtra equipamiento) {
        if (!this.equipamientosExtra.contains(equipamiento)) {
            this.equipamientosExtra.add(equipamiento);
            actualizarPrecioTotal();
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public void removerEquipamiento(EquipamientoExtra equipamiento) {
        if (this.equipamientosExtra.remove(equipamiento)) {
            actualizarPrecioTotal();
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public void agregarGarantia(GarantiaExtendida garantia) {
        if (!this.garantiasExtendidas.contains(garantia)) {
            this.garantiasExtendidas.add(garantia);
            actualizarPrecioTotal();
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public void removerGarantia(GarantiaExtendida garantia) {
        if (this.garantiasExtendidas.remove(garantia)) {
            actualizarPrecioTotal();
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public void agregarAccesorio(Accesorio accesorio) {
        if (!this.accesorios.contains(accesorio)) {
            this.accesorios.add(accesorio);
            actualizarPrecioTotal();
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public void removerAccesorio(Accesorio accesorio) {
        if (this.accesorios.remove(accesorio)) {
            actualizarPrecioTotal();
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public void actualizarPrecioTotal() {
        this.precioTotal = vehiculo.getPrecioBase();
        
        // Sumar equipamientos
        this.precioTotal += equipamientosExtra.stream()
            .mapToDouble(EquipamientoExtra::getPrecio)
            .sum();
        
        // Sumar garantías
        this.precioTotal += garantiasExtendidas.stream()
            .mapToDouble(GarantiaExtendida::getPrecio)
            .sum();
        
        // Sumar accesorios
        this.precioTotal += accesorios.stream()
            .mapToDouble(Accesorio::getPrecio)
            .sum();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<EquipamientoExtra> getEquipamientosExtra() {
        return equipamientosExtra;
    }

    public void setEquipamientosExtra(List<EquipamientoExtra> equipamientosExtra) {
        this.equipamientosExtra = equipamientosExtra;
    }

    public List<GarantiaExtendida> getGarantiasExtendidas() {
        return garantiasExtendidas;
    }

    public void setGarantiasExtendidas(List<GarantiaExtendida> garantiasExtendidas) {
        this.garantiasExtendidas = garantiasExtendidas;
    }

    public List<Accesorio> getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(List<Accesorio> accesorios) {
        this.accesorios = accesorios;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
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
    }

    @Override
    public String toString() {
        return "ConfiguracionVehiculo{" +
                "id=" + id +
                ", vehiculo=" + (vehiculo != null ? vehiculo.getNumeroChasis() : null) +
                ", equipamientosExtra=" + equipamientosExtra.size() +
                ", garantiasExtendidas=" + garantiasExtendidas.size() +
                ", accesorios=" + accesorios.size() +
                ", precioTotal=" + precioTotal +
                ", activa=" + activa +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
} 