
import java.io.*;
import java.util.*;

/**
 * 
 */
public class EstadoPedidoCoR {

    private String nombre;
    private EstadoPedidoCoR siguienteEstado;
    private SubjectObservadorEstado subjectObservadorEstado;

    /**
     * Default constructor
     */
    public EstadoPedidoCoR(String nombre) {
        this.nombre = nombre;
        this.subjectObservadorEstado = new SubjectObservadorEstado();
    }

    /**
     * 
     */
    public void manejarPedido(/* Pedido pedido */) { // Assuming a Pedido object would be passed
        // TODO: Implement CoR logic for handling the pedido
        // ...
        // If state changes or needs to be processed by next in chain:
        // actualizarEstado("Procesado por " + this.nombre);
        // if (siguienteEstado != null) {
        //     siguienteEstado.manejarPedido(pedido);
        // }
    }

    /**
     * 
     */
    public void actualizarEstado(String nuevoEstadoInfo) { // This method might be called internally or by a context
        // TODO: Implement actual state update logic if any, beyond just notification
        System.out.println("EstadoPedidoCoR " + this.nombre + " actualizando estado a: " + nuevoEstadoInfo);
        this.subjectObservadorEstado.setEstadoActual(nuevoEstadoInfo); // Notifies observers
    }

    /**
     * 
     */
    public void setSiguienteEstado(EstadoPedidoCoR siguiente) {
        this.siguienteEstado = siguiente;
    }

    public EstadoPedidoCoR getSiguienteEstado() {
        return this.siguienteEstado;
    }
    
    public String getNombre() {
        return this.nombre;
    }

    public void registrarObservador(Observador observador) {
        this.subjectObservadorEstado.adjuntar(observador);
    }

    public void quitarObservador(Observador observador) {
        this.subjectObservadorEstado.quitar(observador);
    }

}