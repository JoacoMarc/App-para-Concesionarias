package com.grupo9.sistemaConcesionaria.strategy;

import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;

/**
 * DetalleImpuestos - DTO para el desglose de impuestos
 * 
 * Contiene toda la información detallada del cálculo de impuestos
 * incluyendo cada tipo de impuesto por separado y el total.
 */
public class DetalleImpuestos {
    
    private TipoVehiculo tipoVehiculo;
    private double precioBase;
    
    // Impuestos específicos
    private double impuestoNacional;
    private double impuestoProvincialGeneral;
    private double impuestoProvincialAdicional;
    
    // Porcentajes aplicados
    private double porcentajeNacional;
    private double porcentajeProvincialGeneral;
    private double porcentajeProvincialAdicional;
    
    // Total
    private double totalImpuestos;
    private double precioFinal;
    
    // Constructor vacío
    public DetalleImpuestos() {}
    
    // Constructor completo
    public DetalleImpuestos(TipoVehiculo tipoVehiculo, double precioBase) {
        this.tipoVehiculo = tipoVehiculo;
        this.precioBase = precioBase;
    }
    
    /**
     * Calcula el precio final sumando precio base + impuestos
     */
    public void calcularPrecioFinal() {
        this.totalImpuestos = impuestoNacional + impuestoProvincialGeneral + impuestoProvincialAdicional;
        this.precioFinal = precioBase + totalImpuestos;
    }
    
    /**
     * Genera un resumen textual del cálculo de impuestos
     */
    public String generarResumen() {
        return String.format(
            "DETALLE IMPUESTOS - %s | " +
            "Precio Base: $%.2f | " +
            "Impuesto Nacional (%.0f%%): $%.2f | " +
            "Impuesto Provincial General (%.0f%%): $%.2f | " +
            "Impuesto Provincial Adicional (%.0f%%): $%.2f | " +
            "TOTAL IMPUESTOS: $%.2f",
            tipoVehiculo.name(),
            precioBase,
            porcentajeNacional * 100,
            impuestoNacional,
            porcentajeProvincialGeneral * 100,
            impuestoProvincialGeneral,
            porcentajeProvincialAdicional * 100,
            impuestoProvincialAdicional,
            totalImpuestos
        );
    }
    
    // Getters y Setters
    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }
    
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
    
    public double getPrecioBase() {
        return precioBase;
    }
    
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }
    
    public double getImpuestoNacional() {
        return impuestoNacional;
    }
    
    public void setImpuestoNacional(double impuestoNacional) {
        this.impuestoNacional = impuestoNacional;
    }
    
    public double getImpuestoProvincialGeneral() {
        return impuestoProvincialGeneral;
    }
    
    public void setImpuestoProvincialGeneral(double impuestoProvincialGeneral) {
        this.impuestoProvincialGeneral = impuestoProvincialGeneral;
    }
    
    public double getImpuestoProvincialAdicional() {
        return impuestoProvincialAdicional;
    }
    
    public void setImpuestoProvincialAdicional(double impuestoProvincialAdicional) {
        this.impuestoProvincialAdicional = impuestoProvincialAdicional;
    }
    
    public double getPorcentajeNacional() {
        return porcentajeNacional;
    }
    
    public void setPorcentajeNacional(double porcentajeNacional) {
        this.porcentajeNacional = porcentajeNacional;
    }
    
    public double getPorcentajeProvincialGeneral() {
        return porcentajeProvincialGeneral;
    }
    
    public void setPorcentajeProvincialGeneral(double porcentajeProvincialGeneral) {
        this.porcentajeProvincialGeneral = porcentajeProvincialGeneral;
    }
    
    public double getPorcentajeProvincialAdicional() {
        return porcentajeProvincialAdicional;
    }
    
    public void setPorcentajeProvincialAdicional(double porcentajeProvincialAdicional) {
        this.porcentajeProvincialAdicional = porcentajeProvincialAdicional;
    }
    
    public double getTotalImpuestos() {
        return totalImpuestos;
    }
    
    public void setTotalImpuestos(double totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }
    
    public double getPrecioFinal() {
        return precioFinal;
    }
    
    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
} 