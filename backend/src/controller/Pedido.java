
import java.io.*;
import java.util.*;
import java.util.List; // Added import
import java.util.ArrayList; // Added import

// Assuming Usuario class is in the same package or imported correctly if in another.
// import controller.Usuario; // If Usuario is in controller package

/**
 * 
 */
public class Pedido {

    /**
     * 
     */
    private int idPedido;

    /**
     * 
     */
    private int numeroDePedido;

    /**
     * 
     */
    private Date fechaDeCreacion;

    /**
     * 
     */
    private ICliente cliente;

    /**
     * 
     */
    private IVehiculo vehiculo;

    /**
     * 
     */
    private String estado;

    /**
     * 
     */
    private IFormaDePago formaDePago;

    /**
     * 
     */
    private InterfazImpuestoStrategy impuestoStrategy;

    /**
     * 
     */
    private double total; // This might be deprecated or used for sub-totals.

    /**
     * 
     */
    private ServicioDePago servicioDePago;

    /**
     * 
     */
    private ServicioDeNotificacion servicioDeNotificacion;

    // New fields
    private List<String> configuracionesAdicionales;
    private double costoTotal; // This will be the final calculated cost
    private String datosFacturacion;
    private Usuario vendedor; // Assuming Usuario class exists and is importable
    private String areaResponsableActual;
    private List<String> historialEstados;

    /**
     * Updated constructor
     */
    public Pedido(int idPedido, int numeroDePedido, Date fechaDeCreacion, ICliente cliente, 
                  IVehiculo vehiculo, String estadoInitial, IFormaDePago formaDePago, 
                  InterfazImpuestoStrategy impuestoStrategy, double initialTotal, 
                  ServicioDePago servicioDePago, ServicioDeNotificacion servicioDeNotificacion,
                  Usuario vendedor, String datosFacturacion, String areaResponsableActual) {
        this.idPedido = idPedido;
        this.numeroDePedido = numeroDePedido;
        this.fechaDeCreacion = fechaDeCreacion;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.estado = estadoInitial; // Renamed to avoid confusion with field name
        this.formaDePago = formaDePago;
        this.impuestoStrategy = impuestoStrategy;
        this.total = initialTotal; // This might be precioBase from vehicle initially
        this.servicioDePago = servicioDePago;
        this.servicioDeNotificacion = servicioDeNotificacion;
        
        this.vendedor = vendedor;
        this.datosFacturacion = datosFacturacion;
        this.areaResponsableActual = areaResponsableActual;

        this.configuracionesAdicionales = new ArrayList<>();
        this.historialEstados = new ArrayList<>();
        if (estadoInitial != null && !estadoInitial.isEmpty()) {
            this.historialEstados.add(estadoInitial);
        }
        this.costoTotal = 0; // Will be calculated by calcularCostoTotal()
    }


    /**
     * 
     */
    public void crearPedido() {
        // TODO: Logic to initialize a new order.
        // This might involve setting a default state, calculating initial total, etc.
        // For now, dependencies are injected, so it's about internal state.
        System.out.println("Pedido creado para: " + cliente.getNombre() + " Vehiculo: " + vehiculo.getMarca());
    }

    /**
     * 
     */
    public void actualizarEstadoPedido(String nuevoEstado) {
        // this.estado = nuevoEstado; // This will be handled by agregarHistorialEstado
        this.agregarHistorialEstado(nuevoEstado); // Call the new method
        // TODO: Add any other logic related to state change.
        if (this.servicioDeNotificacion != null) {
            this.servicioDeNotificacion.enviarNotificacion("Su pedido ha sido actualizado a: " + this.estado, this.cliente);
        }
    }

    /**
     * Adds a new state to the history and updates the current state.
     * @param nuevoEstado The new state to add.
     */
    public void agregarHistorialEstado(String nuevoEstado) {
        if (nuevoEstado != null && !nuevoEstado.isEmpty()) {
            this.historialEstados.add(nuevoEstado);
            this.estado = nuevoEstado; // Update current state
        }
    }

