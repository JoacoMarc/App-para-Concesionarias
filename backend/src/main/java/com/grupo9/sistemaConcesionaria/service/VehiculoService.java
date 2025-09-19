package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Vehiculo;
import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;
import com.grupo9.sistemaConcesionaria.exception.DuplicateVehiculoException;
import com.grupo9.sistemaConcesionaria.exception.VehiculoNotFoundException;
import com.grupo9.sistemaConcesionaria.repository.VehiculoJsonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * VehiculoService - Servicio para gestión de vehículos con persistencia JSON
 * Migrado desde almacenamiento en memoria a persistencia en archivo JSON
 * Mantiene toda la lógica de negocio original con patrones de diseño
 */
@Service
public class VehiculoService {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoService.class);
    
    @Autowired
    private VehiculoJsonRepository vehiculoRepository;

    /**
     * Inicializa datos de prueba automáticamente al crear el servicio
     */
    @jakarta.annotation.PostConstruct
    public void init() {
        // Solo inicializar si no hay datos existentes
        if (vehiculoRepository.count() == 0) {
            inicializarDatosPrueba();
        } else {
            logger.info("Datos existentes cargados desde JSON: {} vehículos", vehiculoRepository.count());
        }
    }

    /**
     * Registra un nuevo vehículo en el sistema
     * @param vehiculo El vehículo a registrar
     * @throws DuplicateVehiculoException si ya existe un vehículo con el mismo chasis
     */
    public void registrarVehiculo(Vehiculo vehiculo) throws DuplicateVehiculoException {
        if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
            throw new IllegalArgumentException("El vehículo o su número de chasis no pueden ser nulos o vacíos.");
        }
        
        if (vehiculoRepository.existsById(vehiculo.getNumeroChasis())) {
            throw new DuplicateVehiculoException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " ya existe.");
        }
        
        vehiculoRepository.save(vehiculo);
        logger.info("Vehículo registrado: {} {} (Chasis: {})", 
                   vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getNumeroChasis());
    }

    /**
     * Actualiza los datos de un vehículo existente
     * @param vehiculo El vehículo con datos actualizados
     * @throws VehiculoNotFoundException si el vehículo no existe
     */
    public void actualizarDatosVehiculo(Vehiculo vehiculo) throws VehiculoNotFoundException {
        if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
            throw new IllegalArgumentException("El vehículo o su número de chasis no pueden ser nulos o vacíos para actualizar.");
        }
        
        if (!vehiculoRepository.existsById(vehiculo.getNumeroChasis())) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " no encontrado para actualizar.");
        }
        
        vehiculoRepository.save(vehiculo);
        logger.info("Vehículo actualizado: {} {} (Chasis: {})", 
                   vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getNumeroChasis());
    }

    /**
     * Elimina un vehículo del sistema
     * @param numeroChasis El número de chasis del vehículo a eliminar
     * @throws VehiculoNotFoundException si el vehículo no existe
     */
    public void eliminarVehiculo(String numeroChasis) throws VehiculoNotFoundException {
        if (numeroChasis == null || numeroChasis.isEmpty()) {
            throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para eliminar.");
        }
        
        Optional<Vehiculo> vehiculo = vehiculoRepository.findById(numeroChasis);
        if (vehiculo.isEmpty()) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado para eliminar.");
        }
        
        vehiculoRepository.deleteById(numeroChasis);
        logger.info("Vehículo eliminado: {} {} (Chasis: {})", 
                   vehiculo.get().getMarca(), vehiculo.get().getModelo(), numeroChasis);
    }

    /**
     * Obtiene un vehículo por su número de chasis
     * @param numeroChasis El número de chasis del vehículo
     * @return El vehículo encontrado
     * @throws VehiculoNotFoundException si el vehículo no existe
     */
    public Vehiculo getVehiculo(String numeroChasis) throws VehiculoNotFoundException {
        if (numeroChasis == null || numeroChasis.isEmpty()) {
            throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para buscar.");
        }
        
        return vehiculoRepository.findById(numeroChasis)
                .orElseThrow(() -> new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado."));
    }

    /**
     * Obtiene un vehículo por su ID (posición en la lista)
     * @param id El ID del vehículo (posición en la lista)
     * @return El vehículo encontrado o null si no existe
     */
    public Vehiculo getVehiculoPorId(Long id) {
        if (id == null || id < 1) {
            return null;
        }
        
        List<Vehiculo> listaVehiculos = vehiculoRepository.findAll();
        int index = id.intValue() - 1; // Convertir ID a index (1-based to 0-based)
        
        if (index >= 0 && index < listaVehiculos.size()) {
            return listaVehiculos.get(index);
        }
        
        return null;
    }

    /**
     * Obtiene todos los vehículos como lista
     * @return Lista de todos los vehículos
     */
    public List<Vehiculo> getTodosLosVehiculos() {
        return vehiculoRepository.findAll();
    }

    /**
     * Obtiene vehículos disponibles para venta
     * @return Lista de vehículos disponibles
     */
    public List<Vehiculo> getVehiculosDisponibles() {
        return vehiculoRepository.findByDisponibleVenta(true);
    }

    /**
     * Obtiene vehículos por marca
     * @param marca La marca a buscar
     * @return Lista de vehículos de la marca especificada
     */
    public List<Vehiculo> getVehiculosPorMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            return List.of();
        }
        
        return vehiculoRepository.findByMarca(marca);
    }

    /**
     * Obtiene vehículos por tipo
     * @param tipo El tipo de vehículo a buscar
     * @return Lista de vehículos del tipo especificado
     */
    public List<Vehiculo> getVehiculosPorTipo(TipoVehiculo tipo) {
        if (tipo == null) {
            return List.of();
        }
        
        return vehiculoRepository.findByTipo(tipo);
    }

    /**
     * Cambia la disponibilidad de venta de un vehículo
     * @param numeroChasis El número de chasis del vehículo
     * @param disponible True si está disponible, false si no
     * @throws VehiculoNotFoundException si el vehículo no existe
     */
    public void cambiarDisponibilidad(String numeroChasis, boolean disponible) throws VehiculoNotFoundException {
        Vehiculo vehiculo = getVehiculo(numeroChasis);
        vehiculo.setDisponibleVenta(disponible);
        vehiculoRepository.save(vehiculo);
        logger.info("Disponibilidad del vehículo {} cambiada a: {}", numeroChasis, disponible);
    }

    /**
     * Verifica si existe un vehículo con el número de chasis dado
     * @param numeroChasis El número de chasis a verificar
     * @return true si existe, false si no
     */
    public boolean existeVehiculo(String numeroChasis) {
        return numeroChasis != null && vehiculoRepository.existsById(numeroChasis);
    }

    /**
     * Obtiene la cantidad total de vehículos registrados
     * @return Cantidad total de vehículos
     */
    public int getCantidadTotal() {
        return (int) vehiculoRepository.count();
    }

    /**
     * Obtiene la cantidad de vehículos disponibles
     * @return Cantidad de vehículos disponibles
     */
    public int getCantidadDisponibles() {
        return (int) vehiculoRepository.countByDisponibleVenta();
    }

    /**
     * Inicializa datos de prueba para el sistema
     */
    public void inicializarDatosPrueba() {
        try {
            // Crear algunos vehículos de prueba
            Vehiculo auto1 = new Vehiculo("Toyota", "Corolla", "CHI001", "MOT001", 25000.0, TipoVehiculo.AUTO);
            auto1.setColor("Blanco");
            auto1.setCaracteristicas("Automático, A/C, Dirección asistida");
            
            Vehiculo camioneta1 = new Vehiculo("Ford", "Ranger", "CHI002", "MOT002", 35000.0, TipoVehiculo.CAMIONETA);
            camioneta1.setColor("Negro");
            camioneta1.setCaracteristicas("4x4, Diesel, Doble cabina");
            
            Vehiculo moto1 = new Vehiculo("Honda", "CBR250", "CHI003", "MOT003", 8000.0, TipoVehiculo.MOTO);
            moto1.setColor("Rojo");
            moto1.setCaracteristicas("250cc, Deportiva");
            
            vehiculoRepository.save(auto1);
            vehiculoRepository.save(camioneta1);
            vehiculoRepository.save(moto1);
            
            logger.info("Datos de prueba inicializados: {} vehículos creados", getCantidadTotal());
            
        } catch (Exception e) {
            logger.warn("Error inicializando datos de prueba: {}", e.getMessage());
        }
    }
} 