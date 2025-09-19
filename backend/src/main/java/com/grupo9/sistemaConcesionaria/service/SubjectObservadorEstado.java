package com.grupo9.sistemaConcesionaria.service;

/**
 * Observer Pattern - Interface Subject para gestión de observadores
 * Define los métodos para adjuntar, quitar y notificar observadores
 */
public interface SubjectObservadorEstado {
    
    /**
     * Adjunta un observador a la lista de notificaciones
     * @param observador El observador a adjuntar
     */
    void adjuntar(ObservadorEstado observador);
    
    /**
     * Quita un observador de la lista de notificaciones
     * @param observador El observador a quitar
     */
    void quitar(ObservadorEstado observador);
    
    /**
     * Notifica a todos los observadores adjuntos
     */
    void notificar();
    
    /**
     * Establece el estado actual y notifica a los observadores
     * @param estado El nuevo estado
     */
    void setEstadoActual(String estado);
} 