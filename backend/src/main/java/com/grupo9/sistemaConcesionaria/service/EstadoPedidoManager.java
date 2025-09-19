package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.model.state.*;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

/**
 * Manager que coordina el patrón State para los estados de pedidos
 * Integra Chain of Responsibility (Ventas→Cobranzas→Impuestos) con State Pattern (Embarque→Logística→Entrega)
 */
@Service
public class EstadoPedidoManager {

    private static final Logger logger = LoggerFactory.getLogger(EstadoPedidoManager.class);

    // Chain of Responsibility (estados iniciales)
    @Autowired
    private VentasHandler ventasHandler;
    
    @Autowired
    private CobranzasHandler cobranzasHandler;
    
    @Autowired
    private ImpuestosHandler impuestosHandler;

    // State Pattern (estados finales)
    @Autowired
    private EmbarqueState embarqueState;
    
    @Autowired
    private LogisticaState logisticaState;
    
    @Autowired
    private EntregaState entregaState;

    @PostConstruct
    public void configurarEstados() {
        // Configurar cadena State Pattern
        embarqueState.setSiguienteEstado(logisticaState);
        logisticaState.setSiguienteEstado(entregaState);
        // entregaState es final (no tiene siguiente)

        logger.info("Estados configurados: Embarque → Logística → Entrega");
        logger.info("Manager de estados inicializado correctamente");
    }

    /**
     * Procesa un pedido a través de toda la cadena de estados
     * @param pedido El pedido a procesar
     * @throws PedidoNotFoundException si hay errores en el procesamiento
     */
    public void procesarPedidoCompleto(Pedido pedido) throws PedidoNotFoundException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        logger.info("Iniciando procesamiento completo del pedido: {}", pedido.getNumeroDePedido());

        try {
            // FASE 1: Chain of Responsibility (Ventas → Cobranzas → Impuestos)
            procesarFaseChainOfResponsibility(pedido);

            // FASE 2: State Pattern (Embarque → Logística → Entrega)
            procesarFaseStatePattern(pedido);

            // FINALIZACIÓN
            finalizarPedido(pedido);

            logger.info("Procesamiento completo finalizado para pedido: {}", pedido.getNumeroDePedido());

        } catch (Exception e) {
            logger.error("Error en procesamiento completo del pedido {}: {}", 
                        pedido.getNumeroDePedido(), e.getMessage());
            throw new PedidoNotFoundException("Error en procesamiento: " + e.getMessage());
        }
    }

    /**
     * Procesa la fase de Chain of Responsibility
     */
    private void procesarFaseChainOfResponsibility(Pedido pedido) throws PedidoNotFoundException {
        logger.info("=== FASE 1: CHAIN OF RESPONSIBILITY ===");
        logger.info("Procesando: Ventas → Cobranzas → Impuestos");
        
        // Configurar y ejecutar cadena
        ventasHandler
            .setSiguiente(cobranzasHandler)
            .setSiguiente(impuestosHandler);
        
        // Procesar a través de la cadena
        ventasHandler.procesar(pedido);
        
        logger.info("Fase Chain of Responsibility completada");
    }

    /**
     * Procesa la fase de State Pattern
     */
    private void procesarFaseStatePattern(Pedido pedido) throws Exception {
        logger.info("=== FASE 2: STATE PATTERN ===");
        logger.info("Procesando: Embarque → Logística → Entrega");
        
        // Inicializar con el primer estado de la fase State
        pedido.setEstadoState(embarqueState);
        
        // Procesar todos los estados de la secuencia
        while (pedido.getEstadoState() != null && !pedido.getEstadoState().esFinal()) {
            EstadoPedidoState estadoActual = pedido.getEstadoState();
            
            logger.info("Procesando estado: {}", estadoActual.getNombreEstado());
            
            // Procesar estado actual
            pedido.procesarConState();
            
            // Avanzar al siguiente estado
            if (pedido.puedeAvanzar()) {
                boolean avanzado = pedido.avanzarEstado();
                if (!avanzado) {
                    logger.warn("No se pudo avanzar desde estado: {}", estadoActual.getNombreEstado());
                    break;
                }
            } else {
                logger.warn("El pedido no puede avanzar desde estado: {}", estadoActual.getNombreEstado());
                break;
            }
        }
        
        // Procesar estado final si existe
        if (pedido.getEstadoState() != null) {
            logger.info("Procesando estado final: {}", pedido.getEstadoState().getNombreEstado());
            pedido.procesarConState();
        }
        
        logger.info("Fase State Pattern completada");
    }

    /**
     * Finaliza el pedido marcándolo como completado
     */
    private void finalizarPedido(Pedido pedido) {
        logger.info("=== FINALIZANDO PEDIDO ===");
        
        // Cambiar a estado final
        pedido.cambiarEstado(EstadoPedido.COMPLETADO, "Sistema");
        
        // Agregar configuración final
        pedido.agregarConfiguracion("COMPLETADO: Pedido procesado exitosamente en todas las etapas - " + 
                                   java.time.LocalDateTime.now());
        
        logger.info("Pedido {} marcado como COMPLETADO", pedido.getNumeroDePedido());
    }

    /**
     * Procesa solo un estado específico
     * @param pedido El pedido a procesar
     * @param estadoEspecifico El estado específico a procesar
     */
    public void procesarEstadoEspecifico(Pedido pedido, EstadoPedido estadoEspecifico) throws Exception {
        if (pedido == null || estadoEspecifico == null) {
            throw new IllegalArgumentException("Pedido y estado no pueden ser nulos");
        }

        logger.info("Procesando estado específico {} para pedido {}", 
                   estadoEspecifico, pedido.getNumeroDePedido());

        try {
            switch (estadoEspecifico) {
                case VENTAS:
                    ventasHandler.procesar(pedido);
                    break;
                case COBRANZAS:
                    cobranzasHandler.procesar(pedido);
                    break;
                case IMPUESTOS:
                    impuestosHandler.procesar(pedido);
                    break;
                case EMBARQUE:
                    pedido.setEstadoState(embarqueState);
                    pedido.procesarConState();
                    break;
                case LOGISTICA:
                    pedido.setEstadoState(logisticaState);
                    pedido.procesarConState();
                    break;
                case ENTREGA:
                    pedido.setEstadoState(entregaState);
                    pedido.procesarConState();
                    break;
                default:
                    logger.warn("Estado no manejado específicamente: {}", estadoEspecifico);
            }

            logger.info("Estado {} procesado exitosamente", estadoEspecifico);

        } catch (Exception e) {
            logger.error("Error procesando estado {} para pedido {}: {}", 
                        estadoEspecifico, pedido.getNumeroDePedido(), e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene información del estado actual del pedido
     */
    public String getInformacionEstado(Pedido pedido) {
        if (pedido.getEstadoState() != null) {
            return pedido.getEstadoInfo();
        }
        return "Estado: " + pedido.getEstadoActual().getDescripcion();
    }

    /**
     * Verifica si un pedido puede continuar con el procesamiento
     */
    public boolean puedeProcesamientoContinuar(Pedido pedido) {
        if (pedido.getEstadoActual() == EstadoPedido.COMPLETADO || 
            pedido.getEstadoActual() == EstadoPedido.CANCELADO) {
            return false;
        }

        if (pedido.getEstadoState() != null) {
            return pedido.puedeAvanzar();
        }

        return true;
    }
} 