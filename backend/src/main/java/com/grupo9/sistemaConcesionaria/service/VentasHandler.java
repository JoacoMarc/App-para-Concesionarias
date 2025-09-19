package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VentasHandler - Handler concreto para el estado VENTAS
 * Procesa la validación inicial y configuración del pedido
 */
@Component
public class VentasHandler extends EstadoPedidoHandler {

    private static final Logger logger = LoggerFactory.getLogger(VentasHandler.class);

    public VentasHandler(NotificacionService notificacionService) {
        super("Ventas", notificacionService);
    }

    @Override
    protected void procesarEstado(Pedido pedido) throws PedidoNotFoundException {
        logger.info("Procesando pedido {} en estado VENTAS", pedido.getIdPedido());

        try {
            // Validaciones específicas del área de ventas
            validarDatosCliente(pedido);
            validarVehiculoDisponible(pedido);
            configurarPedido(pedido);

            // Actualizar estado
            actualizarEstadoPedido(pedido, EstadoPedido.VENTAS, "Pedido validado en Ventas");

            logger.info("Pedido {} procesado exitosamente en VENTAS", pedido.getIdPedido());

        } catch (Exception e) {
            logger.error("Error procesando pedido {} en VENTAS: {}", pedido.getIdPedido(), e.getMessage());
            throw new PedidoNotFoundException("Error en procesamiento de Ventas: " + e.getMessage());
        }
    }

    /**
     * Valida los datos del cliente
     */
    private void validarDatosCliente(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new IllegalArgumentException("Cliente no puede ser nulo");
        }
        
        if (pedido.getCliente().getDocumento() == null || pedido.getCliente().getDocumento().isEmpty()) {
            throw new IllegalArgumentException("Cliente debe tener documento válido");
        }

        logger.debug("Cliente validado: {}", pedido.getCliente().getNombre());
    }

    /**
     * Valida que el vehículo esté disponible
     */
    private void validarVehiculoDisponible(Pedido pedido) {
        if (pedido.getVehiculo() == null) {
            throw new IllegalArgumentException("Vehículo no puede ser nulo");
        }

        if (!pedido.getVehiculo().isDisponibleVenta()) {
            throw new IllegalArgumentException("Vehículo no está disponible para venta");
        }

        logger.debug("Vehículo validado: {} {}", 
                    pedido.getVehiculo().getMarca(), pedido.getVehiculo().getModelo());
    }

    /**
     * Configura datos adicionales del pedido
     */
    private void configurarPedido(Pedido pedido) {
        // Agregar configuraciones por defecto si no las tiene
        if (pedido.getConfiguracionesAdicionales().isEmpty()) {
            pedido.agregarConfiguracion("Garantía básica de fábrica");
            pedido.agregarConfiguracion("Manual de usuario");
            pedido.agregarConfiguracion("Kit de herramientas básico");
        }

        // Calcular costo total preliminar
        pedido.calcularCostoTotal();

        logger.debug("Pedido configurado con {} elementos adicionales", 
                    pedido.getConfiguracionesAdicionales().size());
    }
} 