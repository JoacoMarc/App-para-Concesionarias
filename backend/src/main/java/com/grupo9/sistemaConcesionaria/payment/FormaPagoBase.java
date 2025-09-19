package com.grupo9.sistemaConcesionaria.payment;

import java.time.LocalDateTime;

/**
 * FormaPagoBase - Clase base abstracta para todas las formas de pago
 * 
 * Implementa herencia y polimorfismo para las formas de pago.
 * Permite ampliar las opciones de pago sin modificar el código existente.
 * 
 * Patrón Template Method: Define la estructura común del procesamiento de pagos.
 */
public abstract class FormaPagoBase {
    
    protected String identificador;
    protected double monto;
    protected LocalDateTime fechaProcesamiento;
    protected String estado;
    protected String descripcion;
    
    // Constructor protegido
    protected FormaPagoBase(String identificador, double monto, String descripcion) {
        this.identificador = identificador;
        this.monto = monto;
        this.descripcion = descripcion;
        this.estado = "PENDIENTE";
        this.fechaProcesamiento = LocalDateTime.now();
    }
    
    /**
     * Método template que define el flujo de procesamiento de pago
     * No puede ser sobrescrito por las subclases
     */
    public final String procesarPago() {
        validarMonto();
        validarDatosEspecificos();
        ejecutarPago();
        generarComprobante();
        this.estado = "PROCESADO";
        
        return String.format("Pago procesado: %s por $%.2f - %s", 
                           getTipoFormaPago(), monto, getDetallesPago());
    }
    
    /**
     * Validación común del monto
     */
    private void validarMonto() {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
    }
    
    /**
     * Métodos abstractos que deben implementar las subclases
     */
    public abstract String getTipoFormaPago();
    public abstract void validarDatosEspecificos();
    public abstract void ejecutarPago();
    public abstract String getDetallesPago();
    public abstract double calcularComisionORecargo();
    
    /**
     * Método con implementación por defecto que puede ser sobrescrito
     */
    protected void generarComprobante() {
        // Implementación base - puede ser sobrescrita
    }
    
    /**
     * Calcula el monto final incluyendo comisiones o recargos
     */
    public double calcularMontoFinal() {
        return monto + calcularComisionORecargo();
    }
    
    /**
     * Obtiene información resumida del pago
     */
    public String getResumenPago() {
        return String.format("%s - $%.2f (%s)", 
                           getTipoFormaPago(), 
                           calcularMontoFinal(), 
                           estado);
    }
    
    // Getters y Setters
    public String getIdentificador() {
        return identificador;
    }
    
    public double getMonto() {
        return monto;
    }
    
    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    protected void setEstado(String estado) {
        this.estado = estado;
    }
} 