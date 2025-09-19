package com.grupo9.sistemaConcesionaria.strategy;

import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;

/**
 * ICalculadoraImpuestos - Interfaz Strategy para cálculo de impuestos
 * 
 * Patrón Strategy: Define una familia de algoritmos de cálculo de impuestos,
 * los encapsula y los hace intercambiables. Permite que el algoritmo varíe
 * independientemente de los clientes que lo usan.
 * 
 * Cada tipo de vehículo tiene su propia estrategia de cálculo de impuestos
 * según las regulaciones fiscales específicas.
 */
public interface ICalculadoraImpuestos {
    
    /**
     * Calcula el total de impuestos para un vehículo
     * @param precioBase Precio base del vehículo antes de impuestos
     * @return DTO con el desglose completo de impuestos
     */
    DetalleImpuestos calcularImpuestos(double precioBase);
    
    /**
     * Obtiene el tipo de vehículo al que aplica esta estrategia
     * @return Tipo de vehículo
     */
    TipoVehiculo getTipoVehiculo();
    
    /**
     * Verifica si esta estrategia es aplicable al tipo de vehículo dado
     * @param tipoVehiculo Tipo de vehículo a verificar
     * @return true si es aplicable, false en caso contrario
     */
    default boolean esAplicable(TipoVehiculo tipoVehiculo) {
        return getTipoVehiculo().equals(tipoVehiculo);
    }
} 