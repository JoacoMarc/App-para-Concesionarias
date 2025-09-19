package com.grupo9.sistemaConcesionaria.model.state;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import org.springframework.stereotype.Component;

/**
 * Estado concreto: Embarque
 * Maneja la preparación y empaque del vehículo para envío
 */
@Component
public class EmbarqueState extends AbstractEstadoPedido {

    public EmbarqueState() {
        super("Embarque", "Preparación y empaque del vehículo para envío");
    }

    @Override
    protected void procesarEstadoEspecifico(Pedido pedido) throws Exception {
        logger.info("=== INICIANDO PROCESO DE EMBARQUE ===");
        logger.info("Pedido: {} - Vehículo: {} {}", 
                   pedido.getNumeroDePedido(),
                   pedido.getVehiculo().getMarca(),
                   pedido.getVehiculo().getModelo());

        // 1. Verificar disponibilidad del vehículo
        verificarDisponibilidadVehiculo(pedido);

        // 2. Preparar vehículo para embarque
        prepararVehiculo(pedido);

        // 3. Inspección de calidad pre-embarque
        realizarInspeccionCalidad(pedido);

        // 4. Generar documentación de embarque
        generarDocumentacionEmbarque(pedido);

        // 5. Asignar transportista
        asignarTransportista(pedido);

        // 6. Simular tiempo de procesamiento
        simularProcesamiento(2000);

        logger.info("=== EMBARQUE COMPLETADO EXITOSAMENTE ===");
        agregarConfiguracion(pedido, "EMBARQUE: Vehículo preparado y listo para envío - " + 
                           java.time.LocalDateTime.now());
    }

    /**
     * Verifica que el vehículo esté disponible para embarque
     */
    private void verificarDisponibilidadVehiculo(Pedido pedido) {
        logger.info("Verificando disponibilidad del vehículo...");
        
        String chasis = pedido.getVehiculo().getNumeroChasis();
        if (chasis == null || chasis.isEmpty()) {
            throw new IllegalStateException("Vehículo sin número de chasis válido");
        }

        // Simular verificación en inventario
        logger.debug("Vehículo chasis {} disponible para embarque", chasis);
        agregarConfiguracion(pedido, "EMBARQUE: Vehículo verificado - Chasis: " + chasis);
    }

    /**
     * Prepara el vehículo para embarque
     */
    private void prepararVehiculo(Pedido pedido) {
        logger.info("Preparando vehículo para embarque...");

        // Limpieza y preparación
        logger.debug("Realizando limpieza completa del vehículo");
        agregarConfiguracion(pedido, "EMBARQUE: Limpieza completa realizada");

        // Verificar niveles de fluidos
        logger.debug("Verificando niveles de fluidos");
        agregarConfiguracion(pedido, "EMBARQUE: Fluidos verificados y completados");

        // Carga de combustible mínima
        logger.debug("Cargando combustible para entrega");
        agregarConfiguracion(pedido, "EMBARQUE: Combustible cargado para entrega");

        // Protección para transporte
        logger.debug("Aplicando protección para transporte");
        agregarConfiguracion(pedido, "EMBARQUE: Protección de transporte aplicada");
    }

    /**
     * Realiza inspección de calidad pre-embarque
     */
    private void realizarInspeccionCalidad(Pedido pedido) {
        logger.info("Realizando inspección de calidad...");

        // Inspección exterior
        logger.debug("Inspeccionando estado exterior del vehículo");
        agregarConfiguracion(pedido, "EMBARQUE: Inspección exterior - OK");

        // Inspección interior
        logger.debug("Inspeccionando estado interior del vehículo");
        agregarConfiguracion(pedido, "EMBARQUE: Inspección interior - OK");

        // Verificación mecánica básica
        logger.debug("Verificación mecánica básica");
        agregarConfiguracion(pedido, "EMBARQUE: Verificación mecánica - OK");

        // Documentos del vehículo
        logger.debug("Verificando documentación del vehículo");
        agregarConfiguracion(pedido, "EMBARQUE: Documentación verificada - OK");
    }

    /**
     * Genera la documentación necesaria para embarque
     */
    private void generarDocumentacionEmbarque(Pedido pedido) {
        logger.info("Generando documentación de embarque...");

        String numeroEmbarque = "EMB-" + System.currentTimeMillis();
        
        // Orden de embarque
        String ordenEmbarque = String.format(
            "ORDEN DE EMBARQUE N° %s\nPedido: %s\nCliente: %s %s\nVehículo: %s %s\nChasis: %s\nFecha: %s",
            numeroEmbarque,
            pedido.getNumeroDePedido(),
            pedido.getCliente().getNombre(),
            pedido.getCliente().getApellido(),
            pedido.getVehiculo().getMarca(),
            pedido.getVehiculo().getModelo(),
            pedido.getVehiculo().getNumeroChasis(),
            java.time.LocalDateTime.now()
        );

        agregarConfiguracion(pedido, "EMBARQUE: " + ordenEmbarque.replace("\n", " | "));
        
        // Lista de verificación
        agregarConfiguracion(pedido, "EMBARQUE: Lista de verificación completada - " + numeroEmbarque);
        
        logger.debug("Documentación de embarque generada: {}", numeroEmbarque);
    }

    /**
     * Asigna transportista para el envío
     */
    private void asignarTransportista(Pedido pedido) {
        logger.info("Asignando transportista...");

        // Simular asignación de transportista
        String[] transportistas = {
            "Transportes Rápidos SA", 
            "Logística Premium SRL", 
            "Envíos Seguros SA",
            "TransAuto Express SRL"
        };
        
        String transportistaAsignado = transportistas[(int) (Math.random() * transportistas.length)];
        String numeroGuia = "GUIA-" + System.currentTimeMillis();
        
        agregarConfiguracion(pedido, "EMBARQUE: Transportista asignado - " + transportistaAsignado);
        agregarConfiguracion(pedido, "EMBARQUE: Número de guía - " + numeroGuia);
        
        logger.debug("Transportista asignado: {} - Guía: {}", transportistaAsignado, numeroGuia);
    }

    @Override
    protected EstadoPedido mapearAEstadoEnum() {
        return EstadoPedido.EMBARQUE;
    }

    @Override
    public boolean puedeAvanzar(Pedido pedido) {
        // Verificar que todas las validaciones de embarque estén completas
        return pedido.getVehiculo() != null && 
               pedido.getVehiculo().getNumeroChasis() != null &&
               !pedido.getVehiculo().getNumeroChasis().isEmpty();
    }
} 