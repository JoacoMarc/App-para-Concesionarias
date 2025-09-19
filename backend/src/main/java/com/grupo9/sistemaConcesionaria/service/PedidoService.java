package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.Cliente;
import com.grupo9.sistemaConcesionaria.model.Vehiculo;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.model.FormaDePago;
import com.grupo9.sistemaConcesionaria.repository.PedidoJsonRepository;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;
import com.grupo9.sistemaConcesionaria.exception.ClienteNotFoundException;
import com.grupo9.sistemaConcesionaria.exception.VehiculoNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Servicio para manejar operaciones CRUD de pedidos
 * Integra Chain of Responsibility y State Pattern para el procesamiento completo
 */
@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    
    @Autowired
    private PedidoJsonRepository pedidoRepository;
    
    // Inyección de dependencias
    @Autowired
    private VehiculoService vehiculoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ImpuestoService impuestoService;
    
    @Autowired
    private NotificacionService notificacionService;

    // ===== INTEGRACIÓN CON INFORMACIÓN CONCESIONARIA =====
    @Autowired
    private InformacionConcesionariaService informacionConcesionariaService;
    
    // ===== NUEVO MANAGER DE ESTADOS =====
    @Autowired
    private EstadoPedidoManager estadoPedidoManager;

    // Handlers originales para compatibilidad
    @Autowired
    private VentasHandler ventasHandler;
    
    @Autowired
    private CobranzasHandler cobranzasHandler;
    
    @Autowired
    private ImpuestosHandler impuestosHandler;

    /**
     * Configuración inicial del servicio
     */
    @jakarta.annotation.PostConstruct
    public void configurarServicio() {
        // Configurar la cadena de responsabilidad original (para compatibilidad)
        ventasHandler
            .setSiguiente(cobranzasHandler)
            .setSiguiente(impuestosHandler);
        
        logger.info("Cadena de responsabilidad configurada: Ventas → Cobranzas → Impuestos");
        logger.info("Sistema de persistencia JSON inicializado - Pedidos existentes: {}", pedidoRepository.count());
    }

    /**
     * Crea un nuevo pedido y lo procesa a través del sistema completo (Chain + State)
     * @param clienteId ID del cliente
     * @param vehiculoId ID del vehículo
     * @param formaDePago Forma de pago seleccionada
     * @return El pedido creado y procesado
     */
    public Pedido crearPedido(Long clienteId, Long vehiculoId, FormaDePago formaDePago) 
            throws ClienteNotFoundException, VehiculoNotFoundException, PedidoNotFoundException {
        
        logger.info("Creando nuevo pedido - Cliente: {}, Vehículo: {}, Pago: {}", 
                   clienteId, vehiculoId, formaDePago);

        // Obtener cliente y vehículo
        Cliente cliente = clienteService.getCliente(clienteId);
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente no encontrado: " + clienteId);
        }

        Vehiculo vehiculo = vehiculoService.getVehiculoPorId(vehiculoId);
        if (vehiculo == null) {
            throw new VehiculoNotFoundException("Vehículo no encontrado: " + vehiculoId);
        }

        // Crear el pedido
        Pedido pedido = new Pedido(cliente, vehiculo, formaDePago);
        
        // Calcular impuestos iniciales usando la nueva API
        double impuestos = impuestoService.calcularTotalImpuestos(vehiculo.getTipo(), vehiculo.getPrecioBase());
        pedido.setImpuestosAplicados(impuestos);
        pedido.calcularCostoTotal();

        // Agregar información de la concesionaria
        String informacionConcesionaria = informacionConcesionariaService.getInformacionCompleta();
        pedido.setDatosFacturacion(informacionConcesionaria);
        
        // Guardar en JSON
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        logger.info("Pedido creado: {} - Costo total: ${:.2f}", 
                   pedidoGuardado.getNumeroDePedido(), pedidoGuardado.getCostoTotal());

        // ===== PROCESAMIENTO COMPLETO CON NUEVO MANAGER =====
        procesarPedidoCompleto(pedidoGuardado);

        // Guardar el pedido procesado
        return pedidoRepository.save(pedidoGuardado);
    }

    /**
     * Procesa un pedido a través del sistema completo usando el EstadoPedidoManager
     * @param pedido El pedido a procesar
     */
    public void procesarPedidoCompleto(Pedido pedido) throws PedidoNotFoundException {
        logger.info("=== INICIANDO PROCESAMIENTO COMPLETO ===");
        logger.info("Pedido: {} | Cliente: {} {} | Vehículo: {} {}", 
                   pedido.getNumeroDePedido(),
                   pedido.getCliente().getNombre(),
                   pedido.getCliente().getApellido(),
                   pedido.getVehiculo().getMarca(),
                   pedido.getVehiculo().getModelo());

        // Agregar información de la concesionaria al inicio
        String encabezadoConcesionaria = informacionConcesionariaService.getEncabezadoFactura();
        pedido.agregarConfiguracion("=== CONCESIONARIA ===");
        pedido.agregarConfiguracion(encabezadoConcesionaria.replace("\n", " | "));
        pedido.agregarConfiguracion("=== INICIO PROCESAMIENTO ===");
        
        try {
            // Usar el nuevo manager que coordina Chain of Responsibility + State Pattern
            estadoPedidoManager.procesarPedidoCompleto(pedido);
            
            logger.info("=== PROCESAMIENTO COMPLETO FINALIZADO ===");
            
        } catch (Exception e) {
            logger.error("Error en procesamiento completo de pedido {}: {}", 
                        pedido.getNumeroDePedido(), e.getMessage());
            
            // Agregar información del error
            pedido.agregarConfiguracion("ERROR: " + e.getMessage() + " - " + java.time.LocalDateTime.now());
            throw e;
        }
    }

    /**
     * Procesa un estado específico de un pedido existente
     * @param pedidoId ID del pedido
     * @param estado Estado específico a procesar
     */
    public void procesarEstadoEspecifico(Long pedidoId, EstadoPedido estado) throws Exception {
        Pedido pedido = getPedido(pedidoId);
        
        logger.info("Procesando estado específico {} para pedido {}", 
                   estado, pedido.getNumeroDePedido());
        
        estadoPedidoManager.procesarEstadoEspecifico(pedido, estado);
        
        // Guardar cambios
        pedidoRepository.save(pedido);
        
        logger.info("Estado {} procesado para pedido {}", estado, pedido.getNumeroDePedido());
    }

    /**
     * Obtiene un pedido por ID
     * @param id ID del pedido
     * @return El pedido encontrado
     */
    public Pedido getPedido(Long id) throws PedidoNotFoundException {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado: " + id));
    }

    /**
     * Obtiene un pedido por número
     * @param numeroPedido Número del pedido
     * @return El pedido encontrado
     */
    public Pedido getPedidoPorNumero(String numeroPedido) throws PedidoNotFoundException {
        return pedidoRepository.findByNumeroDePedido(numeroPedido)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado: " + numeroPedido));
    }

    /**
     * Obtiene todos los pedidos
     * @return Lista de todos los pedidos
     */
    public List<Pedido> getTodosPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Obtiene pedidos por estado
     * @param estado Estado del pedido
     * @return Lista de pedidos en el estado especificado
     */
    public List<Pedido> getPedidosPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoActual(estado);
    }

    /**
     * Obtiene pedidos por cliente
     * @param clienteId ID del cliente
     * @return Lista de pedidos del cliente
     */
    public List<Pedido> getPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    /**
     * Obtiene pedidos por vehículo
     * @param vehiculoChasis Número de chasis del vehículo
     * @return Lista de pedidos para el vehículo
     */
    public List<Pedido> getPedidosPorVehiculo(String vehiculoChasis) {
        return pedidoRepository.findByVehiculoChasis(vehiculoChasis);
    }

    /**
     * Obtiene pedidos por forma de pago
     * @param formaPago Forma de pago
     * @return Lista de pedidos con la forma de pago especificada
     */
    public List<Pedido> getPedidosPorFormaPago(FormaDePago formaPago) {
        return pedidoRepository.findByFormaDePago(formaPago);
    }

    /**
     * Actualiza un pedido existente
     * @param pedido Pedido a actualizar
     * @return Pedido actualizado
     */
    public Pedido actualizarPedido(Pedido pedido) throws PedidoNotFoundException {
        if (pedido.getIdPedido() == null || !pedidoRepository.existsById(pedido.getIdPedido())) {
            throw new PedidoNotFoundException("Pedido no existe para actualizar: " + pedido.getIdPedido());
        }
        
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        logger.info("Pedido actualizado: {}", pedidoActualizado.getNumeroDePedido());
        return pedidoActualizado;
    }

    /**
     * Cancela un pedido
     * @param id ID del pedido a cancelar
     */
    public void cancelarPedido(Long id) throws PedidoNotFoundException {
        Pedido pedido = getPedido(id);
        pedido.cambiarEstado(EstadoPedido.CANCELADO, "Sistema");
        
        pedidoRepository.save(pedido);
        
        // Notificar cancelación
        notificacionService.notificarCambioPedido(
            pedido.getIdPedido(), 
            "CANCELADO", 
            "Sistema"
        );
        
        logger.info("Pedido cancelado: {}", pedido.getNumeroDePedido());
    }

    /**
     * Elimina un pedido permanentemente
     * @param id ID del pedido a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarPedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            logger.info("Pedido eliminado permanentemente: {}", id);
            return true;
        }
        return false;
    }

    /**
     * Genera reporte completo de un pedido incluyendo información de la concesionaria
     * @param pedidoId ID del pedido
     * @return Reporte completo
     */
    public String generarReportePedido(Long pedidoId) throws PedidoNotFoundException {
        Pedido pedido = getPedido(pedidoId);
        
        StringBuilder reporte = new StringBuilder();
        
        // Encabezado con información de la concesionaria
        reporte.append("=".repeat(60)).append("\n");
        reporte.append(informacionConcesionariaService.getEncabezadoFactura()).append("\n");
        reporte.append(informacionConcesionariaService.getDatosContacto()).append("\n");
        reporte.append("=".repeat(60)).append("\n\n");
        
        // Información del pedido
        reporte.append("REPORTE DE PEDIDO\n");
        reporte.append("Número: ").append(pedido.getNumeroDePedido()).append("\n");
        reporte.append("Fecha: ").append(pedido.getFechaCreacion()).append("\n");
        reporte.append("Estado: ").append(pedido.getEstadoActual().getDescripcion()).append("\n");
        
        // Información del cliente
        reporte.append("\nCLIENTE:\n");
        reporte.append("Nombre: ").append(pedido.getCliente().getNombre()).append(" ");
        reporte.append(pedido.getCliente().getApellido()).append("\n");
        reporte.append("Documento: ").append(pedido.getCliente().getDocumento()).append("\n");
        reporte.append("Email: ").append(pedido.getCliente().getMail()).append("\n");
        
        // Información del vehículo
        reporte.append("\nVEHÍCULO:\n");
        reporte.append("Marca: ").append(pedido.getVehiculo().getMarca()).append("\n");
        reporte.append("Modelo: ").append(pedido.getVehiculo().getModelo()).append("\n");
        reporte.append("Chasis: ").append(pedido.getVehiculo().getNumeroChasis()).append("\n");
        reporte.append("Precio Base: $").append(String.format("%.2f", pedido.getVehiculo().getPrecioBase())).append("\n");
        
        // Información financiera
        reporte.append("\nDETALLE FINANCIERO:\n");
        reporte.append("Impuestos: $").append(String.format("%.2f", pedido.getImpuestosAplicados())).append("\n");
        reporte.append("TOTAL: $").append(String.format("%.2f", pedido.getCostoTotal())).append("\n");
        
        // Historial de estados
        reporte.append("\nHISTORIAL DE ESTADOS:\n");
        pedido.getHistorialEstados().forEach(historial -> {
            reporte.append("- ").append(historial.getFecha()).append(" | ");
            reporte.append(historial.getEstado().getDescripcion()).append(" | ");
            reporte.append(historial.getAreaResponsable()).append("\n");
        });
        
        // Configuraciones adicionales
        if (!pedido.getConfiguracionesAdicionales().isEmpty()) {
            reporte.append("\nDETALLE DE PROCESAMIENTO:\n");
            pedido.getConfiguracionesAdicionales().forEach(config -> {
                reporte.append("• ").append(config).append("\n");
            });
        }
        
        reporte.append("\n").append("=".repeat(60));
        
        return reporte.toString();
    }

    /**
     * Obtiene estadísticas de pedidos incluyendo información de la concesionaria
     * @return Información de estadísticas
     */
    public String getEstadisticas() {
        StringBuilder stats = new StringBuilder();
        
        // Información de la concesionaria
        stats.append("=== ").append(informacionConcesionariaService.getNombre()).append(" ===\n");
        stats.append("CUIT: ").append(informacionConcesionariaService.getCuit()).append("\n\n");
        
        // Estadísticas por estado
        long totalPedidos = pedidoRepository.count();
        long pedidosVentas = pedidoRepository.countByEstadoActual(EstadoPedido.VENTAS);
        long pedidosCobranzas = pedidoRepository.countByEstadoActual(EstadoPedido.COBRANZAS);
        long pedidosImpuestos = pedidoRepository.countByEstadoActual(EstadoPedido.IMPUESTOS);
        long pedidosEmbarque = pedidoRepository.countByEstadoActual(EstadoPedido.EMBARQUE);
        long pedidosLogistica = pedidoRepository.countByEstadoActual(EstadoPedido.LOGISTICA);
        long pedidosEntrega = pedidoRepository.countByEstadoActual(EstadoPedido.ENTREGA);
        long pedidosCompletados = pedidoRepository.countByEstadoActual(EstadoPedido.COMPLETADO);
        
        stats.append("ESTADÍSTICAS DE PEDIDOS:\n");
        stats.append("Total: ").append(totalPedidos).append("\n");
        stats.append("Ventas: ").append(pedidosVentas).append("\n");
        stats.append("Cobranzas: ").append(pedidosCobranzas).append("\n");
        stats.append("Impuestos: ").append(pedidosImpuestos).append("\n");
        stats.append("Embarque: ").append(pedidosEmbarque).append("\n");
        stats.append("Logística: ").append(pedidosLogistica).append("\n");
        stats.append("Entrega: ").append(pedidosEntrega).append("\n");
        stats.append("Completados: ").append(pedidosCompletados).append("\n");
        
        return stats.toString();
    }

    /**
     * Obtiene estadísticas financieras
     * @return Información financiera
     */
    public String getEstadisticasFinancieras() {
        double totalVentas = pedidoRepository.sumCostoTotal();
        double ventasCompletadas = pedidoRepository.sumCostoTotalByEstado(EstadoPedido.COMPLETADO);
        
        return String.format(
            "Estadísticas Financieras - %s\nTotal Ventas: $%.2f | Completadas: $%.2f | Promedio: $%.2f",
            informacionConcesionariaService.getNombre(),
            totalVentas, 
            ventasCompletadas, 
            pedidoRepository.count() > 0 ? totalVentas / pedidoRepository.count() : 0.0
        );
    }

    /**
     * Obtiene la cantidad total de pedidos
     * @return Número total de pedidos
     */
    public long getCantidadTotal() {
        return pedidoRepository.count();
    }
} 