package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ImpuestosHandler - Handler concreto para el estado IMPUESTOS
 * Procesa el cálculo y aplicación de impuestos del pedido
 */
@Component
public class ImpuestosHandler extends EstadoPedidoHandler {

    private static final Logger logger = LoggerFactory.getLogger(ImpuestosHandler.class);
    
    @Autowired
    private ImpuestoService impuestoService;

    public ImpuestosHandler(NotificacionService notificacionService) {
        super("Impuestos", notificacionService);
    }

    @Override
    protected void procesarEstado(Pedido pedido) throws PedidoNotFoundException {
        logger.info("Procesando pedido {} en estado IMPUESTOS", pedido.getIdPedido());

        try {
            // Procesos específicos del área de impuestos
            calcularImpuestos(pedido);
            aplicarImpuestos(pedido);
            validarCumplimientoFiscal(pedido);

            // Actualizar estado
            actualizarEstadoPedido(pedido, EstadoPedido.IMPUESTOS, "Impuestos calculados y aplicados");

            logger.info("Pedido {} procesado exitosamente en IMPUESTOS", pedido.getIdPedido());

        } catch (Exception e) {
            logger.error("Error procesando pedido {} en IMPUESTOS: {}", pedido.getIdPedido(), e.getMessage());
            throw new PedidoNotFoundException("Error en procesamiento de Impuestos: " + e.getMessage());
        }
    }

    /**
     * Calcula los impuestos según el tipo de vehículo
     */
    private void calcularImpuestos(Pedido pedido) {
        double montoImpuestos = impuestoService.calcularTotalImpuestos(
            pedido.getVehiculo().getTipo(), 
            pedido.getVehiculo().getPrecioBase()
        );
        
        logger.info("Impuestos calculados para vehículo {} {}: ${}",
                   pedido.getVehiculo().getMarca(),
                   pedido.getVehiculo().getModelo(), 
                   montoImpuestos);

        // Actualizar el pedido con los impuestos calculados
        pedido.setImpuestosAplicados(montoImpuestos);
    }

    /**
     * Aplica los impuestos al costo total
     */
    private void aplicarImpuestos(Pedido pedido) {
        // Recalcular el costo total incluyendo impuestos
        pedido.calcularCostoTotal();
        
        logger.info("Costo total actualizado con impuestos: ${:.2f}", pedido.getCostoTotal());

        // Generar detalle de impuestos usando la nueva API
        String detalleImpuestos = impuestoService.generarResumenImpuestos(
            pedido.getVehiculo().getTipo(), 
            pedido.getVehiculo().getPrecioBase()
        );
        
        // Agregar el detalle como configuración adicional
        pedido.agregarConfiguracion("DETALLE IMPUESTOS: " + detalleImpuestos.replace("\n", " | "));
    }

    /**
     * Valida el cumplimiento fiscal del pedido
     */
    private void validarCumplimientoFiscal(Pedido pedido) {
        // Validaciones fiscales básicas
        validarDocumentoCliente(pedido);
        validarMontoImpuestos(pedido);
        generarComprobanteImpuestos(pedido);

        logger.debug("Cumplimiento fiscal validado para pedido {}", pedido.getIdPedido());
    }

    /**
     * Valida que el cliente tenga documento válido para efectos fiscales
     */
    private void validarDocumentoCliente(Pedido pedido) {
        String documento = pedido.getCliente().getDocumento();
        
        if (documento == null || documento.length() < 7) {
            throw new IllegalArgumentException("Documento del cliente no válido para efectos fiscales");
        }

        logger.debug("Documento fiscal validado: {}", documento);
    }

    /**
     * Valida que el monto de impuestos sea coherente
     */
    private void validarMontoImpuestos(Pedido pedido) {
        double impuestos = pedido.getImpuestosAplicados();
        double precioBase = pedido.getVehiculo().getPrecioBase();
        
        // Los impuestos no deberían superar el 50% del precio base
        if (impuestos > precioBase * 0.5) {
            logger.warn("Monto de impuestos elevado: ${} sobre precio base ${}", impuestos, precioBase);
        }

        if (impuestos < 0) {
            throw new IllegalArgumentException("El monto de impuestos no puede ser negativo");
        }
    }

    /**
     * Genera comprobante de impuestos
     */
    private void generarComprobanteImpuestos(Pedido pedido) {
        String comprobante = String.format(
            "COMPROBANTE IMPUESTOS - Pedido: %s - Cliente: %s %s - Vehículo: %s %s - Impuestos: $%.2f - Fecha: %s",
            pedido.getNumeroDePedido(),
            pedido.getCliente().getNombre(),
            pedido.getCliente().getApellido(),
            pedido.getVehiculo().getMarca(),
            pedido.getVehiculo().getModelo(),
            pedido.getImpuestosAplicados(),
            java.time.LocalDateTime.now()
        );

        // Agregar comprobante como configuración
        pedido.agregarConfiguracion("COMPROBANTE: " + comprobante);
        
        logger.info("Comprobante de impuestos generado para pedido {}", pedido.getIdPedido());
    }
} 