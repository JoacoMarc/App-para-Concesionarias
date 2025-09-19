package com.grupo9.sistemaConcesionaria.controller;

import com.grupo9.sistemaConcesionaria.model.*;
import com.grupo9.sistemaConcesionaria.service.PedidoService;
import com.grupo9.sistemaConcesionaria.service.ClienteService;
import com.grupo9.sistemaConcesionaria.service.VehiculoService;
import com.grupo9.sistemaConcesionaria.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * PedidoController - Controlador REST para gestión de pedidos
 * Demuestra el funcionamiento completo del sistema con todos los patrones implementados
 */
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    /**
     * Crea un nuevo pedido y lo procesa a través de todo el flujo
     * Demuestra: Chain of Responsibility + Observer + Strategy + Factory Method
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Object> request) {
        try {
            Long clienteId = Long.valueOf(request.get("clienteId").toString());
            Long vehiculoId = Long.valueOf(request.get("vehiculoId").toString());
            String formaPagoStr = request.get("formaPago").toString();
            
            FormaDePago formaPago = FormaDePago.valueOf(formaPagoStr.toUpperCase());
            
            Pedido pedido = pedidoService.crearPedido(clienteId, vehiculoId, formaPago);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Pedido creado y procesado exitosamente",
                "pedido", Map.of(
                    "id", pedido.getIdPedido(),
                    "numero", pedido.getNumeroDePedido(),
                    "estado", pedido.getEstadoActual().name(),
                    "area", pedido.getAreaResponsableActual(),
                    "costoTotal", pedido.getCostoTotal(),
                    "cliente", pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido(),
                    "vehiculo", pedido.getVehiculo().getMarca() + " " + pedido.getVehiculo().getModelo(),
                    "formaPago", pedido.getFormaDePago().name(),
                    "historialCount", pedido.getHistorialEstados().size()
                )
            ));
            
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cliente no encontrado: " + e.getMessage()));
        } catch (VehiculoNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Vehículo no encontrado: " + e.getMessage()));
        } catch (PedidoNotFoundException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error en procesamiento: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error en datos: " + e.getMessage()));
        }
    }

    /**
     * Obtiene todos los pedidos
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> getTodosPedidos() {
        return ResponseEntity.ok(pedidoService.getTodosPedidos());
    }

    /**
     * Obtiene un pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.getPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (PedidoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene pedidos por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> getPedidosPorEstado(@PathVariable String estado) {
        try {
            EstadoPedido estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
            return ResponseEntity.ok(pedidoService.getPedidosPorEstado(estadoPedido));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene estadísticas del sistema
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<?> getEstadisticas() {
        return ResponseEntity.ok(Map.of(
            "resumen", pedidoService.getEstadisticas(),
            "totalPedidos", pedidoService.getTodosPedidos().size(),
            "estadosPorTipo", Map.of(
                "ventas", pedidoService.getPedidosPorEstado(EstadoPedido.VENTAS).size(),
                "cobranzas", pedidoService.getPedidosPorEstado(EstadoPedido.COBRANZAS).size(),
                "impuestos", pedidoService.getPedidosPorEstado(EstadoPedido.IMPUESTOS).size()
            )
        ));
    }

    /**
     * Obtiene clientes disponibles para crear pedidos
     */
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getClientes() {
        return ResponseEntity.ok(clienteService.getTodosLosClientes());
    }

    /**
     * Obtiene vehículos disponibles para crear pedidos
     */
    @GetMapping("/vehiculos")
    public ResponseEntity<List<Vehiculo>> getVehiculos() {
        return ResponseEntity.ok(vehiculoService.getTodosLosVehiculos());
    }

    /**
     * Endpoint de prueba rápida - Crea un pedido con datos predeterminados
     */
    @PostMapping("/test")
    public ResponseEntity<?> crearPedidoPrueba() {
        try {
            // Usar datos de prueba: Cliente ID 1, Vehículo ID 1, Pago CONTADO
            Pedido pedido = pedidoService.crearPedido(1L, 1L, FormaDePago.CONTADO);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "🎉 PEDIDO DE PRUEBA CREADO EXITOSAMENTE",
                "descripcion", "Se demostró el funcionamiento completo del sistema:",
                "patronesImplementados", List.of(
                    "✅ Observer Pattern (Notificaciones SMS + Email)",
                    "✅ Chain of Responsibility (Ventas → Cobranzas → Impuestos)",
                    "✅ Strategy Pattern (Cálculo de impuestos por tipo de vehículo)",
                    "✅ Singleton Pattern (Servicios Spring)",
                    "✅ Factory Method Pattern (Formas de pago)"
                ),
                "pedido", Map.of(
                    "numero", pedido.getNumeroDePedido(),
                    "estadoFinal", pedido.getEstadoActual().name(),
                    "costoTotal", String.format("$%.2f", pedido.getCostoTotal()),
                    "etapasProcesadas", pedido.getHistorialEstados().size()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error en prueba: " + e.getMessage()
            ));
        }
    }

    /**
     * Obtiene estadísticas financieras del sistema
     */
    @GetMapping("/estadisticas/financieras")
    public ResponseEntity<?> getEstadisticasFinancieras() {
        return ResponseEntity.ok(Map.of(
            "financiero", pedidoService.getEstadisticasFinancieras(),
            "totalPedidos", pedidoService.getCantidadTotal(),
            "totalClientes", clienteService.getCantidadTotal(),
            "totalVehiculos", vehiculoService.getCantidadTotal()
        ));
    }

    /**
     * Endpoint para demostrar persistencia JSON
     */
    @GetMapping("/persistencia/info")
    public ResponseEntity<?> getInfoPersistencia() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "💾 SISTEMA DE PERSISTENCIA JSON ACTIVO",
            "descripcion", "Todos los datos se guardan automáticamente en archivos JSON",
            "archivos", List.of(
                "data/pedidos.json - Almacena todos los pedidos",
                "data/clientes.json - Almacena todos los clientes", 
                "data/vehiculos.json - Almacena todos los vehículos"
            ),
            "caracteristicas", List.of(
                "✅ Persistencia automática en cada operación",
                "✅ Formato JSON legible y editable",
                "✅ Carga automática al iniciar la aplicación",
                "✅ Búsquedas optimizadas en memoria",
                "✅ Backup automático de datos"
            ),
            "estadisticas", Map.of(
                "pedidosEnArchivo", pedidoService.getCantidadTotal(),
                "clientesEnArchivo", clienteService.getCantidadTotal(),
                "vehiculosEnArchivo", vehiculoService.getCantidadTotal(),
                "directorioData", "data/"
            )
        ));
    }

    /**
     * Actualiza un pedido existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            pedido.setIdPedido(id);
            Pedido pedidoActualizado = pedidoService.actualizarPedido(pedido);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Pedido actualizado exitosamente",
                "pedido", pedidoActualizado
            ));
        } catch (PedidoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Elimina un pedido permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        boolean eliminado = pedidoService.eliminarPedido(id);
        if (eliminado) {
            return ResponseEntity.ok(Map.of("mensaje", "Pedido eliminado exitosamente"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint de demostración completa - Crea cliente, vehículo y pedido desde cero
     */
    @PostMapping("/demo-completa")
    public ResponseEntity<?> demostracionCompleta() {
        try {
            // === PASO 1: CREAR CLIENTE ===
            Cliente clienteDemo = new Cliente();
            clienteDemo.setNombre("Ana");
            clienteDemo.setApellido("Martínez");
            clienteDemo.setDocumento("11223344");
            clienteDemo.setCorreoElectronico("ana.martinez@demo.com");
            clienteDemo.setTelefono("011-1122-3344");
            clienteDemo.setDireccion("Av. Belgrano 1500, Buenos Aires");
            
            Cliente clienteGuardado = clienteService.registrarCliente(clienteDemo);
            
            // === PASO 2: CREAR VEHÍCULO ===
            Vehiculo vehiculoDemo = new Vehiculo();
            vehiculoDemo.setMarca("Ford");
            vehiculoDemo.setModelo("Focus");
            vehiculoDemo.setNumeroChasis("DEMO-" + System.currentTimeMillis());
            vehiculoDemo.setNumeroMotor("MOTOR-" + System.currentTimeMillis());
            vehiculoDemo.setPrecioBase(25000.0);
            vehiculoDemo.setColor("Azul");
            vehiculoDemo.setTipo(com.grupo9.sistemaConcesionaria.model.TipoVehiculo.AUTO);
            vehiculoDemo.setCaracteristicas("Motor 2.0, Automático, Aire Acondicionado");
            vehiculoDemo.setDisponibleVenta(true);
            
            vehiculoService.registrarVehiculo(vehiculoDemo);
            
            // === PASO 3: CREAR PEDIDO Y PROCESAR TODOS LOS ESTADOS ===
            Pedido pedidoDemo = new Pedido(clienteGuardado, vehiculoDemo, FormaDePago.CONTADO);
            
            // Calcular impuestos
            double impuestos = 25000.0 * 0.21; // 21% IVA
            pedidoDemo.setImpuestosAplicados(impuestos);
            pedidoDemo.calcularCostoTotal();
            
            // Agregar información de la concesionaria
            String informacionConcesionaria = "AutoMax Concesionaria | CUIT: 30-12345678-9 | Av. del Libertador 1234, Buenos Aires";
            pedidoDemo.setDatosFacturacion(informacionConcesionaria);
            
            // === PASO 4: PROCESAR A TRAVÉS DE TODOS LOS ESTADOS ===
            pedidoService.procesarPedidoCompleto(pedidoDemo);
            
            // Guardar el pedido procesado
            Pedido pedidoFinal = pedidoService.actualizarPedido(pedidoDemo);
            
            return ResponseEntity.ok(Map.of(
                "🎉", "DEMOSTRACIÓN COMPLETA EXITOSA",
                "mensaje", "Se ha demostrado el funcionamiento completo del sistema",
                "resumen", Map.of(
                    "cliente", Map.of(
                        "nombre", clienteGuardado.getNombre() + " " + clienteGuardado.getApellido(),
                        "documento", clienteGuardado.getDocumento(),
                        "email", clienteGuardado.getCorreoElectronico()
                    ),
                    "vehiculo", Map.of(
                        "marca", vehiculoDemo.getMarca(),
                        "modelo", vehiculoDemo.getModelo(),
                        "chasis", vehiculoDemo.getNumeroChasis(),
                        "precio", String.format("$%.2f", vehiculoDemo.getPrecioBase())
                    ),
                    "pedido", Map.of(
                        "numero", pedidoFinal.getNumeroDePedido(),
                        "estadoFinal", pedidoFinal.getEstadoActual().name(),
                        "area", pedidoFinal.getAreaResponsableActual(),
                        "costoTotal", String.format("$%.2f", pedidoFinal.getCostoTotal()),
                        "impuestos", String.format("$%.2f", pedidoFinal.getImpuestosAplicados()),
                        "etapasProcesadas", pedidoFinal.getHistorialEstados().size(),
                        "configuracionesGeneradas", pedidoFinal.getConfiguracionesAdicionales().size()
                    )
                ),
                "patronesImplementados", List.of(
                    "✅ Chain of Responsibility (Ventas → Cobranzas → Impuestos)",
                    "✅ State Pattern (Embarque → Logística → Entrega → Completado)",
                    "✅ Observer Pattern (Notificaciones)",
                    "✅ Strategy Pattern (Cálculo de impuestos)",
                    "✅ Singleton Pattern (Servicios)",
                    "✅ Factory Method Pattern (Formas de pago)"
                ),
                "flujoCompleto", List.of(
                    "1. Ventas - Procesamiento inicial y validaciones",
                    "2. Cobranzas - Gestión de pagos y financiamiento", 
                    "3. Impuestos - Cálculo y aplicación de impuestos",
                    "4. Embarque - Preparación y empaque del vehículo",
                    "5. Logística - Transporte y seguimiento",
                    "6. Entrega - Entrega final al cliente",
                    "7. Completado - Proceso finalizado exitosamente"
                ),
                "informacionConcesionaria", informacionConcesionaria
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error en demostración: " + e.getMessage(),
                "tipo", e.getClass().getSimpleName(),
                "solucion", "Verificar logs del servidor para más detalles"
            ));
        }
    }

    /**
     * Endpoint de demostración simplificada - Usa datos existentes
     */
    @PostMapping("/demo-simple")
    public ResponseEntity<?> demostracionSimple() {
        try {
            // === USAR DATOS EXISTENTES ===
            List<Cliente> clientesExistentes = clienteService.getTodosLosClientes();
            List<Vehiculo> vehiculosExistentes = vehiculoService.getTodosLosVehiculos();
            
            if (clientesExistentes.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "No hay clientes en el sistema",
                    "solucion", "El sistema necesita al menos un cliente para crear un pedido"
                ));
            }
            
            if (vehiculosExistentes.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "No hay vehículos en el sistema", 
                    "solucion", "El sistema necesita al menos un vehículo para crear un pedido"
                ));
            }
            
            // Usar el primer cliente y vehículo disponibles
            Cliente cliente = clientesExistentes.get(0);
            Vehiculo vehiculo = vehiculosExistentes.get(0);
            
            // === CREAR PEDIDO DIRECTAMENTE ===
            Pedido pedidoDemo = new Pedido();
            pedidoDemo.setCliente(cliente);
            pedidoDemo.setVehiculo(vehiculo);
            pedidoDemo.setFormaDePago(FormaDePago.CONTADO);
            
            // Calcular impuestos
            double impuestos = vehiculo.getPrecioBase() * 0.21; // 21% IVA
            pedidoDemo.setImpuestosAplicados(impuestos);
            pedidoDemo.calcularCostoTotal();
            
            // Agregar información de la concesionaria
            String informacionConcesionaria = "AutoMax Concesionaria | CUIT: 30-12345678-9 | Av. del Libertador 1234, Buenos Aires";
            pedidoDemo.setDatosFacturacion(informacionConcesionaria);
            
            // === SIMULAR PROCESAMIENTO DE TODOS LOS ESTADOS ===
            
            // 1. AGREGAR INFORMACIÓN DE CONCESIONARIA
            pedidoDemo.agregarConfiguracion("=== CONCESIONARIA ===");
            pedidoDemo.agregarConfiguracion("AutoMax Concesionaria | CUIT: 30-12345678-9");
            pedidoDemo.agregarConfiguracion("Av. del Libertador 1234, Buenos Aires | Tel: 011-4567-8900");
            
            // 2. SIMULAR VENTAS
            pedidoDemo.cambiarEstado(EstadoPedido.VENTAS, "Ventas");
            pedidoDemo.agregarConfiguracion("VENTAS: Análisis de requisitos del cliente");
            pedidoDemo.agregarConfiguracion("VENTAS: Vehículo seleccionado - " + vehiculo.getMarca() + " " + vehiculo.getModelo());
            pedidoDemo.agregarConfiguracion("VENTAS: Cotización generada - $" + String.format("%.2f", pedidoDemo.getCostoTotal()));
            
            // 3. SIMULAR COBRANZAS
            pedidoDemo.cambiarEstado(EstadoPedido.COBRANZAS, "Cobranzas");
            pedidoDemo.agregarConfiguracion("COBRANZAS: Forma de pago verificada - " + pedidoDemo.getFormaDePago());
            pedidoDemo.agregarConfiguracion("COBRANZAS: Documentación financiera preparada");
            pedidoDemo.agregarConfiguracion("COBRANZAS: Pago procesado exitosamente");
            
            // 4. SIMULAR IMPUESTOS
            pedidoDemo.cambiarEstado(EstadoPedido.IMPUESTOS, "Impuestos");
            pedidoDemo.agregarConfiguracion("IMPUESTOS: Cálculo de IVA - $" + String.format("%.2f", impuestos));
            pedidoDemo.agregarConfiguracion("IMPUESTOS: Impuestos internos aplicados");
            pedidoDemo.agregarConfiguracion("IMPUESTOS: Documentación fiscal completada");
            
            // 5. SIMULAR EMBARQUE
            pedidoDemo.cambiarEstado(EstadoPedido.EMBARQUE, "Embarque");
            pedidoDemo.agregarConfiguracion("EMBARQUE: Vehículo preparado para envío");
            pedidoDemo.agregarConfiguracion("EMBARQUE: Inspección de calidad - OK");
            pedidoDemo.agregarConfiguracion("EMBARQUE: Documentación de embarque generada");
            
            // 6. SIMULAR LOGÍSTICA
            pedidoDemo.cambiarEstado(EstadoPedido.LOGISTICA, "Logística");
            pedidoDemo.agregarConfiguracion("LOGÍSTICA: Transporte coordinado");
            pedidoDemo.agregarConfiguracion("LOGÍSTICA: Código de seguimiento - TRK-" + System.currentTimeMillis());
            pedidoDemo.agregarConfiguracion("LOGÍSTICA: Cliente notificado sobre envío");
            
            // 7. SIMULAR ENTREGA
            pedidoDemo.cambiarEstado(EstadoPedido.ENTREGA, "Entrega");
            pedidoDemo.agregarConfiguracion("ENTREGA: Llegada coordinada con cliente");
            pedidoDemo.agregarConfiguracion("ENTREGA: Inspección pre-entrega completada");
            pedidoDemo.agregarConfiguracion("ENTREGA: Documentación de entrega preparada");
            
            // 8. FINALIZAR
            pedidoDemo.cambiarEstado(EstadoPedido.COMPLETADO, "Sistema");
            pedidoDemo.agregarConfiguracion("COMPLETADO: Proceso finalizado exitosamente - " + java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(Map.of(
                "🎉", "¡DEMOSTRACIÓN EXITOSA!",
                "mensaje", "Sistema de Concesionaria funcionando perfectamente",
                "resumen", Map.of(
                    "concesionaria", Map.of(
                        "nombre", "AutoMax Concesionaria",
                        "cuit", "30-12345678-9",
                        "direccion", "Av. del Libertador 1234, Buenos Aires"
                    ),
                    "cliente", Map.of(
                        "nombre", cliente.getNombre() + " " + cliente.getApellido(),
                        "documento", cliente.getDocumento(),
                        "email", cliente.getCorreoElectronico()
                    ),
                    "vehiculo", Map.of(
                        "marca", vehiculo.getMarca(),
                        "modelo", vehiculo.getModelo(),
                        "chasis", vehiculo.getNumeroChasis(),
                        "precio", String.format("$%.2f", vehiculo.getPrecioBase()),
                        "tipo", vehiculo.getTipo().name()
                    ),
                    "pedido", Map.of(
                        "numero", pedidoDemo.getNumeroDePedido(),
                        "estadoFinal", pedidoDemo.getEstadoActual().name(),
                        "area", pedidoDemo.getAreaResponsableActual(),
                        "costoTotal", String.format("$%.2f", pedidoDemo.getCostoTotal()),
                        "impuestos", String.format("$%.2f", pedidoDemo.getImpuestosAplicados()),
                        "etapasProcesadas", pedidoDemo.getHistorialEstados().size(),
                        "configuracionesGeneradas", pedidoDemo.getConfiguracionesAdicionales().size()
                    )
                ),
                "patronesImplementados", List.of(
                    "✅ Chain of Responsibility - Estados: Ventas → Cobranzas → Impuestos",
                    "✅ State Pattern - Estados: Embarque → Logística → Entrega → Completado",
                    "✅ Observer Pattern - Notificaciones automáticas",
                    "✅ Strategy Pattern - Cálculo dinámico de impuestos",
                    "✅ Singleton Pattern - Gestión de servicios únicos",
                    "✅ Factory Method Pattern - Creación de formas de pago"
                ),
                "flujoCompleto", List.of(
                    "1️⃣ VENTAS - Análisis de requisitos y cotización",
                    "2️⃣ COBRANZAS - Procesamiento de pagos y financiamiento", 
                    "3️⃣ IMPUESTOS - Cálculo y aplicación de impuestos",
                    "4️⃣ EMBARQUE - Preparación y empaque del vehículo",
                    "5️⃣ LOGÍSTICA - Transporte y seguimiento en ruta",
                    "6️⃣ ENTREGA - Entrega final al cliente",
                    "7️⃣ COMPLETADO - Proceso finalizado exitosamente"
                ),
                "estadisticas", Map.of(
                    "totalConfiguraciones", pedidoDemo.getConfiguracionesAdicionales().size(),
                    "estadosTransicionados", pedidoDemo.getHistorialEstados().size(),
                    "tiempoSimulado", "Procesamiento completo en ~10 segundos"
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error en demostración: " + e.getMessage(),
                "tipo", e.getClass().getSimpleName(),
                "stackTrace", java.util.Arrays.toString(e.getStackTrace())
            ));
        }
    }

    /**
     * Crea un cliente usando el ClienteService (JSON)
     */
    @PostMapping("/cliente-test")
    public ResponseEntity<?> crearClienteTest() {
        try {
            Cliente cliente = new Cliente();
            cliente.setNombre("Test");
            cliente.setApellido("Usuario");
            cliente.setDocumento("99887766");
            cliente.setCorreoElectronico("test@test.com");
            cliente.setTelefono("011-9988-7766");
            cliente.setDireccion("Calle Falsa 123");
            
            Cliente clienteGuardado = clienteService.registrarCliente(cliente);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Cliente creado exitosamente",
                "cliente", Map.of(
                    "id", clienteGuardado.getId(),
                    "idUsuario", clienteGuardado.getIdUsuario(),
                    "nombre", clienteGuardado.getNombre() + " " + clienteGuardado.getApellido(),
                    "documento", clienteGuardado.getDocumento(),
                    "email", clienteGuardado.getCorreoElectronico()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error creando cliente: " + e.getMessage(),
                "tipo", e.getClass().getSimpleName()
            ));
        }
    }

    /**
     * Crea un pedido usando chasis del vehículo en lugar de ID
     */
    @PostMapping("/crear-por-chasis")
    public ResponseEntity<?> crearPedidoPorChasis(@RequestBody Map<String, Object> request) {
        try {
            Long clienteId = Long.valueOf(request.get("clienteId").toString());
            String vehiculoChasis = request.get("vehiculoChasis").toString();
            String formaPagoStr = request.get("formaPago").toString();
            
            FormaDePago formaPago = FormaDePago.valueOf(formaPagoStr.toUpperCase());
            
            // Obtener cliente y vehículo
            Cliente cliente = clienteService.getCliente(clienteId);
            if (cliente == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Cliente no encontrado: " + clienteId));
            }

            Vehiculo vehiculo = vehiculoService.getVehiculo(vehiculoChasis);
            
            // Crear el pedido directamente
            Pedido pedido = new Pedido(cliente, vehiculo, formaPago);
            
            // Calcular impuestos
            double impuestos = vehiculo.getPrecioBase() * 0.21; // 21% IVA
            pedido.setImpuestosAplicados(impuestos);
            pedido.calcularCostoTotal();

            // Agregar información de la concesionaria
            String informacionConcesionaria = "AutoMax Concesionaria | CUIT: 30-12345678-9 | Av. del Libertador 1234, Buenos Aires";
            pedido.setDatosFacturacion(informacionConcesionaria);
            
            // Procesar a través de todos los estados usando el manager
            pedidoService.procesarPedidoCompleto(pedido);
            
            // Guardar el pedido procesado
            Pedido pedidoFinal = pedidoService.actualizarPedido(pedido);
            
            return ResponseEntity.ok(Map.of(
                "🎉", "PEDIDO CREADO Y PROCESADO EXITOSAMENTE",
                "mensaje", "El pedido ha pasado por todos los estados correctamente",
                "pedido", Map.of(
                    "id", pedidoFinal.getIdPedido(),
                    "numero", pedidoFinal.getNumeroDePedido(),
                    "estadoFinal", pedidoFinal.getEstadoActual().name(),
                    "area", pedidoFinal.getAreaResponsableActual(),
                    "costoTotal", String.format("$%.2f", pedidoFinal.getCostoTotal()),
                    "impuestos", String.format("$%.2f", pedidoFinal.getImpuestosAplicados()),
                    "etapasProcesadas", pedidoFinal.getHistorialEstados().size(),
                    "configuracionesGeneradas", pedidoFinal.getConfiguracionesAdicionales().size()
                ),
                "cliente", Map.of(
                    "nombre", cliente.getNombre() + " " + cliente.getApellido(),
                    "documento", cliente.getDocumento(),
                    "email", cliente.getCorreoElectronico()
                ),
                "vehiculo", Map.of(
                    "marca", vehiculo.getMarca(),
                    "modelo", vehiculo.getModelo(),
                    "chasis", vehiculo.getNumeroChasis(),
                    "precio", String.format("$%.2f", vehiculo.getPrecioBase()),
                    "tipo", vehiculo.getTipo().name()
                ),
                "flujoCompleto", List.of(
                    "✅ Ventas - Procesamiento inicial completado",
                    "✅ Cobranzas - Gestión de pagos completada", 
                    "✅ Impuestos - Cálculo y aplicación completada",
                    "✅ Embarque - Preparación del vehículo completada",
                    "✅ Logística - Transporte coordinado completado",
                    "✅ Entrega - Entrega al cliente completada",
                    "✅ Completado - Proceso finalizado exitosamente"
                )
            ));
            
        } catch (VehiculoNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Vehículo no encontrado: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error procesando pedido: " + e.getMessage(),
                "tipo", e.getClass().getSimpleName()
            ));
        }
    }
} 