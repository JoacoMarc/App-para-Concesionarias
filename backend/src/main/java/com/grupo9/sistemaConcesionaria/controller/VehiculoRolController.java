package com.grupo9.sistemaConcesionaria.controller;

import com.grupo9.sistemaConcesionaria.model.Vehiculo;
import com.grupo9.sistemaConcesionaria.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador de vehículos con restricciones por rol
 * Implementa los requerimientos específicos de acceso por tipo de usuario
 */
@RestController
@RequestMapping("/api/vehiculos/roles")
@CrossOrigin(origins = "*")
public class VehiculoRolController {

    @Autowired
    private VehiculoService vehiculoService;

    /**
     * ADMINISTRADOR: Ve todos los vehículos sin restricción
     * GET /api/vehiculos/roles/admin/todos
     */
    @GetMapping("/admin/todos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> getTodosLosVehiculos(Authentication auth) {
        try {
            List<Vehiculo> vehiculos = vehiculoService.getTodosLosVehiculos();
            
            Map<String, Object> response = Map.of(
                "vehiculos", vehiculos,
                "total", vehiculos.size(),
                "usuario", auth.getName(),
                "rol", "ADMINISTRADOR",
                "mensaje", "Acceso completo - Todos los vehículos"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error obteniendo vehículos", "detalle", e.getMessage()));
        }
    }

    /**
     * VENDEDOR: Solo vehículos disponibles (no en proceso de venta)
     * GET /api/vehiculos/roles/vendedor/disponibles
     */
    @GetMapping("/vendedor/disponibles")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<?> getVehiculosDisponiblesVendedor(Authentication auth) {
        try {
            List<Vehiculo> todosVehiculos = vehiculoService.getTodosLosVehiculos();
            
            // Filtrar solo vehículos disponibles (no en proceso de venta)
            List<Vehiculo> vehiculosDisponibles = todosVehiculos.stream()
                .filter(v -> v.isDisponibleVenta()) // Solo disponibles
                .collect(Collectors.toList());
            
            Map<String, Object> response = Map.of(
                "vehiculos", vehiculosDisponibles,
                "total", vehiculosDisponibles.size(),
                "totalExcluidos", todosVehiculos.size() - vehiculosDisponibles.size(),
                "usuario", auth.getName(),
                "rol", "VENDEDOR",
                "restricciones", List.of(
                    "No puede ver vehículos en proceso de venta",
                    "No tiene acceso a pedidos de compra",
                    "No tiene acceso a datos de clientes"
                ),
                "mensaje", "Acceso restringido - Solo vehículos disponibles"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error obteniendo vehículos disponibles", "detalle", e.getMessage()));
        }
    }

    /**
     * COMPRADOR: Solo vehículos disponibles (no en proceso de venta)
     * GET /api/vehiculos/roles/comprador/disponibles
     */
    @GetMapping("/comprador/disponibles")
    @PreAuthorize("hasRole('COMPRADOR')")
    public ResponseEntity<?> getVehiculosDisponiblesComprador(Authentication auth) {
        try {
            List<Vehiculo> todosVehiculos = vehiculoService.getTodosLosVehiculos();
            
            // Filtrar solo vehículos disponibles (no en proceso de venta)
            List<Vehiculo> vehiculosDisponibles = todosVehiculos.stream()
                .filter(v -> v.isDisponibleVenta()) // Solo disponibles
                .map(this::createVehiculoSafeForComprador) // Ocultar información sensible
                .collect(Collectors.toList());
            
            Map<String, Object> response = Map.of(
                "vehiculos", vehiculosDisponibles,
                "total", vehiculosDisponibles.size(),
                "usuario", auth.getName(),
                "rol", "COMPRADOR",
                "restricciones", List.of(
                    "No puede ver vehículos en proceso de venta",
                    "No puede ver pedidos de otros compradores",
                    "Solo información básica de vehículos"
                ),
                "mensaje", "Acceso restringido - Solo vehículos disponibles para compra"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error obteniendo vehículos disponibles", "detalle", e.getMessage()));
        }
    }

    /**
     * VENDEDOR: Verificar disponibilidad específica de un vehículo
     * GET /api/vehiculos/roles/{chasis}/disponibilidad
     */
    @GetMapping("/{chasis}/disponibilidad")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<?> verificarDisponibilidad(@PathVariable String chasis, Authentication auth) {
        try {
            Vehiculo vehiculo = vehiculoService.getVehiculo(chasis);
            
            if (vehiculo == null) {
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = Map.of(
                "chasis", chasis,
                "marca", vehiculo.getMarca(),
                "modelo", vehiculo.getModelo(),
                "disponible", vehiculo.isDisponibleVenta(),
                "estado", vehiculo.isDisponibleVenta() ? "DISPONIBLE" : "EN_PROCESO_VENTA",
                "usuario", auth.getName(),
                "rol", "VENDEDOR",
                "mensaje", vehiculo.isDisponibleVenta() ? 
                    "Vehículo disponible para venta" : 
                    "Vehículo no disponible (en proceso de venta)"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error verificando disponibilidad", "detalle", e.getMessage()));
        }
    }

    /**
     * Endpoint universal para vehículos disponibles (VENDEDOR y COMPRADOR)
     * GET /api/vehiculos/roles/disponibles
     */
    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('VENDEDOR') or hasRole('COMPRADOR')")
    public ResponseEntity<?> getVehiculosDisponibles(Authentication auth) {
        
        // Obtener rol del usuario
        String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        
        // Delegar al método específico según el rol
        return switch (rol) {
            case "VENDEDOR" -> getVehiculosDisponiblesVendedor(auth);
            case "COMPRADOR" -> getVehiculosDisponiblesComprador(auth);
            default -> ResponseEntity.status(403)
                .body(Map.of("error", "Rol no autorizado", "rol", rol));
        };
    }

    /**
     * Crea una versión "segura" del vehículo para compradores
     * Oculta información sensible que no deben ver
     */
    private Vehiculo createVehiculoSafeForComprador(Vehiculo vehiculo) {
        Vehiculo vehiculoSafe = new Vehiculo();
        vehiculoSafe.setNumeroChasis(vehiculo.getNumeroChasis());
        vehiculoSafe.setMarca(vehiculo.getMarca());
        vehiculoSafe.setModelo(vehiculo.getModelo());
        vehiculoSafe.setTipo(vehiculo.getTipo());
        vehiculoSafe.setPrecioBase(vehiculo.getPrecioBase());
        vehiculoSafe.setDisponibleVenta(vehiculo.isDisponibleVenta());
        vehiculoSafe.setColor(vehiculo.getColor());
        // No incluir: numeroMotor, características internas, etc.
        
        return vehiculoSafe;
    }
} 