    /**
     * Calculates the total cost of the order including base price, taxes, and additional configurations.
     */
    public void calcularCostoTotal() {
        double basePrice = 0;
        if (this.vehiculo != null) {
            basePrice = this.vehiculo.getPrecioBase();
        }

        double calculatedTaxes = 0;
        if (this.impuestoStrategy != null) {
            // Assuming calcularImpuesto() now returns the tax amount
            // It might need parameters like basePrice or vehicle details,
            // The InterfazImpuestoStrategy.calcularImpuesto now takes precioBase.
            calculatedTaxes = this.impuestoStrategy.calcularImpuesto(basePrice); 
        }

        double costOfConfiguracionesAdicionales = 0;
        if (this.configuracionesAdicionales != null) {
            costOfConfiguracionesAdicionales = this.configuracionesAdicionales.size() * 500.0; // 500 per item
        }

        this.costoTotal = basePrice + calculatedTaxes + costOfConfiguracionesAdicionales;
        this.total = this.costoTotal; // Optional: Update 'total' to reflect the final cost as well.
                                      // Or 'total' could remain as initialTotal/subTotal.
                                      // For now, aligning it with costoTotal.
        System.out.println("Costo total calculado: " + this.costoTotal);
    }


    /**
     * This method might be deprecated or changed if calcularCostoTotal covers its responsibility.
     * For now, it is assumed that calcularImpuesto in the strategy returns the tax amount directly.
     */
    public void calcularImpuestos() {
        // This method's logic is now largely covered by calcularCostoTotal.
        // It could be kept for specific tax calculation logging or if the strategy
        // directly modifies 'this.total' or another field.
        // If impuestoStrategy.calcularImpuesto() returns a value, it's used in calcularCostoTotal.
        if (this.impuestoStrategy != null) {
            // double taxes = this.impuestoStrategy.calcularImpuesto(); // Example if it returns tax
            // System.out.println("Impuestos calculados (valor): " + taxes);
            // this.total += taxes; // If 'total' is meant to accumulate this way
            System.out.println("Método calcularImpuestos() llamado. La lógica principal está en calcularCostoTotal().");
        }
    }

    /**
     * Processes the payment using the payment service.
     */
    public boolean realizarPago() {
        if (this.formaDePago == null) {
            System.out.println("Forma de pago no especificada.");
            return false;
        }
        if (this.servicioDePago != null) {
            boolean pagoExitoso = this.servicioDePago.procesarPago(this.formaDePago, this.total);
            if (pagoExitoso) {
                this.actualizarEstadoPedido("Pagado"); // Example state update
            } else {
                this.actualizarEstadoPedido("ErrorDePago"); // Example state update
            }
            return pagoExitoso;
        }
        System.out.println("Servicio de pago no configurado.");
        return false;
    }

    // Getters
    public int getIdPedido() {
        return idPedido;
    }

    public int getNumeroDePedido() {
        return numeroDePedido;
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public ICliente getCliente() {
        return cliente;
    }

    public IVehiculo getVehiculo() {
        return vehiculo;
    }

    public String getEstado() {
        return estado;
    }

    public IFormaDePago getFormaDePago() {
        return formaDePago;
    }

    public InterfazImpuestoStrategy getImpuestoStrategy() {
        return impuestoStrategy;
    }

    public double getTotal() {
        return total;
    }

    public ServicioDePago getServicioDePago() {
        return servicioDePago;
    }

    public ServicioDeNotificacion getServicioDeNotificacion() {
        return servicioDeNotificacion;
    }

    // Setters
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setNumeroDePedido(int numeroDePedido) {
        this.numeroDePedido = numeroDePedido;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public void setCliente(ICliente cliente) {
        this.cliente = cliente;
    }

    public void setVehiculo(IVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFormaDePago(IFormaDePago formaDePago) {
        this.formaDePago = formaDePago;
    }

    public void setImpuestoStrategy(InterfazImpuestoStrategy impuestoStrategy) {
        this.impuestoStrategy = impuestoStrategy;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setServicioDePago(ServicioDePago servicioDePago) {
        this.servicioDePago = servicioDePago;
    }

    public void setServicioDeNotificacion(ServicioDeNotificacion servicioDeNotificacion) {
        this.servicioDeNotificacion = servicioDeNotificacion;
    }

    // Getters and Setters for new fields

    public List<String> getConfiguracionesAdicionales() {
        return configuracionesAdicionales;
    }

    public void setConfiguracionesAdicionales(List<String> configuracionesAdicionales) {
        this.configuracionesAdicionales = configuracionesAdicionales;
    }

    public double getCostoTotal() {
        // It's good practice to ensure costoTotal is up-to-date if it's derived
        // Or ensure calcularCostoTotal() is called before getting.
        // For now, just returning the field value.
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getDatosFacturacion() {
        return datosFacturacion;
    }

    public void setDatosFacturacion(String datosFacturacion) {
        this.datosFacturacion = datosFacturacion;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public String getAreaResponsableActual() {
        return areaResponsableActual;
    }

    public void setAreaResponsableActual(String areaResponsableActual) {
        this.areaResponsableActual = areaResponsableActual;
    }

    public List<String> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<String> historialEstados) {
        this.historialEstados = historialEstados;
    }
}