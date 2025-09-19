package com.grupo9.sistemaConcesionaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo9.sistemaConcesionaria.model.state.EstadoPedidoState;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Pedido - Sistema de Concesionaria
 * Cumple con las consignas: númeroPedido, fechaCreación, cliente, vehículo, 
 * formaPago, impuestosAplicados, costoTotal, datosFacturación, vendedor, 
 * estadoActual, historialEstados
 * Ahora con soporte para persistencia JSON y patrón State
 */
@Entity
@Table(name = "pedidos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idPedido")
    private Long idPedido;

    @Column(nullable = false, unique = true, length = 50)
    @JsonProperty("numeroDePedido")
    private String numeroDePedido;

    @Column(nullable = false)
    @JsonProperty("fechaCreacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonProperty("cliente")
    private Cliente cliente;

    @NotNull(message = "El vehículo es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonProperty("vehiculo")
    private Vehiculo vehiculo;

    @NotNull(message = "La forma de pago es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("formaDePago")
    private FormaDePago formaDePago;

    @Positive(message = "Los impuestos deben ser positivos")
    @Column(nullable = false)
    @JsonProperty("impuestosAplicados")
    private Double impuestosAplicados = 0.0;

    @Positive(message = "El costo total debe ser positivo")
    @Column(nullable = false)
    @JsonProperty("costoTotal")
    private Double costoTotal = 0.0;

    @Column(length = 1000)
    @JsonProperty("datosFacturacion")
    private String datosFacturacion;

    @Column(length = 100)
    @JsonProperty("vendedor")
    private String vendedor = "Sistema";

    @NotNull(message = "El estado actual es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("estadoActual")
    private EstadoPedido estadoActual = EstadoPedido.PENDIENTE;

    @Column(length = 100)
    @JsonProperty("areaResponsableActual")
    private String areaResponsableActual = "Ventas";

    // ===== PATRÓN STATE =====
    
    @Transient // No persiste en BD, se maneja en tiempo de ejecución
    @com.fasterxml.jackson.annotation.JsonIgnore
    private EstadoPedidoState estadoState;

    @ElementCollection
    @CollectionTable(name = "pedido_historial", joinColumns = @JoinColumn(name = "pedido_id"))
    @JsonProperty("historialEstados")
    private List<HistorialEstado> historialEstados = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "pedido_configuraciones", joinColumns = @JoinColumn(name = "pedido_id"))
    @Column(name = "configuracion")
    @JsonProperty("configuracionesAdicionales")
    private List<String> configuracionesAdicionales = new ArrayList<>();

    // Constructors
    public Pedido() {
        this.fechaCreacion = LocalDateTime.now();
        this.numeroDePedido = generateNumeroPedido();
        addToHistorial(EstadoPedido.VENTAS, "Ventas");
    }

    public Pedido(Cliente cliente, Vehiculo vehiculo, FormaDePago formaDePago) {
        this();
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.formaDePago = formaDePago;
    }

    // ===== MÉTODOS DEL PATRÓN STATE =====
    
    /**
     * Procesa el pedido usando el patrón State
     */
    public void procesarConState() throws Exception {
        if (estadoState != null) {
            estadoState.procesar(this);
        }
    }

    /**
     * Avanza al siguiente estado si es posible
     */
    public boolean avanzarEstado() throws Exception {
        if (estadoState != null && estadoState.puedeAvanzar(this)) {
            EstadoPedidoState siguienteEstado = estadoState.getSiguienteEstado();
            if (siguienteEstado != null) {
                this.estadoState = siguienteEstado;
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si puede avanzar al siguiente estado
     */
    public boolean puedeAvanzar() {
        return estadoState != null && estadoState.puedeAvanzar(this);
    }

    /**
     * Obtiene información del estado actual
     */
    public String getEstadoInfo() {
        if (estadoState != null) {
            return estadoState.getNombreEstado() + ": " + estadoState.getDescripcionEstado();
        }
        return estadoActual.getDescripcion();
    }

    // Business methods
    public void agregarConfiguracion(String configuracion) {
        this.configuracionesAdicionales.add(configuracion);
    }

    public void calcularCostoTotal() {
        // Precio base del vehículo + configuraciones + impuestos
        double subtotal = vehiculo.getPrecioBase();
        // TODO: Agregar costo de configuraciones adicionales
        this.costoTotal = subtotal + this.impuestosAplicados;
    }

    public void cambiarEstado(EstadoPedido nuevoEstado, String nuevaArea) {
        this.estadoActual = nuevoEstado;
        this.areaResponsableActual = nuevaArea;
        addToHistorial(nuevoEstado, nuevaArea);
    }

    private void addToHistorial(EstadoPedido estado, String area) {
        HistorialEstado historial = new HistorialEstado();
        historial.setFecha(LocalDateTime.now());
        historial.setEstado(estado);
        historial.setAreaResponsable(area);
        this.historialEstados.add(historial);
    }

    private String generateNumeroPedido() {
        return "PED-" + System.currentTimeMillis();
    }

    // Getters and Setters
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getNumeroDePedido() {
        return numeroDePedido;
    }

    public void setNumeroDePedido(String numeroDePedido) {
        this.numeroDePedido = numeroDePedido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<String> getConfiguracionesAdicionales() {
        return configuracionesAdicionales;
    }

    public void setConfiguracionesAdicionales(List<String> configuracionesAdicionales) {
        this.configuracionesAdicionales = configuracionesAdicionales;
    }

    public FormaDePago getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(FormaDePago formaDePago) {
        this.formaDePago = formaDePago;
    }

    public Double getImpuestosAplicados() {
        return impuestosAplicados;
    }

    public void setImpuestosAplicados(Double impuestosAplicados) {
        this.impuestosAplicados = impuestosAplicados;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getDatosFacturacion() {
        return datosFacturacion;
    }

    public void setDatosFacturacion(String datosFacturacion) {
        this.datosFacturacion = datosFacturacion;
    }

    public String getVendedorNombre() {
        return vendedor;
    }

    public void setVendedorNombre(String vendedor) {
        this.vendedor = vendedor;
    }

    public EstadoPedido getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoPedido estadoActual) {
        this.estadoActual = estadoActual;
    }

    public String getAreaResponsableActual() {
        return areaResponsableActual;
    }

    public void setAreaResponsableActual(String areaResponsableActual) {
        this.areaResponsableActual = areaResponsableActual;
    }

    public List<HistorialEstado> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<HistorialEstado> historialEstados) {
        this.historialEstados = historialEstados;
    }

    // ===== GETTER/SETTER PARA STATE PATTERN =====
    
    public EstadoPedidoState getEstadoState() {
        return estadoState;
    }

    public void setEstadoState(EstadoPedidoState estadoState) {
        this.estadoState = estadoState;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", numeroDePedido='" + numeroDePedido + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", estadoActual=" + estadoActual +
                ", costoTotal=" + costoTotal +
                ", estadoState=" + (estadoState != null ? estadoState.getNombreEstado() : "null") +
                '}';
    }
} 