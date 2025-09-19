
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ManejoDeNotificaciones implements InterfazDeNotificacion {

    /**
     * Default constructor
     */
    private ManejoDeNotificaciones() {
        // TODO implement here
    }

    /**
     * 
     */
    private static ManejoDeNotificaciones instancia;

    /**
     * 
     */
    private InterfazDeNotificacion estado; // Current notification strategy/state

    /**
     * 
     */
    public static ManejoDeNotificaciones getInstancia() {
        if (instancia == null) {
            instancia = new ManejoDeNotificaciones();
        }
        return instancia;
    }

    /**
     * @param canal
     */
    public void setCanalPorDefecto(CanalDeNotificacion canal) {
        if (canal == CanalDeNotificacion.MAIL) { // Assuming CanalDeNotificacion is an enum
            this.estado = new MailEstado(); 
        } else if (canal == CanalDeNotificacion.SMS) {
            this.estado = new SMSEstado();
        } else {
            this.estado = null; // Or a default/null object pattern implementation
            System.err.println("Canal de notificación por defecto no reconocido.");
        }
    }

    /**
     * 
     */
    public void setEstado(InterfazDeNotificacion nuevoEstado) {
        this.estado = nuevoEstado;
    }
    
    /**
     * Retrieves the current notification state/strategy.
     * @return The current InterfazDeNotificacion instance.
     */
    public InterfazDeNotificacion getEstado() {
        return estado;
    }

    /**
     * 
     */
    @Override
    public void enviar() {
        if (this.estado != null) {
            this.estado.enviar();
        } else {
            System.err.println("Estado de notificación no configurado.");
        }
    }

}