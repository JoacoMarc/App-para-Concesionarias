package com.grupo9.sistemaConcesionaria.model.state;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import org.springframework.stereotype.Component;

/**
 * Estado concreto: Entrega
 * Maneja la entrega final del vehículo al cliente
 */
@Component
public class EntregaState extends AbstractEstadoPedido {

    public EntregaState() {
        super("Entrega", "Entrega final del vehículo al cliente");
    }

    @Override
    protected void procesarEstadoEspecifico(Pedido pedido) throws Exception {
        logger.info("=== INICIANDO PROCESO DE ENTREGA ===");
        logger.info("Pedido: {} - Vehículo: {} {}", 
                   pedido.getNumeroDePedido(),
                   pedido.getVehiculo().getMarca(),
                   pedido.getVehiculo().getModelo());

        // 1. Coordinar llegada con el cliente
        coordinarLlegada(pedido);

        // 2. Realizar inspección pre-entrega
        realizarInspeccionPreEntrega(pedido);

        // 3. Preparar documentación de entrega
        prepararDocumentacionEntrega(pedido);

        // 4. Efectuar la entrega formal
        efectuarEntrega(pedido);

        // 5. Realizar capacitación básica del vehículo
        realizarCapacitacion(pedido);

        // 6. Obtener conformidad del cliente
        obtenerConformidad(pedido);

        // 7. Completar proceso de entrega
        completarProceso(pedido);

        // 8. Simular tiempo de procesamiento
        simularProcesamiento(3000);

        logger.info("=== ENTREGA COMPLETADA EXITOSAMENTE ===");
        agregarConfiguracion(pedido, "ENTREGA: Vehículo entregado satisfactoriamente - " + 
                           java.time.LocalDateTime.now());
    }

    /**
     * Coordina la llegada del vehículo con el cliente
     */
    private void coordinarLlegada(Pedido pedido) {
        logger.info("Coordinando llegada con el cliente...");

        String nombreCliente = pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido();
        
        // Simular contacto con el cliente
        agregarConfiguracion(pedido, "ENTREGA: Contacto establecido con " + nombreCliente);
        
        // Programar cita de entrega
        String fechaEntrega = java.time.LocalDateTime.now().plusHours(2).toString();
        agregarConfiguracion(pedido, "ENTREGA: Cita programada para " + fechaEntrega);
        
        // Confirmación de presencia
        agregarConfiguracion(pedido, "ENTREGA: Cliente confirmó disponibilidad");
        
        // Verificar dirección de entrega
        String direccion = pedido.getCliente().getDireccion();
        if (direccion == null) {
            direccion = pedido.getCliente().getDireccionFacturacion();
        }
        if (direccion == null) {
            direccion = "Dirección a confirmar";
        }
        
        agregarConfiguracion(pedido, "ENTREGA: Dirección confirmada - " + direccion);
        
        logger.debug("Llegada coordinada con {} en {}", nombreCliente, direccion);
    }

    /**
     * Realiza inspección del vehículo antes de la entrega
     */
    private void realizarInspeccionPreEntrega(Pedido pedido) {
        logger.info("Realizando inspección pre-entrega...");

        // Lista de verificación completa
        String[] verificaciones = {
            "Estado de la carrocería - Sin daños",
            "Funcionamiento del motor - Óptimo",
            "Sistema eléctrico - Funcional",
            "Neumáticos y ruedas - Estado perfecto",
            "Interior del vehículo - Limpio y completo",
            "Documentación - Completa y en orden",
            "Combustible - Tanque con nivel de entrega",
            "Accesorios incluidos - Presentes y funcionales"
        };

        for (String verificacion : verificaciones) {
            agregarConfiguracion(pedido, "ENTREGA: ✓ " + verificacion);
        }

        // Prueba de funcionamiento
        logger.debug("Realizando prueba de funcionamiento...");
        agregarConfiguracion(pedido, "ENTREGA: Prueba de funcionamiento completada exitosamente");

        // Fotografías del estado del vehículo
        agregarConfiguracion(pedido, "ENTREGA: Registro fotográfico del estado del vehículo completado");
    }

    /**
     * Prepara toda la documentación necesaria para la entrega
     */
    private void prepararDocumentacionEntrega(Pedido pedido) {
        logger.info("Preparando documentación de entrega...");

        String numeroEntrega = "ENT-" + System.currentTimeMillis();
        
        // Acta de entrega
        String actaEntrega = String.format(
            "ACTA DE ENTREGA N° %s\nFecha: %s\nPedido: %s\nCliente: %s %s\nDocumento: %s\nVehículo: %s %s\nChasis: %s\nPrecio Total: $%.2f",
            numeroEntrega,
            java.time.LocalDateTime.now(),
            pedido.getNumeroDePedido(),
            pedido.getCliente().getNombre(),
            pedido.getCliente().getApellido(),
            pedido.getCliente().getDocumento(),
            pedido.getVehiculo().getMarca(),
            pedido.getVehiculo().getModelo(),
            pedido.getVehiculo().getNumeroChasis(),
            pedido.getCostoTotal()
        );

        agregarConfiguracion(pedido, "ENTREGA: " + actaEntrega.replace("\n", " | "));

        // Documentos del vehículo
        agregarConfiguracion(pedido, "ENTREGA: Título de propiedad transferido");
        agregarConfiguracion(pedido, "ENTREGA: Cédula verde entregada");
        agregarConfiguracion(pedido, "ENTREGA: Manual del propietario entregado");
        agregarConfiguracion(pedido, "ENTREGA: Certificados de garantía entregados");

        // Llaves y elementos adicionales
        agregarConfiguracion(pedido, "ENTREGA: Juego completo de llaves entregado (2 unidades)");
        agregarConfiguracion(pedido, "ENTREGA: Kit de herramientas básicas entregado");
        
        logger.debug("Documentación preparada: Acta {}", numeroEntrega);
    }

