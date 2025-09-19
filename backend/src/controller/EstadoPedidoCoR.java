package controller;

import java.io.*; // Often included, though not strictly necessary for this logic
import java.util.*; // Often included, though not strictly necessary for this logic

// Explicit imports for clarity, even if some are in the same package.
// import controller.Pedido; // Assuming Pedido is in controller package
// import controller.InterfazGestorDePedidos; // Assuming InterfazGestorDePedidos is in controller package
// import controller.Observador; // Assuming Observador is in controller package
// import controller.SubjectObservadorEstado; // Assuming SubjectObservadorEstado is in controller package
// import controller.PedidoNotFoundException; // Assuming PedidoNotFoundException is in controller package

/**
 * Represents a state in the Chain of Responsibility for processing Pedido objects.
 * Each state can handle a pedido, update its status, notify observers,
 * and pass it to the next state in the chain.
 */
public class EstadoPedidoCoR {

    private String nombre; // Name of the current state (e.g., "Ventas", "Logistica")
    private EstadoPedidoCoR siguienteEstado; // Next state in the chain
    private SubjectObservadorEstado subjectObservadorEstado; // Subject for observer pattern

    /**
     * Constructor for EstadoPedidoCoR.
     * @param nombre The name of this state.
     */
    public EstadoPedidoCoR(String nombre) {
        this.nombre = nombre;
        this.subjectObservadorEstado = new SubjectObservadorEstado(); // Each state has its own subject
    }

    /**
     * Handles the pedido processing for the current state.
     * Updates the pedido's area responsable and history, notifies observers,
     * persists changes, and then passes the pedido to the next state if one exists.
     * 
     * @param pedido The Pedido object to be processed.
     * @param gestorPedidos The GestorDePedidos instance to persist changes to the pedido.
     * @throws PedidoNotFoundException if the gestorPedidos fails to update the pedido because it's not found.
     */
    public void manejarPedido(Pedido pedido, InterfazGestorDePedidos gestorPedidos) throws PedidoNotFoundException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo al manejar el estado " + this.nombre);
        }
        if (gestorPedidos == null) {
            throw new IllegalArgumentException("El GestorDePedidos no puede ser nulo al manejar el estado " + this.nombre);
        }

        System.out.println("Procesando pedido " + pedido.getIdPedido() + " en estado: " + this.nombre);

        // Update pedido's state information
        pedido.setAreaResponsableActual(this.nombre);
        pedido.agregarHistorialEstado("Entró al estado: " + this.nombre + " (" + new Date().toString() + ")");
        // Note: Pedido's agregarHistorialEstado should also update pedido.setEstado(this.nombre) internally.
        // If not, we might need: pedido.setEstado(this.nombre);

        // Placeholder for state-specific actions
        // TODO: Implement specific actions for state [this.nombre] here.
        // For example, if this.nombre.equals("Finanzas"), specific financial validation might occur.
        // if (this.nombre.equals("Logistica")) { /* perform logistics specific tasks */ }

        // Notify observers about the update
        String mensajeNotificacion = "Pedido " + pedido.getIdPedido() + " actualizado. Nuevo estado: " + this.nombre + 
                                     ", Área Responsable: " + pedido.getAreaResponsableActual();
        this.subjectObservadorEstado.setEstadoActual(mensajeNotificacion);
        
        // Persist changes using the GestorDePedidos
        try {
            gestorPedidos.actualizarPedido(pedido);
            System.out.println("Pedido " + pedido.getIdPedido() + " persistido tras el estado: " + this.nombre);
        } catch (PedidoNotFoundException e) {
            System.err.println("Error al persistir pedido " + pedido.getIdPedido() + " en estado " + this.nombre + ": " + e.getMessage());
            throw e; // Re-throw if this state cannot handle it or if it's critical
        }
        // Catch other potential exceptions from gestorPedidos.actualizarPedido if necessary

        // Pass to the next state in the chain
        if (siguienteEstado != null) {
            System.out.println("Pasando pedido " + pedido.getIdPedido() + " del estado " + this.nombre + " al estado " + siguienteEstado.getNombre());
            siguienteEstado.manejarPedido(pedido, gestorPedidos);
        } else {
            System.out.println("Pedido " + pedido.getIdPedido() + " ha completado el estado final: " + this.nombre);
            // Final notification if needed
            this.subjectObservadorEstado.setEstadoActual("Pedido " + pedido.getIdPedido() + " ha completado todos los estados. Estado final: " + this.nombre);
        }
    }

    /**
     * Allows external components to trigger a state update notification for this state's observers.
     * This might be used if a change relevant to this state happens outside the CoR flow.
     * @param nuevoEstadoInfo A string message describing the state update.
     */
    public void actualizarEstado(String nuevoEstadoInfo) {
        System.out.println("EstadoPedidoCoR (" + this.nombre + "): Actualización directa de estado/info: " + nuevoEstadoInfo);
        this.subjectObservadorEstado.setEstadoActual(nuevoEstadoInfo); // Notifies observers
    }

    /**
     * Sets the next state in the Chain of Responsibility.
     * @param siguiente The next EstadoPedidoCoR instance.
     */
    public void setSiguienteEstado(EstadoPedidoCoR siguiente) {
        this.siguienteEstado = siguiente;
    }

    /**
     * Gets the next state in the chain.
     * @return The next EstadoPedidoCoR instance, or null if this is the last state.
     */
    public EstadoPedidoCoR getSiguienteEstado() {
        return this.siguienteEstado;
    }
    
    /**
     * Gets the name of this state.
     * @return The name of the state.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Registers an observer to be notified of state changes.
     * @param observador The Observador instance to register.
     */
    public void registrarObservador(Observador observador) {
        if (observador != null) {
            this.subjectObservadorEstado.adjuntar(observador);
        }
    }

    /**
     * Removes an observer from the notification list.
     * @param observador The Observador instance to remove.
     */
    public void quitarObservador(Observador observador) {
        if (observador != null) {
            this.subjectObservadorEstado.quitar(observador);
        }
    }
}
