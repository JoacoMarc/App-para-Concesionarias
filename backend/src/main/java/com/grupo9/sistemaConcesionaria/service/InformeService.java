package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.TipoVehiculo;
import com.grupo9.sistemaConcesionaria.strategy.DetalleImpuestos;
import com.grupo9.sistemaConcesionaria.payment.FormaPagoBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * InformeService - Servicio para generación de informes del sistema
 * 
 * Genera informes con totales de precios con/sin impuestos y formas de pago
 * según los requerimientos del sistema.
 */
@Service
public class InformeService {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ImpuestoService impuestoService;
    
    @Autowired
    private InformacionConcesionariaService concesionariaService;
    
    /**
     * Genera un informe completo de ventas con desglose de precios e impuestos
     */
    public String generarInformeVentas() {
        List<Pedido> pedidos = pedidoService.getTodosPedidos();
        
        StringBuilder informe = new StringBuilder();
        
        // Encabezado del informe
        informe.append(generarEncabezadoInforme("INFORME DE VENTAS"));
        
        // Estadísticas generales
        double totalVentasSinImpuestos = 0;
        double totalImpuestos = 0;
        double totalVentasConImpuestos = 0;
        Map<String, Long> ventasPorFormaPago = pedidos.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getFormaDePago().name(),
                    Collectors.counting()
                ));
        
        // Desglose por pedido
        informe.append("\n=== DESGLOSE POR PEDIDO ===\n");
        for (Pedido pedido : pedidos) {
            DetalleImpuestos detalle = impuestoService.calcularImpuestos(
                pedido.getVehiculo().getTipo(), 
                pedido.getVehiculo().getPrecioBase()
            );
            
            totalVentasSinImpuestos += detalle.getPrecioBase();
            totalImpuestos += detalle.getTotalImpuestos();
            totalVentasConImpuestos += detalle.getPrecioFinal();
            
            informe.append(String.format(
                "Pedido: %s | Cliente: %s %s | Vehículo: %s %s\n" +
                "  • Precio Base: $%.2f | Impuestos: $%.2f | Total: $%.2f\n" +
                "  • Forma de Pago: %s | Estado: %s\n" +
                "  • Desglose Impuestos: %s\n\n",
                pedido.getNumeroDePedido(),
                pedido.getCliente().getNombre(),
                pedido.getCliente().getApellido(),
                pedido.getVehiculo().getMarca(),
                pedido.getVehiculo().getModelo(),
                detalle.getPrecioBase(),
                detalle.getTotalImpuestos(),
                detalle.getPrecioFinal(),
                pedido.getFormaDePago().name(),
                pedido.getEstadoActual().name(),
                detalle.generarResumen()
            ));
        }
        
        // Resumen de totales
        informe.append("=== RESUMEN DE TOTALES ===\n");
        informe.append(String.format("Total Pedidos: %d\n", pedidos.size()));
        informe.append(String.format("Total Ventas (sin impuestos): $%.2f\n", totalVentasSinImpuestos));
        informe.append(String.format("Total Impuestos: $%.2f\n", totalImpuestos));
        informe.append(String.format("Total Ventas (con impuestos): $%.2f\n", totalVentasConImpuestos));
        
        // Desglose por forma de pago
        informe.append("\n=== VENTAS POR FORMA DE PAGO ===\n");
        ventasPorFormaPago.forEach((forma, cantidad) -> {
            double montoForma = pedidos.stream()
                    .filter(p -> p.getFormaDePago().name().equals(forma))
                    .mapToDouble(p -> {
                        DetalleImpuestos det = impuestoService.calcularImpuestos(
                            p.getVehiculo().getTipo(), 
                            p.getVehiculo().getPrecioBase()
                        );
                        return det.getPrecioFinal();
                    })
                    .sum();
            
            informe.append(String.format("• %s: %d pedidos - $%.2f\n", 
                          forma, cantidad, montoForma));
        });
        
        return informe.toString();
    }
    
    /**
     * Genera informe de impuestos por tipo de vehículo
     */
    public String generarInformeImpuestos() {
        StringBuilder informe = new StringBuilder();
        
        informe.append(generarEncabezadoInforme("INFORME DE IMPUESTOS POR TIPO DE VEHÍCULO"));
        
        // Simular cálculo para cada tipo con precio base de ejemplo
        double precioEjemplo = 100000.0; // $100,000 como base
        
        informe.append("=== TABLA DE IMPUESTOS (Base: $100,000) ===\n");
        informe.append(String.format("%-12s | %-10s | %-10s | %-10s | %-10s\n", 
                      "Tipo", "Nacional", "Prov.Gral", "Prov.Adic", "Total"));
        informe.append("-".repeat(70)).append("\n");
        
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            if (impuestoService.tieneEstrategiaParaTipo(tipo)) {
                DetalleImpuestos detalle = impuestoService.calcularImpuestos(tipo, precioEjemplo);
                
                informe.append(String.format("%-12s | $%-9.2f | $%-9.2f | $%-9.2f | $%-9.2f\n",
                              tipo.name(),
                              detalle.getImpuestoNacional(),
                              detalle.getImpuestoProvincialGeneral(),
                              detalle.getImpuestoProvincialAdicional(),
                              detalle.getTotalImpuestos()));
            }
        }
        
        // Impuestos aplicados en pedidos reales
        List<Pedido> pedidos = pedidoService.getTodosPedidos();
        if (!pedidos.isEmpty()) {
            informe.append("\n=== IMPUESTOS EN PEDIDOS ACTUALES ===\n");
            
            Map<TipoVehiculo, Double> impuestosPorTipo = pedidos.stream()
                    .collect(Collectors.groupingBy(
                        p -> p.getVehiculo().getTipo(),
                        Collectors.summingDouble(p -> {
                            DetalleImpuestos det = impuestoService.calcularImpuestos(
                                p.getVehiculo().getTipo(), 
                                p.getVehiculo().getPrecioBase()
                            );
                            return det.getTotalImpuestos();
                        })
                    ));
            
            impuestosPorTipo.forEach((tipo, total) -> {
                long cantidad = pedidos.stream()
                        .filter(p -> p.getVehiculo().getTipo().equals(tipo))
                        .count();
                
                informe.append(String.format("• %s: %d vehículos - $%.2f en impuestos\n", 
                              tipo.name(), cantidad, total));
            });
        }
        
        return informe.toString();
    }
    
    /**
     * Genera informe de formas de pago disponibles
     */
    public String generarInformeFormasPago() {
        StringBuilder informe = new StringBuilder();
        
        informe.append(generarEncabezadoInforme("INFORME DE FORMAS DE PAGO"));
        
        informe.append("=== FORMAS DE PAGO DISPONIBLES ===\n\n");
        
        // Información de cada forma de pago
        double montoEjemplo = 50000.0;
        
        informe.append("1. PAGO AL CONTADO\n");
        informe.append("   • Descuento: 5%\n");
        informe.append("   • Procesamiento: Inmediato\n");
        informe.append(String.format("   • Ejemplo: $%.2f → $%.2f (ahorro: $%.2f)\n\n", 
                      montoEjemplo, montoEjemplo * 0.95, montoEjemplo * 0.05));
        
        informe.append("2. TRANSFERENCIA BANCARIA\n");
        informe.append("   • Recargo: Ninguno\n");
        informe.append("   • Procesamiento: 24-48 horas\n");
        informe.append(String.format("   • Ejemplo: $%.2f → $%.2f\n\n", 
                      montoEjemplo, montoEjemplo));
        
        informe.append("3. TARJETA DE CRÉDITO\n");
        informe.append("   • Recargos por cuotas:\n");
        informe.append("     - 1 cuota: 0%\n");
        informe.append("     - 2-3 cuotas: 5%\n");
        informe.append("     - 4-6 cuotas: 10%\n");
        informe.append("     - 7-9 cuotas: 15%\n");
        informe.append("     - 10-12 cuotas: 20%\n");
        informe.append("   • Procesamiento: Inmediato con autorización\n");
        informe.append(String.format("   • Ejemplo 6 cuotas: $%.2f → $%.2f (6 cuotas de $%.2f)\n\n", 
                      montoEjemplo, montoEjemplo * 1.10, (montoEjemplo * 1.10) / 6));
        
        // Estadísticas de uso si hay pedidos
        List<Pedido> pedidos = pedidoService.getTodosPedidos();
        if (!pedidos.isEmpty()) {
            informe.append("=== ESTADÍSTICAS DE USO ===\n");
            
            Map<String, Long> usoFormasPago = pedidos.stream()
                    .collect(Collectors.groupingBy(
                        p -> p.getFormaDePago().name(),
                        Collectors.counting()
                    ));
            
            long totalPedidos = pedidos.size();
            usoFormasPago.forEach((forma, cantidad) -> {
                double porcentaje = (cantidad * 100.0) / totalPedidos;
                informe.append(String.format("• %s: %d pedidos (%.1f%%)\n", 
                              forma, cantidad, porcentaje));
            });
        }
        
        return informe.toString();
    }
    
    /**
     * Genera un informe ejecutivo con resumen general
     */
    public String generarInformeEjecutivo() {
        StringBuilder informe = new StringBuilder();
        
        informe.append(generarEncabezadoInforme("INFORME EJECUTIVO"));
        
        List<Pedido> pedidos = pedidoService.getTodosPedidos();
        
        // Resumen general
        double totalVentas = pedidos.stream()
                .mapToDouble(p -> {
                    DetalleImpuestos det = impuestoService.calcularImpuestos(
                        p.getVehiculo().getTipo(), 
                        p.getVehiculo().getPrecioBase()
                    );
                    return det.getPrecioFinal();
                })
                .sum();
        
        double totalImpuestos = pedidos.stream()
                .mapToDouble(p -> {
                    DetalleImpuestos det = impuestoService.calcularImpuestos(
                        p.getVehiculo().getTipo(), 
                        p.getVehiculo().getPrecioBase()
                    );
                    return det.getTotalImpuestos();
                })
                .sum();
        
        informe.append("=== RESUMEN EJECUTIVO ===\n");
        informe.append(String.format("Total de Pedidos: %d\n", pedidos.size()));
        informe.append(String.format("Ventas Totales: $%.2f\n", totalVentas));
        informe.append(String.format("Impuestos Recaudados: $%.2f\n", totalImpuestos));
        informe.append(String.format("Promedio por Pedido: $%.2f\n", 
                      pedidos.isEmpty() ? 0 : totalVentas / pedidos.size()));
        
        return informe.toString();
    }
    
    /**
     * Genera encabezado estándar para los informes
     */
    private String generarEncabezadoInforme(String tipoInforme) {
        StringBuilder encabezado = new StringBuilder();
        
        encabezado.append("=".repeat(80)).append("\n");
        encabezado.append(String.format("   %s\n", concesionariaService.getNombre()));
        encabezado.append(String.format("   CUIT: %s\n", concesionariaService.getCuit()));
        encabezado.append(String.format("   %s\n", concesionariaService.getDireccionCompleta()));
        encabezado.append("=".repeat(80)).append("\n");
        encabezado.append(String.format("   %s\n", tipoInforme));
        encabezado.append(String.format("   Fecha: %s\n", 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        encabezado.append("=".repeat(80)).append("\n\n");
        
        return encabezado.toString();
    }
} 