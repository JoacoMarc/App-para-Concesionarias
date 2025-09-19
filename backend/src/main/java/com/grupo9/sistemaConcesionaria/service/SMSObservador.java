package com.grupo9.sistemaConcesionaria.service;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SMSObservador - Observador concreto para envío de notificaciones por SMS
 * Implementa el Observer Pattern para notificar cambios de estado por SMS
 */
@Component
public class SMSObservador implements ObservadorEstado {

    private static final Logger logger = LoggerFactory.getLogger(SMSObservador.class);
    private static final String TIPO_NOTIFICACION = "SMS";

    @Override
    public void actualizar(String estado) {
        enviarSMS(estado);
    }

    @Override
    public String getTipoNotificacion() {
        return TIPO_NOTIFICACION;
    }

    /**
     * Simula el envío de un SMS con la información del estado
     * @param estado Información del cambio de estado
     */
    private void enviarSMS(String estado) {
        try {
            // Simulación del envío de SMS
            logger.info("📱 SMS ENVIADO: {}", estado);
            
            // Aquí iría la lógica real de envío de SMS
            // Por ejemplo: integración con Twilio, AWS SNS, etc.
            
            // Simular delay de envío
            Thread.sleep(100);
            
            logger.debug("SMS entregado exitosamente");
            
        } catch (InterruptedException e) {
            logger.error("Error al enviar SMS: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error inesperado al enviar SMS: {}", e.getMessage());
        }
    }

    /**
     * Método para configurar el servicio de SMS (futuro)
     * @param configuracion Configuración del proveedor de SMS
     */
    public void configurarServicioSMS(String configuracion) {
        logger.info("Configurando servicio SMS: {}", configuracion);
        // Aquí se configuraría el proveedor de SMS real
    }
} 