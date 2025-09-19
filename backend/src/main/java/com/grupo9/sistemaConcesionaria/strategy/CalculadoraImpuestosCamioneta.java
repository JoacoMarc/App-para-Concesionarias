package com.grupo9.sistemaConcesionaria.strategy;

import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;
import org.springframework.stereotype.Component;

/**
 * CalculadoraImpuestosCamioneta - Estrategia de cálculo de impuestos para CAMIONETAS
 * 
 * Implementación específica del patrón Strategy para vehículos tipo CAMIONETA.
 * Aplica las siguientes tasas impositivas:
 * - Impuesto Nacional: 10%
 * - Impuesto Provincial General: 5%
 * - Impuesto Provincial Adicional: 2%
 */
@Component
public class CalculadoraImpuestosCamioneta implements ICalculadoraImpuestos {
    
    // Constantes de porcentajes impositivos para CAMIONETAS
    private static final double PORCENTAJE_IMPUESTO_NACIONAL = 0.10;        // 10%
    private static final double PORCENTAJE_IMPUESTO_PROVINCIAL_GENERAL = 0.05;  // 5%
    private static final double PORCENTAJE_IMPUESTO_PROVINCIAL_ADICIONAL = 0.02; // 2%
    
    @Override
    public DetalleImpuestos calcularImpuestos(double precioBase) {
        DetalleImpuestos detalle = new DetalleImpuestos(TipoVehiculo.CAMIONETA, precioBase);
        
        // Calcular cada impuesto
        double impuestoNacional = precioBase * PORCENTAJE_IMPUESTO_NACIONAL;
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
        return TipoVehiculo.CAMIONETA;
    }
} 