package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;
import com.grupo9.sistemaConcesionaria.strategy.ICalculadoraImpuestos;
import com.grupo9.sistemaConcesionaria.strategy.DetalleImpuestos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ImpuestoService - Servicio para cálculo de impuestos usando Strategy Pattern
 * 
 * Patrón Strategy implementado:
 * - Contexto: ImpuestoService
 * - Estrategia: ICalculadoraImpuestos
 * - Estrategias concretas: CalculadoraImpuestosAuto, CalculadoraImpuestosMoto, etc.
 * 
 * Permite calcular impuestos de manera específica para cada tipo de vehículo
 * sin afectar la lógica de otros tipos.
 */
@Service
public class ImpuestoService {

    private static final Logger logger = LoggerFactory.getLogger(ImpuestoService.class);
    
    // Mapa de estrategias por tipo de vehículo
    private final Map<TipoVehiculo, ICalculadoraImpuestos> calculadoras;

    /**
     * Constructor que inyecta todas las implementaciones de ICalculadoraImpuestos
     * y las organiza por tipo de vehículo
     */
    @Autowired
    public ImpuestoService(List<ICalculadoraImpuestos> calculadorasList) {
        this.calculadoras = calculadorasList.stream()
                .collect(Collectors.toMap(
                    ICalculadoraImpuestos::getTipoVehiculo,
                    Function.identity()
                ));
        
        // Log de estrategias registradas
        calculadoras.forEach((tipo, calculadora) -> {
            logger.info("Estrategia de impuestos registrada para: {}", tipo);
        });
    }

    /**
     * Calcula impuestos usando la estrategia apropiada para el tipo de vehículo
     * 
     * @param tipoVehiculo Tipo de vehículo
     * @param precioBase Precio base del vehículo
     * @return Detalle completo del cálculo de impuestos
     */
    public DetalleImpuestos calcularImpuestos(TipoVehiculo tipoVehiculo, double precioBase) {
        ICalculadoraImpuestos calculadora = calculadoras.get(tipoVehiculo);
        
        if (calculadora == null) {
            logger.error("No hay estrategia de impuestos para el tipo: {}", tipoVehiculo);
            throw new IllegalArgumentException("Tipo de vehículo no soportado: " + tipoVehiculo);
        }
        
        DetalleImpuestos detalle = calculadora.calcularImpuestos(precioBase);
        
        logger.info("Impuestos calculados para {} - Precio Base: $%.2f, Total Impuestos: $%.2f", 
                   tipoVehiculo, precioBase, detalle.getTotalImpuestos());
        
        return detalle;
    }

    /**
     * Obtiene solo el total de impuestos (para compatibilidad con código existente)
     * 
     * @param tipoVehiculo Tipo de vehículo
     * @param precioBase Precio base del vehículo
     * @return Total de impuestos
     */
    public double calcularTotalImpuestos(TipoVehiculo tipoVehiculo, double precioBase) {
        return calcularImpuestos(tipoVehiculo, precioBase).getTotalImpuestos();
    }

    /**
     * Genera un resumen textual del cálculo de impuestos
     * 
     * @param tipoVehiculo Tipo de vehículo
     * @param precioBase Precio base del vehículo
     * @return Resumen textual del cálculo
     */
    public String generarResumenImpuestos(TipoVehiculo tipoVehiculo, double precioBase) {
        DetalleImpuestos detalle = calcularImpuestos(tipoVehiculo, precioBase);
        return detalle.generarResumen();
    }

    /**
     * Verifica si hay estrategia disponible para un tipo de vehículo
     * 
     * @param tipoVehiculo Tipo de vehículo a verificar
     * @return true si hay estrategia disponible
     */
    public boolean tieneEstrategiaParaTipo(TipoVehiculo tipoVehiculo) {
        return calculadoras.containsKey(tipoVehiculo);
    }

    /**
     * Obtiene información de todas las estrategias disponibles
     * 
     * @return Mapa con información de cada estrategia
     */
    public Map<TipoVehiculo, String> obtenerEstrategiasDisponibles() {
        return calculadoras.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().getClass().getSimpleName()
                ));
    }
} 