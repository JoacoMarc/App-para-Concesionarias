package com.grupo9.sistemaConcesionaria.service;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EmailObservador - Observador concreto para envío de notificaciones por email
 * Implementa el Observer Pattern para notificar cambios de estado por correo electrónico
 */
@Component
public class EmailObservador implements ObservadorEstado {

    private static final Logger logger = LoggerFactory.getLogger(EmailObservador.class);
    private static final String TIPO_NOTIFICACION = "EMAIL";

    @Override
    public void actualizar(String estado) {
        enviarEmail(estado);
    }

    @Override
    public String getTipoNotificacion() {
        return TIPO_NOTIFICACION;
    }

    /**
     * Simula el envío de un email con la información del estado
     * @param estado Información del cambio de estado
     */
    private void enviarEmail(String estado) {
        try {
            // Simulación del envío de email
            logger.info("📧 EMAIL ENVIADO: {}", estado);
            
            // Aquí iría la lógica real de envío de email
            // Por ejemplo: integración con JavaMail, SendGrid, AWS SES, etc.
            
            // Simular delay de envío
            Thread.sleep(150);
            
            logger.debug("Email entregado exitosamente");
            
        } catch (InterruptedException e) {
            logger.error("Error al enviar email: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error inesperado al enviar email: {}", e.getMessage());
        }
    }

    /**
     * Método para configurar el servicio de email (futuro)
     * @param servidorSMTP Configuración del servidor SMTP
     * @param puerto Puerto del servidor
     * @param usuario Usuario para autenticación
     */
    public void configurarServicioEmail(String servidorSMTP, int puerto, String usuario) {
        logger.info("Configurando servicio Email - Servidor: {}, Puerto: {}, Usuario: {}", 
                   servidorSMTP, puerto, usuario);
        // Aquí se configuraría el proveedor de email real
    }

    /**
     * Crea el contenido HTML del email (futuro)
     * @param estado Información del estado
     * @return Contenido HTML formateado
     */
    private String crearContenidoHTML(String estado) {
        return String.format("""
            <html>
            <body>
                <h2>Notificación de Sistema Concesionaria</h2>
                <p><strong>Actualización de Estado:</strong></p>
                <p>%s</p>
                <hr>
                <p><small>Este es un mensaje automático del sistema.</small></p>
            </body>
            </html>
            """, estado);
    }
} 