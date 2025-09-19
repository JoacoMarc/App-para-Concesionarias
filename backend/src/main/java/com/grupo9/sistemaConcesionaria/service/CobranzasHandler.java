package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CobranzasHandler - Handler concreto para el estado COBRANZAS
 * Procesa el pago y validación financiera del pedido
 */
@Component
public class CobranzasHandler extends EstadoPedidoHandler {

    private static final Logger logger = LoggerFactory.getLogger(CobranzasHandler.class);

    public CobranzasHandler(NotificacionService notificacionService) {
        super("Cobranzas", notificacionService);
    }

    @Override
    protected void procesarEstado(Pedido pedido) throws PedidoNotFoundException {
        logger.info("Procesando pedido {} en estado COBRANZAS", pedido.getIdPedido());

        try {
            // Procesos específicos del área de cobranzas
            validarFormaPago(pedido);
            procesarPago(pedido);
            generarFacturacion(pedido);

            // Actualizar estado
            actualizarEstadoPedido(pedido, EstadoPedido.COBRANZAS, "Pago procesado en Cobranzas");

            logger.info("Pedido {} procesado exitosamente en COBRANZAS", pedido.getIdPedido());

        } catch (Exception e) {
            logger.error("Error procesando pedido {} en COBRANZAS: {}", pedido.getIdPedido(), e.getMessage());
            throw new PedidoNotFoundException("Error en procesamiento de Cobranzas: " + e.getMessage());
        }
    }

    /**
     * Valida la forma de pago seleccionada
     */
    private void validarFormaPago(Pedido pedido) {
        if (pedido.getFormaDePago() == null) {
            throw new IllegalArgumentException("Forma de pago no puede ser nula");
        }

        logger.debug("Forma de pago validada: {}", pedido.getFormaDePago());
    }

    /**
     * Procesa el pago según la forma seleccionada
     */
    private void procesarPago(Pedido pedido) {
        double montoTotal = pedido.getCostoTotal();
        
        logger.info("Procesando pago de ${:.2f} mediante {}", 
                   montoTotal, pedido.getFormaDePago());

        // Simular procesamiento según forma de pago
        switch (pedido.getFormaDePago()) {
            case CONTADO:
                procesarPagoContado(montoTotal);
                break;
            case TRANSFERENCIA:
                procesarPagoTransferencia(montoTotal);
                break;
            case TARJETA:
                procesarPagoTarjeta(montoTotal);
                break;
            default:
                throw new IllegalArgumentException("Forma de pago no válida: " + pedido.getFormaDePago());
        }

        logger.debug("Pago procesado exitosamente");
    }

    /**
     * Procesa pago en efectivo
     */
    private void procesarPagoContado(double monto) {
        logger.info("Procesando pago en efectivo por ${:.2f}", monto);
        // Lógica específica para pago en efectivo
        simulatePaymentDelay(500);
    }

    /**
     * Procesa pago por transferencia
     */
    private void procesarPagoTransferencia(double monto) {
        logger.info("Procesando transferencia bancaria por ${:.2f}", monto);
        // Lógica específica para transferencia
        simulatePaymentDelay(1000);
    }

    /**
     * Procesa pago por tarjeta de crédito
     */
    private void procesarPagoTarjeta(double monto) {
        logger.info("Procesando pago con tarjeta de crédito por ${:.2f}", monto);
        // Lógica específica para tarjeta
        simulatePaymentDelay(800);
    }

    /**
     * Genera la facturación del pedido
     */
    private void generarFacturacion(Pedido pedido) {
        // Actualizar datos de facturación
        String datosFacturacion = String.format(
            "FACTURA - Cliente: %s %s - Documento: %s - Total: $%.2f - Fecha: %s",
            pedido.getCliente().getNombre(),
            pedido.getCliente().getApellido(),
            pedido.getCliente().getDocumento(),
            pedido.getCostoTotal(),
            java.time.LocalDateTime.now()
        );
        
        pedido.setDatosFacturacion(datosFacturacion);
        
        logger.info("Facturación generada para pedido {}", pedido.getIdPedido());
    }

    /**
     * Simula delay del procesamiento de pago
     */
    private void simulatePaymentDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Delay de procesamiento interrumpido");
        }
    }
} 