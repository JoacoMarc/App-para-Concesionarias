package com.grupo9.sistemaConcesionaria.model.state;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import org.springframework.stereotype.Component;

/**
 * Estado concreto: Logística
 * Maneja el transporte y seguimiento del vehículo en ruta
 */
@Component
public class LogisticaState extends AbstractEstadoPedido {

    public LogisticaState() {
        super("Logística", "Transporte y seguimiento del vehículo en ruta");
    }

    @Override
    protected void procesarEstadoEspecifico(Pedido pedido) throws Exception {
        logger.info("=== INICIANDO PROCESO DE LOGÍSTICA ===");
        logger.info("Pedido: {} - Vehículo: {} {}", 
                   pedido.getNumeroDePedido(),
                   pedido.getVehiculo().getMarca(),
                   pedido.getVehiculo().getModelo());

        // 1. Coordinar el transporte
        coordinarTransporte(pedido);

        // 2. Establecer ruta de entrega
        establecerRutaEntrega(pedido);

        // 3. Configurar seguimiento en tiempo real
        configurarSeguimiento(pedido);

        // 4. Notificar al cliente sobre el envío
        notificarCliente(pedido);

        // 5. Programar checkpoints de verificación
        programarCheckpoints(pedido);

        // 6. Simular tiempo de procesamiento
        simularProcesamiento(2500);

        logger.info("=== LOGÍSTICA CONFIGURADA EXITOSAMENTE ===");
        agregarConfiguracion(pedido, "LOGÍSTICA: Transporte coordinado y en ruta - " + 
                           java.time.LocalDateTime.now());
    }

    /**
     * Coordina el transporte con la empresa transportista
     */
    private void coordinarTransporte(Pedido pedido) {
        logger.info("Coordinando transporte...");

        // Generar datos de transporte
        String numeroViaje = "VJ-" + System.currentTimeMillis();
        String conductorAsignado = generarConductor();
        String placaVehiculo = generarPlaca();

        // Información del transporte
        agregarConfiguracion(pedido, "LOGÍSTICA: Número de viaje - " + numeroViaje);
        agregarConfiguracion(pedido, "LOGÍSTICA: Conductor asignado - " + conductorAsignado);
        agregarConfiguracion(pedido, "LOGÍSTICA: Vehículo de transporte - " + placaVehiculo);

        // Verificar documentación de transporte
        logger.debug("Verificando documentación de transporte");
        agregarConfiguracion(pedido, "LOGÍSTICA: Documentación de transporte verificada");

        // Confirmar carga del vehículo
        logger.debug("Confirmando carga del vehículo en transporte");
        agregarConfiguracion(pedido, "LOGÍSTICA: Vehículo cargado en transporte");
    }

    /**
     * Establece la ruta óptima de entrega
     */
    private void establecerRutaEntrega(Pedido pedido) {
        logger.info("Estableciendo ruta de entrega...");

        // Calcular ruta basada en la dirección del cliente
        String direccionCliente = pedido.getCliente().getDireccion();
        if (direccionCliente == null) {
            direccionCliente = "Dirección no especificada";
        }

        // Simular cálculo de ruta
        String[] rutasDisponibles = {
            "Ruta A - Autopista principal (2-3 días)",
            "Ruta B - Carreteras secundarias (3-4 días)",
            "Ruta C - Ruta expresa (1-2 días)"
        };

        String rutaSeleccionada = rutasDisponibles[(int) (Math.random() * rutasDisponibles.length)];
        agregarConfiguracion(pedido, "LOGÍSTICA: Ruta seleccionada - " + rutaSeleccionada);

        // Calcular tiempo estimado de entrega
        int diasEstimados = calcularTiempoEntrega(rutaSeleccionada);
        String fechaEstimada = java.time.LocalDateTime.now().plusDays(diasEstimados).toLocalDate().toString();
        
        agregarConfiguracion(pedido, "LOGÍSTICA: Destino - " + direccionCliente);
        agregarConfiguracion(pedido, "LOGÍSTICA: Fecha estimada de entrega - " + fechaEstimada);

        logger.debug("Ruta establecida: {} hacia {}", rutaSeleccionada, direccionCliente);
    }

