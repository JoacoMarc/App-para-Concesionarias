package com.grupo9.sistemaConcesionaria.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoRestController {

    // Recreamos las clases necesarias como clases internas para evitar problemas de import
    private final GestorVehiculos gestorVehiculos;

    public VehiculoRestController() {
        this.gestorVehiculos = GestorVehiculos.getInstancia();
    }

    // GET /api/vehiculos - Obtener todos los vehículos
    @GetMapping
    public ResponseEntity<Collection<Vehiculo>> obtenerTodosLosVehiculos() {
        try {
            Collection<Vehiculo> vehiculos = gestorVehiculos.getTodosLosVehiculos();
            return ResponseEntity.ok(vehiculos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/vehiculos/{numeroChasis} - Obtener vehículo por número de chasis
    @GetMapping("/{numeroChasis}")
    public ResponseEntity<?> obtenerVehiculoPorChasis(@PathVariable String numeroChasis) {
        try {
            Vehiculo vehiculo = gestorVehiculos.getVehiculo(numeroChasis);
            return ResponseEntity.ok(vehiculo);
        } catch (VehiculoNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vehículo no encontrado con número de chasis: " + numeroChasis);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // GET /api/vehiculos/disponibles - Obtener vehículos disponibles para venta
    @GetMapping("/disponibles")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosDisponibles() {
        try {
            Collection<Vehiculo> todosLosVehiculos = gestorVehiculos.getTodosLosVehiculos();
            List<Vehiculo> vehiculosDisponibles = new ArrayList<>();
            
            for (Vehiculo vehiculo : todosLosVehiculos) {
                if (vehiculo.isDisponibleVenta()) {
                    vehiculosDisponibles.add(vehiculo);
                }
            }
            
            return ResponseEntity.ok(vehiculosDisponibles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/vehiculos/marca/{marca} - Obtener vehículos por marca
    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosPorMarca(@PathVariable String marca) {
        try {
            Collection<Vehiculo> todosLosVehiculos = gestorVehiculos.getTodosLosVehiculos();
            List<Vehiculo> vehiculosPorMarca = new ArrayList<>();
            
            for (Vehiculo vehiculo : todosLosVehiculos) {
                if (vehiculo.getMarca() != null && vehiculo.getMarca().equalsIgnoreCase(marca)) {
                    vehiculosPorMarca.add(vehiculo);
                }
            }
            
            return ResponseEntity.ok(vehiculosPorMarca);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/vehiculos - Crear un nuevo vehículo
    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody VehiculoRequest vehiculoRequest) {
        try {
            // Validaciones básicas
            if (vehiculoRequest.marca == null || vehiculoRequest.marca.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "La marca es obligatoria"));
            }
            
            if (vehiculoRequest.modelo == null || vehiculoRequest.modelo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El modelo es obligatorio"));
            }
            
            if (vehiculoRequest.numeroChasis == null || vehiculoRequest.numeroChasis.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El número de chasis es obligatorio"));
            }
            
            if (vehiculoRequest.numeroMotor == null || vehiculoRequest.numeroMotor.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El número de motor es obligatorio"));
            }

            // Crear nuevo vehículo
            Vehiculo nuevoVehiculo = new Vehiculo();
            nuevoVehiculo.setMarca(vehiculoRequest.marca);
            nuevoVehiculo.setModelo(vehiculoRequest.modelo);
            nuevoVehiculo.setColor(vehiculoRequest.color != null ? vehiculoRequest.color : "No especificado");
            nuevoVehiculo.setNumeroChasis(vehiculoRequest.numeroChasis);
            nuevoVehiculo.setNumeroMotor(vehiculoRequest.numeroMotor);
            nuevoVehiculo.setPrecioBase(vehiculoRequest.precioBase != null ? vehiculoRequest.precioBase : 0.0);
            nuevoVehiculo.setDisponibleVenta(vehiculoRequest.disponibleVenta != null ? vehiculoRequest.disponibleVenta : true);
            
            // Generar ID automático
            nuevoVehiculo.setIdVehiculo(generateNextId());

            gestorVehiculos.registrarVehiculo(nuevoVehiculo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Vehículo creado exitosamente");
            response.put("vehiculo", nuevoVehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (DuplicateVehiculoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    // PUT /api/vehiculos/{numeroChasis} - Actualizar un vehículo existente
    @PutMapping("/{numeroChasis}")
    public ResponseEntity<?> actualizarVehiculo(@PathVariable String numeroChasis, 
                                               @RequestBody VehiculoRequest vehiculoRequest) {
        try {
            Vehiculo vehiculoExistente = gestorVehiculos.getVehiculo(numeroChasis);
            
            if (vehiculoRequest.marca != null) vehiculoExistente.setMarca(vehiculoRequest.marca);
            if (vehiculoRequest.modelo != null) vehiculoExistente.setModelo(vehiculoRequest.modelo);
            if (vehiculoRequest.color != null) vehiculoExistente.setColor(vehiculoRequest.color);
            if (vehiculoRequest.numeroMotor != null) vehiculoExistente.setNumeroMotor(vehiculoRequest.numeroMotor);
            if (vehiculoRequest.precioBase != null) vehiculoExistente.setPrecioBase(vehiculoRequest.precioBase);
            if (vehiculoRequest.disponibleVenta != null) vehiculoExistente.setDisponibleVenta(vehiculoRequest.disponibleVenta);

            gestorVehiculos.actualizarDatosVehiculo(vehiculoExistente);
            
            return ResponseEntity.ok(Map.of("mensaje", "Vehículo actualizado exitosamente", "vehiculo", vehiculoExistente));
            
        } catch (VehiculoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Vehículo no encontrado con número de chasis: " + numeroChasis));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    // DELETE /api/vehiculos/{numeroChasis} - Eliminar un vehículo
    @DeleteMapping("/{numeroChasis}")
    public ResponseEntity<?> eliminarVehiculo(@PathVariable String numeroChasis) {
        try {
            gestorVehiculos.eliminarVehiculo(numeroChasis);
            return ResponseEntity.ok(Map.of("mensaje", "Vehículo eliminado exitosamente"));
        } catch (VehiculoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Vehículo no encontrado con número de chasis: " + numeroChasis));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    // PATCH /api/vehiculos/{numeroChasis}/disponibilidad - Cambiar disponibilidad
    @PatchMapping("/{numeroChasis}/disponibilidad")
    public ResponseEntity<?> cambiarDisponibilidad(@PathVariable String numeroChasis, 
                                                  @RequestParam boolean disponible) {
        try {
            Vehiculo vehiculo = gestorVehiculos.getVehiculo(numeroChasis);
            vehiculo.setDisponibleVenta(disponible);
            gestorVehiculos.actualizarDatosVehiculo(vehiculo);
            
            return ResponseEntity.ok(Map.of("mensaje", "Disponibilidad actualizada", "vehiculo", vehiculo));
        } catch (VehiculoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Vehículo no encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    // POST /api/vehiculos/inicializar - Cargar datos de ejemplo
    @PostMapping("/inicializar")
    public ResponseEntity<?> inicializarDatos() {
        try {
            // Limpiar datos existentes y cargar datos de ejemplo
            gestorVehiculos.getTodosLosVehiculos().clear();
            
            // Crear vehículos de ejemplo
            Vehiculo auto1 = new Vehiculo();
            auto1.setIdVehiculo(1);
            auto1.setMarca("Toyota");
            auto1.setModelo("Corolla");
            auto1.setColor("Blanco");
            auto1.setNumeroChasis("TOY001");
            auto1.setNumeroMotor("TOY001M");
            auto1.setPrecioBase(25000.0);
            auto1.setDisponibleVenta(true);
            
            Vehiculo auto2 = new Vehiculo();
            auto2.setIdVehiculo(2);
            auto2.setMarca("Honda");
            auto2.setModelo("Civic");
            auto2.setColor("Negro");
            auto2.setNumeroChasis("HON001");
            auto2.setNumeroMotor("HON001M");
            auto2.setPrecioBase(28000.0);
            auto2.setDisponibleVenta(true);
            
            Vehiculo auto3 = new Vehiculo();
            auto3.setIdVehiculo(3);
            auto3.setMarca("Ford");
            auto3.setModelo("Focus");
            auto3.setColor("Azul");
            auto3.setNumeroChasis("FOR001");
            auto3.setNumeroMotor("FOR001M");
            auto3.setPrecioBase(24000.0);
            auto3.setDisponibleVenta(false);
            
            gestorVehiculos.registrarVehiculo(auto1);
            gestorVehiculos.registrarVehiculo(auto2);
            gestorVehiculos.registrarVehiculo(auto3);
            
            return ResponseEntity.ok(Map.of("mensaje", "Datos inicializados correctamente", 
                "vehiculos", gestorVehiculos.getTodosLosVehiculos()));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al inicializar datos: " + e.getMessage()));
        }
    }

    // Método auxiliar para generar IDs
    private int generateNextId() {
        Collection<Vehiculo> vehiculos = gestorVehiculos.getTodosLosVehiculos();
        int maxId = 0;
        for (Vehiculo v : vehiculos) {
            if (v.getIdVehiculo() > maxId) {
                maxId = v.getIdVehiculo();
            }
        }
        return maxId + 1;
    }

    // =============== CLASES COPIADAS DESDE src/controller ===============
    // Nota: Estas son copias de las clases existentes para evitar problemas de import
    
    public static class Vehiculo {
        private int idVehiculo;
        private String marca;
        private String modelo;
        private String color;
        private String numeroChasis;
        private String numeroMotor;
        private boolean disponibleVenta;
        private double precioBase;

        public Vehiculo() {}

        // Getters y Setters
        public int getIdVehiculo() { return idVehiculo; }
        public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }
        
        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }
        
        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public String getNumeroChasis() { return numeroChasis; }
        public void setNumeroChasis(String numeroChasis) { this.numeroChasis = numeroChasis; }
        
        public String getNumeroMotor() { return numeroMotor; }
        public void setNumeroMotor(String numeroMotor) { this.numeroMotor = numeroMotor; }
        
        public boolean isDisponibleVenta() { return disponibleVenta; }
        public void setDisponibleVenta(boolean disponibleVenta) { this.disponibleVenta = disponibleVenta; }
        
        public double getPrecioBase() { return precioBase; }
        public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    }

    public static class GestorVehiculos {
        private static GestorVehiculos instancia;
        private HashMap<String, Vehiculo> vehiculos;

        private GestorVehiculos() {
            this.vehiculos = new HashMap<>();
        }

        public static GestorVehiculos getInstancia() {
            if (instancia == null) {
                instancia = new GestorVehiculos();
            }
            return instancia;
        }

        public void registrarVehiculo(Vehiculo vehiculo) throws DuplicateVehiculoException {
            if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
                throw new IllegalArgumentException("El vehículo o su número de chasis no pueden ser nulos o vacíos.");
            }
            if (this.vehiculos.containsKey(vehiculo.getNumeroChasis())) {
                throw new DuplicateVehiculoException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " ya existe.");
            }
            this.vehiculos.put(vehiculo.getNumeroChasis(), vehiculo);
        }

        public void actualizarDatosVehiculo(Vehiculo vehiculo) throws VehiculoNotFoundException {
            if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
                throw new IllegalArgumentException("El vehículo o su número de chasis no pueden ser nulos o vacíos para actualizar.");
            }
            if (!this.vehiculos.containsKey(vehiculo.getNumeroChasis())) {
                throw new VehiculoNotFoundException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " no encontrado para actualizar.");
            }
            this.vehiculos.put(vehiculo.getNumeroChasis(), vehiculo);
        }

        public void eliminarVehiculo(String numeroChasis) throws VehiculoNotFoundException {
            if (numeroChasis == null || numeroChasis.isEmpty()) {
                throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para eliminar.");
            }
            if (!this.vehiculos.containsKey(numeroChasis)) {
                throw new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado para eliminar.");
            }
            this.vehiculos.remove(numeroChasis);
        }

        public Vehiculo getVehiculo(String numeroChasis) throws VehiculoNotFoundException {
            if (numeroChasis == null || numeroChasis.isEmpty()) {
                throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para buscar.");
            }
            Vehiculo vehiculo = this.vehiculos.get(numeroChasis);
            if (vehiculo == null) {
                throw new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado.");
            }
            return vehiculo;
        }

        public Collection<Vehiculo> getTodosLosVehiculos() {
            return new ArrayList<>(this.vehiculos.values());
        }
    }

    public static class DuplicateVehiculoException extends Exception {
        public DuplicateVehiculoException(String message) {
            super(message);
        }
    }

    public static class VehiculoNotFoundException extends Exception {
        public VehiculoNotFoundException(String message) {
            super(message);
        }
    }

    // Clase para las requests
    public static class VehiculoRequest {
        public String marca;
        public String modelo;
        public String color;
        public String numeroChasis;
        public String numeroMotor;
        public Double precioBase;
        public Boolean disponibleVenta;
        
        public VehiculoRequest() {}
    }
} 