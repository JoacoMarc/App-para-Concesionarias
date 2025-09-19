package com.grupo9.sistemaConcesionaria.strategy;

import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;
import org.springframework.stereotype.Component;

/**
 * CalculadoraImpuestosCamion - Estrategia de cálculo de impuestos para CAMIONES
 * 
 * Implementación específica del patrón Strategy para vehículos tipo CAMION.
 * Aplica las siguientes tasas impositivas:
 * - Impuesto Nacional: 0% (No aplicable)
 * - Impuesto Provincial General: 5%
 * - Impuesto Provincial Adicional: 2%
 */
@Component
public class CalculadoraImpuestosCamion implements ICalculadoraImpuestos {
    
    // Constantes de porcentajes impositivos para CAMIONES
    private static final double PORCENTAJE_IMPUESTO_NACIONAL = 0.00;        // 0% - No aplicable
    private static final double PORCENTAJE_IMPUESTO_PROVINCIAL_GENERAL = 0.05;  // 5%
    private static final double PORCENTAJE_IMPUESTO_PROVINCIAL_ADICIONAL = 0.02; // 2%
    
    @Override
    public DetalleImpuestos calcularImpuestos(double precioBase) {
        DetalleImpuestos detalle = new DetalleImpuestos(TipoVehiculo.CAMION, precioBase);
        
        // Calcular cada impuesto
        double impuestoNacional = precioBase * PORCENTAJE_IMPUESTO_NACIONAL; // Siempre 0 para camiones
        double impuestoProvincialGeneral = precioBase * PORCENTAJE_IMPUESTO_PROVINCIAL_GENERAL;
        double impuestoProvincialAdicional = precioBase * PORCENTAJE_IMPUESTO_PROVINCIAL_ADICIONAL;
        
        // Establecer valores en el detalle
        detalle.setImpuestoNacional(impuestoNacional);
        detalle.setImpuestoProvincialGeneral(impuestoProvincialGeneral);
        detalle.setImpuestoProvincialAdicional(impuestoProvincialAdicional);
        
        // Establecer porcentajes
        detalle.setPorcentajeNacional(PORCENTAJE_IMPUESTO_NACIONAL);
        detalle.setPorcentajeProvincialGeneral(PORCENTAJE_IMPUESTO_PROVINCIAL_GENERAL);
        detalle.setPorcentajeProvincialAdicional(PORCENTAJE_IMPUESTO_PROVINCIAL_ADICIONAL);
        
        // Calcular totales
        detalle.calcularPrecioFinal();
        
        return detalle;
    }
    
    @Override
    public TipoVehiculo getTipoVehiculo() {
        return TipoVehiculo.CAMION;
    }
} 