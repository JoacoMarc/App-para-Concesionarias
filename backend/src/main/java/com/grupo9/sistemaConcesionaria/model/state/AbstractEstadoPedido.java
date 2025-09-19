package com.grupo9.sistemaConcesionaria.model.state;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.model.HistorialEstado;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

/**
 * Clase abstracta base para implementar el patrón State
 * Proporciona funcionalidad común para todos los estados
 */
public abstract class AbstractEstadoPedido implements EstadoPedidoState {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractEstadoPedido.class);
    
    protected String nombreEstado;
    protected String descripcionEstado;
    protected EstadoPedidoState siguienteEstado;

    public AbstractEstadoPedido(String nombreEstado, String descripcionEstado) {
        this.nombreEstado = nombreEstado;
        this.descripcionEstado = descripcionEstado;
    }

    @Override
    public final void procesar(Pedido pedido) throws PedidoNotFoundException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        logger.info("Iniciando procesamiento en estado: {} para pedido: {}", 
                   nombreEstado, pedido.getIdPedido());

        try {
            // Validaciones previas
            validarPrecondiciones(pedido);

            // Procesar estado específico
            procesarEstadoEspecifico(pedido);

            // Actualizar el estado del pedido
            actualizarEstadoPedido(pedido);

            // Validar si puede avanzar al siguiente estado
            if (puedeAvanzar(pedido) && siguienteEstado != null) {
                logger.info("Pedido {} puede avanzar al siguiente estado: {}", 
                           pedido.getIdPedido(), siguienteEstado.getNombreEstado());

            }

            logger.info("Procesamiento completado en estado: {} para pedido: {}", 
                       nombreEstado, pedido.getIdPedido());

        } catch (Exception e) {
            logger.error("Error procesando pedido {} en estado {}: {}", 
                        pedido.getIdPedido(), nombreEstado, e.getMessage());
            throw new PedidoNotFoundException("Error en estado " + nombreEstado + ": " + e.getMessage());
        }
    }

    protected abstract void procesarEstadoEspecifico(Pedido pedido) throws Exception;

    protected void validarPrecondiciones(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new IllegalArgumentException("El pedido debe tener un cliente asignado");
        }
        if (pedido.getVehiculo() == null) {
            throw new IllegalArgumentException("El pedido debe tener un vehículo asignado");
        }
    }


    protected void actualizarEstadoPedido(Pedido pedido) {
        EstadoPedido estadoEnum = mapearAEstadoEnum();
        
        // Usar el método cambiarEstado existente que actualiza estado e historial automáticamente
        pedido.cambiarEstado(estadoEnum, nombreEstado);
        
        logger.debug("Estado del pedido {} actualizado a: {}", 
                    pedido.getIdPedido(), estadoEnum.getDescripcion());
    }


    protected abstract EstadoPedido mapearAEstadoEnum();


    public void setSiguienteEstado(EstadoPedidoState siguienteEstado) {
        this.siguienteEstado = siguienteEstado;
    }

    @Override
    public EstadoPedidoState getSiguienteEstado() {
        return siguienteEstado;
    }

    @Override
    public String getNombreEstado() {
        return nombreEstado;
    }

    @Override
    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    @Override
    public boolean esFinal() {
        return siguienteEstado == null;
    }

    @Override
    public boolean puedeAvanzar(Pedido pedido) {
        return true;
    }

    protected void agregarConfiguracion(Pedido pedido, String configuracion) {
        pedido.agregarConfiguracion(configuracion);
        logger.debug("Configuración agregada al pedido {}: {}", 
                    pedido.getIdPedido(), configuracion);
    }

    protected void simularProcesamiento(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Procesamiento interrumpido en estado: {}", nombreEstado);
        }
    }
} 