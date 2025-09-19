import java.io.*;
import java.util.*;

/**
 * 
 */
// Ensure necessary imports are present
// import controller.EstadoPedidoCoR;
// import controller.Pedido;
// import controller.PedidoNotFoundException; // For iniciarProcesamientoPedido
// import controller.Observador; // For conceptual observer registration
// Potentially: import controller.MailObservador; // Hypothetical
// Potentially: import controller.SMSObservador; // Hypothetical

import java.util.Date; // For realizarPedido
import java.util.List; // For realizarPedido
// Assuming other classes like Pedido, Cliente, Vehiculo, FormaDePago, InterfazImpuestoStrategy, Usuario
// DuplicatePedidoException, VehiculoNotFoundException, ClienteNotFoundException, PedidoNotFoundException
// are in the 'controller' package or appropriately imported.

public class SistemaFacade {

    // Concessionaria Details
    private static final String NOMBRE_CONCESIONARIA = "Concesionaria Super Autos";
    private static final String CUIT_CONCESIONARIA = "30-12345678-9";

    private InterfazGestorDePedidos gestorDePedidos;
    private InterfazGestorDeUsuario gestorDeUsuario;
    private InterfazGestorVehiculos gestorVehiculos;
    private InterfazGestorImpuestos gestorImpuestos;
    private GestorDeStock gestorDeStock; // Assuming no interface for this one yet
    private InterfazDeNotificacion notificaciones; // General notification interface

    // Fields for Chain of Responsibility
    private EstadoPedidoCoR cadenaDeProcesamientoPedidos;
    private EstadoPedidoCoR estadoVentas;
    private EstadoPedidoCoR estadoCobranzas;
    private EstadoPedidoCoR estadoImpuestos;
    private EstadoPedidoCoR estadoEmbarque;
    private EstadoPedidoCoR estadoLogistica;
    private EstadoPedidoCoR estadoEntrega;


    /**
     * Default constructor
     */
    public SistemaFacade(
        InterfazGestorDePedidos gestorDePedidos,
        InterfazGestorDeUsuario gestorDeUsuario,
        InterfazGestorVehiculos gestorVehiculos,
        InterfazGestorImpuestos gestorImpuestos,
        GestorDeStock gestorDeStock,
        InterfazDeNotificacion notificaciones // This is the general notification, not for CoR states specifically
    ) {
        this.gestorDePedidos = gestorDePedidos;
        this.gestorDeUsuario = gestorDeUsuario;
        this.gestorVehiculos = gestorVehiculos;
        this.gestorImpuestos = gestorImpuestos;
        this.gestorDeStock = gestorDeStock;
        this.notificaciones = notificaciones; // General notification system

        // Initialize the Chain of Responsibility for pedido processing
        this.estadoVentas = new EstadoPedidoCoR("Ventas");
        this.estadoCobranzas = new EstadoPedidoCoR("Cobranzas");
        this.estadoImpuestos = new EstadoPedidoCoR("Impuestos");
        this.estadoEmbarque = new EstadoPedidoCoR("Embarque");
        this.estadoLogistica = new EstadoPedidoCoR("Logistica");
        this.estadoEntrega = new EstadoPedidoCoR("Entrega");

        // Link the states
        this.estadoVentas.setSiguienteEstado(this.estadoCobranzas);
        this.estadoCobranzas.setSiguienteEstado(this.estadoImpuestos);
        this.estadoImpuestos.setSiguienteEstado(this.estadoEmbarque);
        this.estadoEmbarque.setSiguienteEstado(this.estadoLogistica);
        this.estadoLogistica.setSiguienteEstado(this.estadoEntrega);
        this.estadoEntrega.setSiguienteEstado(null); // End of the chain

        // Set the head of the chain
        this.cadenaDeProcesamientoPedidos = this.estadoVentas;

        // Conceptual: Register observers for each state
        // These would be specific implementations of the Observador interface
        // For example, if MailEstado and SMSEstado implemented Observador:
        // Observador mailObservador = new MailObservador(); // Hypothetical
        // Observador smsObservador = new SMSObservador(); // Hypothetical

        // this.estadoVentas.registrarObservador(mailObservador);
        // this.estadoVentas.registrarObservador(smsObservador);
        // this.estadoCobranzas.registrarObservador(mailObservador);
        // ... and so on for other states and observers.
        // Actual classes like MailEstado.java and SMSEstado.java would need to implement Observador.
        // For now, these are comments indicating where such registration would occur.
    }

