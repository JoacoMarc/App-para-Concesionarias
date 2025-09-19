package com.grupo9.sistemaConcesionaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * NotificacionService - Implementación concreta del Subject en el Observer Pattern
 * Gestiona los observadores y envía notificaciones cuando cambian los estados de pedidos
 */
@Service
public class NotificacionService implements SubjectObservadorEstado {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    
    private final List<ObservadorEstado> observadores = new ArrayList<>();
    private String estadoActual;

    /**
     * Constructor que inyecta automáticamente todos los observadores disponibles
     */
    @Autowired
    public NotificacionService(List<ObservadorEstado> observadoresList) {
        // Registrar todos los observadores disponibles en el contexto de Spring
        for (ObservadorEstado observador : observadoresList) {
            adjuntar(observador);
            logger.info("Observador registrado: {}", observador.getTipoNotificacion());
        }
    }

    @Override
    public void adjuntar(ObservadorEstado observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
            logger.debug("Observador adjuntado: {}", observador.getTipoNotificacion());
        }
    }

    @Override
    public void quitar(ObservadorEstado observador) {
        if (observador != null && observadores.remove(observador)) {
            logger.debug("Observador removido: {}", observador.getTipoNotificacion());
        }
    }

    @Override
    public void notificar() {
        logger.info("Notificando a {} observadores sobre cambio de estado: {}", 
                   observadores.size(), estadoActual);
        
        for (ObservadorEstado observador : observadores) {
            try {
                observador.actualizar(estadoActual);
                logger.debug("Notificación enviada a: {}", observador.getTipoNotificacion());
            } catch (Exception e) {
                logger.error("Error al notificar al observador {}: {}", 
                           observador.getTipoNotificacion(), e.getMessage());
            }
        }
    }

    @Override
    public void setEstadoActual(String estado) {
        this.estadoActual = estado;
        logger.info("Estado actualizado: {}", estado);
        notificar();
    }

    /**
     * Notifica sobre un cambio específico de pedido
     * @param pedidoId ID del pedido
     * @param nuevoEstado Nuevo estado del pedido
     * @param areaResponsable Área responsable actual
     */
    public void notificarCambioPedido(Long pedidoId, String nuevoEstado, String areaResponsable) {
        String mensaje = String.format("Pedido #%d - Estado: %s - Área: %s - Fecha: %s", 
                                      pedidoId, nuevoEstado, areaResponsable, 
                                      java.time.LocalDateTime.now().toString());
        setEstadoActual(mensaje);
    }

    /**
     * Obtiene la cantidad de observadores registrados
     * @return Número de observadores
     */
    public int getCantidadObservadores() {
        return observadores.size();
    }

    /**
     * Obtiene los tipos de notificación disponibles
     * @return Lista de tipos de notificación
     */
    public List<String> getTiposNotificacion() {
        return observadores.stream()
                .map(ObservadorEstado::getTipoNotificacion)
                .toList();
    }
} 