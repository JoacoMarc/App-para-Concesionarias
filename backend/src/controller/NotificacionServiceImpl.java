package controller;

// Assuming ICliente and ServicioDeNotificacion are in the same package or imported.
// Assuming ManejoDeNotificaciones, MailEstado, SMSEstado are in the same package or imported.

public class NotificacionServiceImpl implements ServicioDeNotificacion {
    private ManejoDeNotificaciones manejador;
    // private Object templateEngine; // Example for future enhancement

    public NotificacionServiceImpl(ManejoDeNotificaciones manejador) {
        if (manejador == null) {
            throw new IllegalArgumentException("ManejoDeNotificaciones no puede ser null.");
        }
        this.manejador = manejador;
    }

    @Override
    public void enviarNotificacion(String mensaje, ICliente cliente) {
        if (cliente == null) {
            System.err.println("Cliente es null. No se puede enviar notificación.");
            return;
        }
        if (mensaje == null || mensaje.isEmpty()) {
            System.err.println("Mensaje es null o vacío. No se puede enviar notificación.");
            return;
        }

        System.out.println("ServicioDeNotificacion: Preparando para enviar notificación a " + cliente.getNombre() + " con mensaje: \"" + mensaje + "\"");

        // DESIGN ISSUE ACKNOWLEDGEMENT:
        // InterfazDeNotificacion.enviar() currently takes no parameters.
        // This means the 'mensaje' and 'cliente' details cannot be directly passed
        // to the specific strategy (MailEstado, SMSEstado) via manejador.enviar().
        // A proper solution would involve changing InterfazDeNotificacion.enviar()
        // to accept parameters, e.g., enviar(String messageContent, String recipientAddress).
        //
        // For this subtask, we are demonstrating the delegation to ManejoDeNotificaciones.
        // The actual message content won't be used by MailEstado/SMSEstado's current
        // parameterless enviar() method. This limitation should be addressed in a subsequent refactoring
        // of InterfazDeNotificacion and its implementations.

        // Example: Choose a default notification method if not already set, or if specific logic dictates.
        // This is just an example; typically the state of 'manejador' would be pre-configured.
        if (manejador.getEstado() == null) {
            System.out.println("ServicioDeNotificacion: No hay un estado de notificación preconfigurado en ManejoDeNotificaciones. Configurando a Mail por defecto.");
            manejador.setEstado(new MailEstado()); // Or use setCanalPorDefecto with an appropriate enum if available
        }
        
        // The following call will use the currently set state in ManejoDeNotificaciones.
        // However, 'mensaje' and 'cliente' are not passed further due to the current
        // InterfazDeNotificacion.enviar() signature.
        manejador.enviar(); 

        System.out.println("ServicioDeNotificacion: Intento de envío completado (la implementación actual de enviar() en el estado no usa el mensaje/cliente).");
    }
}
