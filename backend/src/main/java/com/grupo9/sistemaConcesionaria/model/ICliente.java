package com.grupo9.sistemaConcesionaria.model;

/**
 * Interfaz que define las operaciones básicas que debe implementar un Cliente
 */
public interface ICliente {
    
    /**
     * Obtiene el nombre del cliente
     * @return nombre del cliente
     */
    String getNombre();
    
    /**
     * Obtiene el apellido del cliente
     * @return apellido del cliente
     */
    String getApellido();
    
    /**
     * Obtiene el ID único del cliente
     * @return ID del cliente
     */
    int getIdCliente();
    
    /**
     * Obtiene el documento del cliente
     * @return documento del cliente
     */
    String getDocumento();
    
    /**
     * Obtiene el correo electrónico del cliente
     * @return correo electrónico del cliente
     */
    String getCorreoElectronico();
    
    /**
     * Obtiene el teléfono del cliente
     * @return teléfono del cliente
     */
    String getTelefono();
} 