    /**
     * Configura el sistema de seguimiento en tiempo real
     */
    private void configurarSeguimiento(Pedido pedido) {
        logger.info("Configurando seguimiento...");

        // Generar código de seguimiento
        String codigoSeguimiento = "TRK-" + System.currentTimeMillis();
        agregarConfiguracion(pedido, "LOGÍSTICA: Código de seguimiento - " + codigoSeguimiento);

        // Configurar puntos de control GPS
        String[] puntosControl = {
            "Salida de planta - Coordenadas: -34.6037, -58.3816",
            "Checkpoint 1 - Coordenadas: -34.7037, -58.4816",
            "Checkpoint 2 - Coordenadas: -34.8037, -58.5816"
        };

        for (String punto : puntosControl) {
            agregarConfiguracion(pedido, "LOGÍSTICA: " + punto);
        }

        // Sistema de alertas
        agregarConfiguracion(pedido, "LOGÍSTICA: Sistema de alertas SMS activado");
        agregarConfiguracion(pedido, "LOGÍSTICA: Notificaciones email configuradas");

        logger.debug("Seguimiento configurado con código: {}", codigoSeguimiento);
    }

    /**
     * Notifica al cliente sobre el inicio del transporte
     */
    private void notificarCliente(Pedido pedido) {
        logger.info("Notificando al cliente...");

        String nombreCliente = pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido();
        
        // Simular envío de notificaciones
        String notificacionSMS = String.format(
            "Sr/a %s, su vehículo %s %s está en camino. Código de seguimiento: TRK-%d",
            nombreCliente,
            pedido.getVehiculo().getMarca(),
            pedido.getVehiculo().getModelo(),
            System.currentTimeMillis()
        );

        agregarConfiguracion(pedido, "LOGÍSTICA: SMS enviado - " + notificacionSMS);

        // Email detallado
        agregarConfiguracion(pedido, "LOGÍSTICA: Email detallado enviado con información de seguimiento");

        // Información de contacto para consultas
        agregarConfiguracion(pedido, "LOGÍSTICA: Contacto logística - 0800-VEHICULOS (0800-843424567)");

        logger.debug("Cliente notificado: {}", nombreCliente);
    }

    /**
     * Programa checkpoints de verificación durante el transporte
     */
    private void programarCheckpoints(Pedido pedido) {
        logger.info("Programando checkpoints de verificación...");

        // Checkpoints cada 8 horas durante el transporte
        String[] checkpoints = {
            "08:00 - Verificación de estado del vehículo",
            "16:00 - Verificación de ubicación y documentos",
            "00:00 - Checkpoint nocturno de seguridad"
        };

        for (int i = 0; i < checkpoints.length; i++) {
            agregarConfiguracion(pedido, "LOGÍSTICA: Checkpoint " + (i + 1) + " - " + checkpoints[i]);
        }

        // Protocolo de emergencia
        agregarConfiguracion(pedido, "LOGÍSTICA: Protocolo de emergencia activado");
        agregarConfiguracion(pedido, "LOGÍSTICA: Seguro de transporte vigente");

        logger.debug("Checkpoints programados: {} verificaciones", checkpoints.length);
    }

    /**
     * Genera un nombre de conductor aleatorio
     */
    private String generarConductor() {
        String[] nombres = {"Carlos Rodríguez", "Ana García", "Miguel López", "Laura Martínez", "José Fernández"};
        return nombres[(int) (Math.random() * nombres.length)];
    }

    /**
     * Genera una placa de vehículo de transporte
     */
    private String generarPlaca() {
        return "TR" + (int) (Math.random() * 900 + 100) + "AB";
    }

    /**
     * Calcula el tiempo estimado de entrega basado en la ruta
     */
    private int calcularTiempoEntrega(String ruta) {
        if (ruta.contains("expresa")) return 2;
        if (ruta.contains("principal")) return 3;
        return 4; // ruta secundaria
    }

    @Override
    protected EstadoPedido mapearAEstadoEnum() {
        return EstadoPedido.LOGISTICA;
    }

    @Override
    public boolean puedeAvanzar(Pedido pedido) {
        // Verificar que el cliente tenga dirección válida para la entrega
        return pedido.getCliente() != null && 
               (pedido.getCliente().getDireccion() != null || 
                pedido.getCliente().getDireccionFacturacion() != null);
    }
} 