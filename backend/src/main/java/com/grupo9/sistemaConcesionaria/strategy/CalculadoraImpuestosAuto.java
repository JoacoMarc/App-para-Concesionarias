package com.grupo9.sistemaConcesionaria.strategy;

import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;
import org.springframework.stereotype.Component;

/**
 * CalculadoraImpuestosAuto - Estrategia de cálculo de impuestos para AUTOS
 * 
 * Implementación específica del patrón Strategy para vehículos tipo AUTO.
 * Aplica las siguientes tasas impositivas:
 * - Impuesto Nacional: 21%
 * - Impuesto Provincial General: 5%
 * - Impuesto Provincial Adicional: 1%
 */
@Component
public class CalculadoraImpuestosAuto implements ICalculadoraImpuestos {
    
    // Constantes de porcentajes impositivos para AUTOS
    private static final double PORCENTAJE_IMPUESTO_NACIONAL = 0.21;        // 21%
    private static final double PORCENTAJE_IMPUESTO_PROVINCIAL_GENERAL = 0.05;  // 5%
    private static final double PORCENTAJE_IMPUESTO_PROVINCIAL_ADICIONAL = 0.01; // 1%
    
    @Override
    public DetalleImpuestos calcularImpuestos(double precioBase) {
        DetalleImpuestos detalle = new DetalleImpuestos(TipoVehiculo.AUTO, precioBase);
        
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
        return TipoVehiculo.AUTO;
    }
} 