    // Getter methods for the gestores could be added if needed by external components
    // but not strictly necessary for Facade's internal operation using them.

    /**
     * Initiates the processing of a given pedido through the chain of responsibility.
     * @param pedido The pedido to process.
     * @throws PedidoNotFoundException if the pedido cannot be updated by the gestor during processing.
     * @throws IllegalArgumentException if the pedido is null.
     */
    public void iniciarProcesamientoPedido(Pedido pedido) throws PedidoNotFoundException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        if (this.cadenaDeProcesamientoPedidos != null) {
            // Pass the gestorDePedidos instance which EstadoPedidoCoR needs to persist changes
            this.cadenaDeProcesamientoPedidos.manejarPedido(pedido, this.gestorDePedidos);
        } else {
            // This case should ideally not happen if constructor initializes the chain correctly
            System.err.println("Error crítico: La cadena de procesamiento de pedidos no ha sido inicializada.");
            // Optionally, throw a new RuntimeException or a specific configuration exception
        }
    }

    /**
     * 
     */
    public void realizarPedido() {
        // TODO implement here
        // This placeholder will be replaced by the new realizarPedido method below
    }

    /**
     * Creates and processes a new vehicle order.
     *
     * @param idPedido Unique ID for the pedido.
     * @param fechaDeCreacion Date of order creation.
     * @param cliente The client placing the order.
     * @param vehiculo The vehicle being ordered.
     * @param formaDePago The payment method chosen.
     * @param impuestoStrategy The tax strategy to apply.
     * @param vendedor The salesperson handling the order.
     * @param datosFacturacion Billing information.
     * @param configuracionesAdicionales List of additional configurations for the vehicle.
     * @return The created Pedido object.
     * @throws DuplicatePedidoException if a pedido with the same ID already exists.
     * @throws VehiculoNotFoundException if the specified vehicle is not found or not available.
     * @throws ClienteNotFoundException if the specified client is not found.
     * @throws PedidoNotFoundException if there's an issue updating the vehicle status after order registration (should be rare).
     */
    public Pedido realizarPedido(
            int idPedido,
            Date fechaDeCreacion,
            Cliente cliente,
            Vehiculo vehiculo,
            IFormaDePago formaDePago, // Changed from FormaDePago to IFormaDePago to match Pedido constructor
            InterfazImpuestoStrategy impuestoStrategy,
            Usuario vendedor,
            String datosFacturacion,
            List<String> configuracionesAdicionales)
            throws DuplicatePedidoException, VehiculoNotFoundException, ClienteNotFoundException, PedidoNotFoundException {

        // 1. Verify Client
        if (cliente == null || cliente.getDocumento() == null || cliente.getDocumento().isEmpty()) {
            throw new IllegalArgumentException("Cliente y su documento no pueden ser nulos o vacíos.");
        }
        // The getCliente method itself throws ClienteNotFoundException if not found.
        // We might not need to pre-fetch it unless other client details are immediately needed here.
        // For now, let's assume the client object passed is sufficient or will be validated by gestor.
        // However, the requirement says "Check if the cliente exists".
        this.gestorDeUsuario.getCliente(cliente.getDocumento()); // This will throw ClienteNotFoundException if not found

        // 2. Verify Vehicle
        if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
            throw new IllegalArgumentException("Vehículo y su número de chasis no pueden ser nulos o vacíos.");
        }
        Vehiculo vehiculoEnCatalogo = this.gestorVehiculos.getVehiculo(vehiculo.getNumeroChasis()); // Throws VehiculoNotFoundException
        if (!vehiculoEnCatalogo.isDisponibleVenta()) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " no está disponible para la venta.");
        }

        // 3. Create Pedido Object
        int numeroDePedido = idPedido; // Using idPedido as numeroDePedido for now
        String estadoInicial = "Creado"; // Initial state
        double initialTotal = vehiculoEnCatalogo.getPrecioBase();
        ServicioDePago servicioDePago = null; // To be wired up later
        ServicioDeNotificacion servicioDeNotificacion = null; // To be wired up later
        String areaResponsableInicial = "Inicio";

        Pedido nuevoPedido = new Pedido(
                idPedido,
                numeroDePedido,
                fechaDeCreacion,
                cliente,
                vehiculoEnCatalogo, // Use the verified vehicle from catalog
                estadoInicial,
                formaDePago,
                impuestoStrategy,
                initialTotal,
                servicioDePago,
                servicioDeNotificacion,
                vendedor,
                datosFacturacion,
                areaResponsableInicial);

        if (configuracionesAdicionales != null) {
            nuevoPedido.setConfiguracionesAdicionales(configuracionesAdicionales);
        }

        // 4. Calculate Total Cost
        nuevoPedido.calcularCostoTotal(); // This method should use the vehicle's base price, taxes, and additional configs

        // 5. Register Pedido
        this.gestorDePedidos.registrarPedido(nuevoPedido); // Throws DuplicatePedidoException

        // 6. Update Vehicle Status
        vehiculoEnCatalogo.setDisponibleVenta(false);
        this.gestorVehiculos.actualizarDatosVehiculo(vehiculoEnCatalogo); // Throws VehiculoNotFoundException (but shouldn't if we just got it)

        // 7. Initiate Processing
        this.iniciarProcesamientoPedido(nuevoPedido); // Throws PedidoNotFoundException (from CoR if update fails)

        // 8. Return the created Pedido object
        return nuevoPedido;
    }

    // Static getters for Concessionaria Details
    public static String getNombreConcesionaria() {
        return NOMBRE_CONCESIONARIA;
    }

    public static String getCuitConcesionaria() {
        return CUIT_CONCESIONARIA;
    }

    /**
     * Processes a specific pedido, potentially re-initiating its journey through the CoR.
     * If the pedido has a current area of responsibility, processing attempts to resume from that state.
     * Otherwise, or if explicitly needed, it can re-start from the beginning of the chain.
     *
     * @param idPedido The ID of the pedido to process.
     * @throws PedidoNotFoundException if the pedido with the given ID is not found.
     * @throws IllegalStateException if a handler for the pedido's current area is not found in the chain.
     */
    public void procesarPedidoEspecifico(int idPedido) throws PedidoNotFoundException, IllegalStateException {
        if (this.gestorDePedidos == null) {
            throw new IllegalStateException("GestorDePedidos no inicializado.");
        }
        Pedido pedido = this.gestorDePedidos.getPedido(idPedido); // Throws PedidoNotFoundException if not found

        String areaActual = pedido.getAreaResponsableActual();

        if (areaActual == null || areaActual.isEmpty() || areaActual.equalsIgnoreCase("Inicio") || areaActual.equalsIgnoreCase("Creado")) {
            // If no specific area, or is in an initial conceptual state, start from the beginning of the chain
            System.out.println("Pedido " + idPedido + " no tiene área responsable actual o está en estado inicial. Iniciando procesamiento desde el principio.");
            this.iniciarProcesamientoPedido(pedido);
        } else {
            EstadoPedidoCoR estadoActualHandler = this.cadenaDeProcesamientoPedidos;
            boolean foundHandler = false;
            while (estadoActualHandler != null) {
                if (estadoActualHandler.getNombre().equalsIgnoreCase(areaActual)) {
                    System.out.println("Reanudando procesamiento para pedido " + idPedido + " desde el estado: " + areaActual);
                    estadoActualHandler.manejarPedido(pedido, this.gestorDePedidos);
                    foundHandler = true;
                    break;
                }
                estadoActualHandler = estadoActualHandler.getSiguienteEstado();
            }

            if (!foundHandler) {
                // This case might mean the areaResponsableActual is not a valid state name in the chain,
                // or the chain is not fully set up, or the pedido is in an unsupported state.
                // Forcing it to the start of the chain might be an option, or throwing an error.
                System.err.println("Advertencia: No se encontró un manejador de estado para el área: " + areaActual + " del pedido " + idPedido + ". Reiniciando desde el principio.");
                this.iniciarProcesamientoPedido(pedido); 
                // Alternatively, throw new IllegalStateException("No se encontró un manejador de estado para el área: " + areaActual);
            }
        }
    }

    /**
     * 
     */
    /**
     * Generates a sales report based on orders, with optional date and status filters.
     * This method delegates the report generation to GestorDePedidos.
     *
     * @param filtroFecha Date filter string (e.g., "YYYY-MM-DD"). Can be null or empty.
     * @param filtroEstado State filter string. Can be null or empty.
     * @return A string containing the sales report.
     */
    public String generarReporteDeVentas(String filtroFecha, String filtroEstado) {
        if (this.gestorDePedidos == null) {
            return "Error: GestorDePedidos no inicializado.";
        }
        return this.gestorDePedidos.generarInforme(filtroFecha, filtroEstado);
    }

    /**
     * Generates a report summarizing total amounts and counts by payment method.
     * Delegates to GestorDePedidos.
     * @return A string containing the formatted report.
     */
    public String generarReporteTotalesPorFormaDePago() {
        if (this.gestorDePedidos == null) {
            return "Error: GestorDePedidos no inicializado.";
        }
        return this.gestorDePedidos.generarInformeTotalesPorFormaDePago();
    }

    // --- Vehicle Operations ---

    /**
     * Registers a new vehicle in the system.
     * Delegates to GestorVehiculos.
     * @param vehiculo The vehicle to register.
     * @throws DuplicateVehiculoException if the vehicle already exists.
     */
    public void registrarVehiculoEnSistema(Vehiculo vehiculo) throws DuplicateVehiculoException {
        if (this.gestorVehiculos == null) throw new IllegalStateException("GestorDeVehiculos no inicializado.");
        this.gestorVehiculos.registrarVehiculo(vehiculo);
    }

    /**
     * Updates the data of an existing vehicle.
     * Delegates to GestorVehiculos.
     * @param vehiculo The vehicle with updated data.
     * @throws VehiculoNotFoundException if the vehicle is not found.
     */
    public void actualizarDatosVehiculo(Vehiculo vehiculo) throws VehiculoNotFoundException {
        if (this.gestorVehiculos == null) throw new IllegalStateException("GestorDeVehiculos no inicializado.");
        this.gestorVehiculos.actualizarDatosVehiculo(vehiculo);
    }

    /**
     * Removes a vehicle from the system by its chassis number.
     * Delegates to GestorVehiculos.
     * @param numeroChasis The chassis number of the vehicle to remove.
     * @throws VehiculoNotFoundException if the vehicle is not found.
     */
    public void eliminarVehiculoDelSistema(String numeroChasis) throws VehiculoNotFoundException {
        if (this.gestorVehiculos == null) throw new IllegalStateException("GestorDeVehiculos no inicializado.");
        this.gestorVehiculos.eliminarVehiculo(numeroChasis);
    }

    /**
     * Retrieves a vehicle by its chassis number.
     * Delegates to GestorVehiculos.
     * @param numeroChasis The chassis number of the vehicle.
     * @return The found vehicle.
     * @throws VehiculoNotFoundException if the vehicle is not found.
     */
    public Vehiculo getVehiculo(String numeroChasis) throws VehiculoNotFoundException {
        if (this.gestorVehiculos == null) throw new IllegalStateException("GestorDeVehiculos no inicializado.");
        return this.gestorVehiculos.getVehiculo(numeroChasis);
    }

    /**
     * Retrieves all vehicles in the system.
     * Delegates to GestorVehiculos.
     * @return A collection of all vehicles.
     */
    public Collection<Vehiculo> getTodosLosVehiculos() {
        if (this.gestorVehiculos == null) throw new IllegalStateException("GestorDeVehiculos no inicializado.");
        return this.gestorVehiculos.getTodosLosVehiculos();
    }

    // --- Client Operations ---

    /**
     * Registers a new client.
     * Delegates to GestorDeUsuarios.
     * @param cliente The client to register.
     * @throws DuplicateClienteException if the client already exists.
     */
    public void registrarCliente(Cliente cliente) throws DuplicateClienteException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        this.gestorDeUsuario.registrarCliente(cliente);
    }

    /**
     * Updates the data of an existing client.
     * Delegates to GestorDeUsuarios.
     * @param cliente The client with updated data.
     * @throws ClienteNotFoundException if the client is not found.
     */
    public void actualizarDatosCliente(Cliente cliente) throws ClienteNotFoundException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        this.gestorDeUsuario.actualizarCliente(cliente);
    }

    /**
     * Removes a client by their document number.
     * Delegates to GestorDeUsuarios.
     * @param documento The document number of the client to remove.
     * @throws ClienteNotFoundException if the client is not found.
     */
    public void eliminarCliente(String documento) throws ClienteNotFoundException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        this.gestorDeUsuario.eliminarCliente(documento);
    }

    /**
     * Retrieves a client by their document number.
     * Delegates to GestorDeUsuarios.
     * @param documento The document number of the client.
     * @return The found client.
     * @throws ClienteNotFoundException if the client is not found.
     */
    public Cliente getCliente(String documento) throws ClienteNotFoundException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        return this.gestorDeUsuario.getCliente(documento);
    }

    /**
     * Retrieves all clients.
     * Delegates to GestorDeUsuarios.
     * @return A collection of all clients.
     */
    public Collection<Cliente> getTodosLosClientes() {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        return this.gestorDeUsuario.getTodosLosClientes();
    }

    // --- System User Operations ---

    /**
     * Registers a new system user.
     * Delegates to GestorDeUsuarios.
     * @param usuario The system user to register.
     * @throws DuplicateUsuarioException if the user already exists.
     */
    public void registrarUsuarioSistema(Usuario usuario) throws DuplicateUsuarioException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        this.gestorDeUsuario.registrarUsuarioSistema(usuario);
    }

    /**
     * Updates the data of an existing system user.
     * Delegates to GestorDeUsuarios.
     * @param usuario The system user with updated data.
     * @throws UsuarioNotFoundException if the user is not found.
     */
    public void actualizarDatosUsuarioSistema(Usuario usuario) throws UsuarioNotFoundException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        this.gestorDeUsuario.actualizarUsuarioSistema(usuario);
    }

    /**
     * Removes a system user by their email.
     * Delegates to GestorDeUsuarios.
     * @param mail The email of the system user to remove.
     * @throws UsuarioNotFoundException if the user is not found.
     */
    public void eliminarUsuarioSistema(String mail) throws UsuarioNotFoundException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        this.gestorDeUsuario.eliminarUsuarioSistema(mail);
    }

    /**
     * Retrieves a system user by their email.
     * Delegates to GestorDeUsuarios.
     * @param mail The email of the system user.
     * @return The found system user.
     * @throws UsuarioNotFoundException if the user is not found.
     */
    public Usuario getUsuarioSistema(String mail) throws UsuarioNotFoundException {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        return this.gestorDeUsuario.getUsuarioSistema(mail);
    }

    /**
     * Retrieves all system users.
     * Delegates to GestorDeUsuarios.
     * @return A collection of all system users.
     */
    public Collection<Usuario> getTodosLosUsuariosSistema() {
        if (this.gestorDeUsuario == null) throw new IllegalStateException("GestorDeUsuarios no inicializado.");
        return this.gestorDeUsuario.getTodosLosUsuariosSistema();
    }
}