    /**
     * Efectúa la entrega formal del vehículo
     */
    private void efectuarEntrega(Pedido pedido) {
        logger.info("Efectuando entrega formal...");

        String horaEntrega = java.time.LocalDateTime.now().toString();
        
        // Proceso de entrega
        agregarConfiguracion(pedido, "ENTREGA: Vehículo presentado al cliente - " + horaEntrega);
        agregarConfiguracion(pedido, "ENTREGA: Documentación firmada por ambas partes");
        agregarConfiguracion(pedido, "ENTREGA: Llaves entregadas oficialmente");
        
        // Verificación de identidad
        agregarConfiguracion(pedido, "ENTREGA: Identidad del cliente verificada");
        agregarConfiguracion(pedido, "ENTREGA: Documento DNI: " + pedido.getCliente().getDocumento() + " - Verificado");
        
        // Registro de entrega
        agregarConfiguracion(pedido, "ENTREGA: Registro de entrega completado");
        agregarConfiguracion(pedido, "ENTREGA: Fotografía cliente con vehículo tomada");
        
        logger.debug("Entrega formal completada en {}", horaEntrega);
    }

    /**
     * Realiza capacitación básica del vehículo al cliente
     */
    private void realizarCapacitacion(Pedido pedido) {
        logger.info("Realizando capacitación básica...");

        // Temas de capacitación
        String[] temasCapacitacion = {
            "Funcionamiento básico del vehículo",
            "Sistema de seguridad y alarmas",
            "Mantenimiento preventivo recomendado",
            "Ubicación de elementos de seguridad",
            "Uso del sistema de entretenimiento",
            "Garantías y servicios post-venta",
            "Contactos de servicio técnico"
        };

        for (String tema : temasCapacitacion) {
            agregarConfiguracion(pedido, "ENTREGA: Capacitación - " + tema);
        }

        // Entrega de material informativo
        agregarConfiguracion(pedido, "ENTREGA: Manual de usuario explicado");
        agregarConfiguracion(pedido, "ENTREGA: Tarjeta de contactos de emergencia entregada");
        agregarConfiguracion(pedido, "ENTREGA: Información de mantenimientos programados entregada");
        
        logger.debug("Capacitación básica completada");
    }

    /**
     * Obtiene la conformidad y satisfacción del cliente
     */
    private void obtenerConformidad(Pedido pedido) {
        logger.info("Obteniendo conformidad del cliente...");

        // Verificación de satisfacción
        agregarConfiguracion(pedido, "ENTREGA: Cliente expresó satisfacción con el vehículo");
        agregarConfiguracion(pedido, "ENTREGA: Cliente confirmó que el vehículo cumple expectativas");
        
        // Firma de conformidad
        agregarConfiguracion(pedido, "ENTREGA: Acta de conformidad firmada");
        agregarConfiguracion(pedido, "ENTREGA: Sin observaciones por parte del cliente");
        
        // Encuesta de satisfacción
        int puntuacion = (int) (Math.random() * 2) + 9; // Simular puntuación alta (9-10)
        agregarConfiguracion(pedido, "ENTREGA: Puntuación de satisfacción: " + puntuacion + "/10");
        
        // Información post-venta
        agregarConfiguracion(pedido, "ENTREGA: Información de servicios post-venta explicada");
        agregarConfiguracion(pedido, "ENTREGA: Cliente informado sobre próximos mantenimientos");
        
        logger.debug("Conformidad obtenida - Puntuación: {}/10", puntuacion);
    }

    /**
     * Completa todo el proceso de entrega
     */
    private void completarProceso(Pedido pedido) {
        logger.info("Completando proceso de entrega...");

        // Finalización administrativa
        agregarConfiguracion(pedido, "ENTREGA: Proceso administrativo completado");
        agregarConfiguracion(pedido, "ENTREGA: Expediente de venta cerrado");
        
        // Notificaciones internas
        agregarConfiguracion(pedido, "ENTREGA: Departamento de ventas notificado");
        agregarConfiguracion(pedido, "ENTREGA: Departamento de contabilidad notificado");
        agregarConfiguracion(pedido, "ENTREGA: Base de datos actualizada");
        
        // Activación de garantías
        agregarConfiguracion(pedido, "ENTREGA: Garantías del fabricante activadas");
        agregarConfiguracion(pedido, "ENTREGA: Garantías extendidas activadas");
        
        // Estado final
        agregarConfiguracion(pedido, "ENTREGA: PROCESO COMPLETADO EXITOSAMENTE");
        
        logger.debug("Proceso de entrega completado completamente");
    }

    @Override
    protected EstadoPedido mapearAEstadoEnum() {
        return EstadoPedido.ENTREGA;
    }

    @Override
    public boolean puedeAvanzar(Pedido pedido) {
        return pedido.getCliente() != null && 
               pedido.getCliente().getDocumento() != null &&
               !pedido.getCliente().getDocumento().isEmpty();
    }
} 