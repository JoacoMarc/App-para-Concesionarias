package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;

/**
 * Chain of Responsibility Pattern - Handler para procesamiento de estados de pedido
 * Cada handler representa un estado específico en el flujo de procesamiento
 */
public abstract class EstadoPedidoHandler {

    protected EstadoPedidoHandler siguienteHandler;
    protected NotificacionService notificacionService;
    protected String nombreEstado;

    /**
     * Constructor para el handler
     * @param nombreEstado Nombre del estado que maneja este handler
     * @param notificacionService Servicio de notificaciones
     */
    public EstadoPedidoHandler(String nombreEstado, NotificacionService notificacionService) {
        this.nombreEstado = nombreEstado;
        this.notificacionService = notificacionService;
    }

    /**
     * Establece el siguiente handler en la cadena
     * @param siguiente El siguiente handler
     * @return El siguiente handler (para encadenamiento fluido)
     */
    public EstadoPedidoHandler setSiguiente(EstadoPedidoHandler siguiente) {
        this.siguienteHandler = siguiente;
        return siguiente;
    }

    /**
     * Procesa el pedido en el estado actual
     * @param pedido El pedido a procesar
     * @throws PedidoNotFoundException si hay errores en el procesamiento
     */
    public final void procesar(Pedido pedido) throws PedidoNotFoundException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        // Procesar en el estado actual
        procesarEstado(pedido);

        // Pasar al siguiente handler si existe
        if (siguienteHandler != null) {
            siguienteHandler.procesar(pedido);
        }
    }

    /**
     * Método abstracto que implementa cada estado específico
     * @param pedido El pedido a procesar
     * @throws PedidoNotFoundException si hay errores específicos del estado
     */
    protected abstract void procesarEstado(Pedido pedido) throws PedidoNotFoundException;

    /**
     * Actualiza el estado del pedido y notifica
     * @param pedido El pedido a actualizar
     * @param nuevoEstado El nuevo estado
     * @param descripcion Descripción del cambio
     */
    protected void actualizarEstadoPedido(Pedido pedido, EstadoPedido nuevoEstado, String descripcion) {
        // Actualizar estado usando el método existente del modelo
        pedido.cambiarEstado(nuevoEstado, nombreEstado);

        // Notificar cambio
        notificacionService.notificarCambioPedido(
            pedido.getIdPedido(), 
            nuevoEstado.name(), 
            nombreEstado
        );
    }

    /**
     * Obtiene el nombre del estado
     * @return Nombre del estado
     */
    public String getNombreEstado() {
        return nombreEstado;
    }
} 