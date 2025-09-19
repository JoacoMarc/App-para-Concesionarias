package com.grupo9.sistemaConcesionaria.service;

/**
 * Observer Pattern - Interface para observadores de cambios de estado
 * Los observadores serán notificados cuando cambie el estado de un pedido
 */
public interface ObservadorEstado {
    
    /**
     * Método llamado cuando hay una actualización de estado
     * @param estado Información del nuevo estado
     */
    void actualizar(String estado);
    
    /**
     * Obtiene el tipo de notificación que maneja este observador
     * @return Tipo de notificación (SMS, EMAIL, etc.)
     */
    String getTipoNotificacion();
} 