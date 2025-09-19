package com.grupo9.sistemaConcesionaria.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grupo9.sistemaConcesionaria.model.Vehiculo;
import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * VehiculoJsonRepository - Repositorio JSON para entidades Vehiculo
 * Maneja la persistencia de vehículos en archivo vehiculos.json
 */
@Repository
public class VehiculoJsonRepository extends BaseJsonRepository<Vehiculo, String> {

    public VehiculoJsonRepository() {
        super(
            "vehiculos.json",
            new TypeReference<List<Vehiculo>>() {},
            Vehiculo::getNumeroChasis,
            vehiculo -> vehiculo // No necesitamos setter para chasis ya que es manual
        );
    }

    @Override
    protected Vehiculo generateId(Vehiculo vehiculo, List<Vehiculo> existingEntities) {
        // Para vehículos, el chasis debe ser único y se establece manualmente
        // Si no tiene chasis, generamos uno automático
        if (vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
            String generatedChasis = "AUTO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            vehiculo.setNumeroChasis(generatedChasis);
        }
        return vehiculo;
    }

    /**
     * Busca vehículos por marca
     * @param marca Marca del vehículo
     * @return Lista de vehículos de la marca especificada
     */
    public List<Vehiculo> findByMarca(String marca) {
        return findAll().stream()
                .filter(v -> marca.equalsIgnoreCase(v.getMarca()))
                .toList();
    }

    /**
     * Busca vehículos por tipo
     * @param tipo Tipo de vehículo
     * @return Lista de vehículos del tipo especificado
     */
    public List<Vehiculo> findByTipo(TipoVehiculo tipo) {
        return findAll().stream()
                .filter(v -> tipo.equals(v.getTipo()))
                .toList();
    }

    /**
     * Busca vehículos disponibles para venta
     * @return Lista de vehículos disponibles
     */
    public List<Vehiculo> findByDisponibleVenta(boolean disponible) {
        return findAll().stream()
                .filter(v -> v.isDisponibleVenta() == disponible)
                .toList();
    }

    /**
     * Busca vehículos por rango de precio
     * @param precioMin Precio mínimo
     * @param precioMax Precio máximo
     * @return Lista de vehículos en el rango de precio
     */
    public List<Vehiculo> findByPrecioBetween(double precioMin, double precioMax) {
        return findAll().stream()
                .filter(v -> v.getPrecioBase() >= precioMin && v.getPrecioBase() <= precioMax)
                .toList();
    }

    /**
     * Cuenta vehículos por tipo
     * @param tipo Tipo de vehículo
     * @return Cantidad de vehículos del tipo
     */
    public long countByTipo(TipoVehiculo tipo) {
        return findAll().stream()
                .filter(v -> tipo.equals(v.getTipo()))
                .count();
    }

    /**
     * Cuenta vehículos disponibles
     * @return Cantidad de vehículos disponibles para venta
     */
    public long countByDisponibleVenta() {
        return findAll().stream()
                .filter(Vehiculo::isDisponibleVenta)
                .count();
    }
} 