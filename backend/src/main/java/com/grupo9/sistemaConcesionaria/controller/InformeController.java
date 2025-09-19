package com.grupo9.sistemaConcesionaria.controller;

import com.grupo9.sistemaConcesionaria.service.InformeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * InformeController - Controlador REST para generación de informes
 * 
 * Expone endpoints para generar diferentes tipos de informes del sistema
 * con información de precios, impuestos y formas de pago.
 */
@RestController
@RequestMapping("/api/informes")
@CrossOrigin(origins = "*")
public class InformeController {

    @Autowired
    private InformeService informeService;

    /**
     * Genera informe completo de ventas
     */
    @GetMapping("/ventas")
    public ResponseEntity<String> getInformeVentas() {
        try {
            String informe = informeService.generarInformeVentas();
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error generando informe de ventas: " + e.getMessage());
        }
    }

    /**
     * Genera informe de impuestos por tipo de vehículo
     */
    @GetMapping("/impuestos")
    public ResponseEntity<String> getInformeImpuestos() {
        try {
            String informe = informeService.generarInformeImpuestos();
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error generando informe de impuestos: " + e.getMessage());
        }
    }

    /**
     * Genera informe de formas de pago
     */
    @GetMapping("/formas-pago")
    public ResponseEntity<String> getInformeFormasPago() {
        try {
            String informe = informeService.generarInformeFormasPago();
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error generando informe de formas de pago: " + e.getMessage());
        }
    }

    /**
     * Genera informe ejecutivo
     */
    @GetMapping("/ejecutivo")
    public ResponseEntity<String> getInformeEjecutivo() {
        try {
            String informe = informeService.generarInformeEjecutivo();
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error generando informe ejecutivo: " + e.getMessage());
        }
    }

    /**
     * Obtiene índice de todos los informes disponibles
     */
    @GetMapping
    public ResponseEntity<?> getIndiceInformes() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "📊 Sistema de Informes AutoMax Concesionaria",
            "informesDisponibles", Map.of(
                "ventas", Map.of(
                    "endpoint", "/api/informes/ventas",
                    "descripcion", "Informe completo de ventas con desglose de precios e impuestos",
                    "contenido", "Desglose por pedido, totales, ventas por forma de pago"
                ),
                "impuestos", Map.of(
                    "endpoint", "/api/informes/impuestos",
                    "descripcion", "Tabla de impuestos por tipo de vehículo",
                    "contenido", "Porcentajes y cálculos para AUTO, MOTO, CAMIONETA, CAMION"
                ),
                "formasPago", Map.of(
                    "endpoint", "/api/informes/formas-pago",
                    "descripcion", "Información detallada de formas de pago disponibles",
                    "contenido", "Contado, transferencia, tarjeta de crédito con recargos y descuentos"
                ),
                "ejecutivo", Map.of(
                    "endpoint", "/api/informes/ejecutivo",
                    "descripcion", "Resumen ejecutivo con métricas principales",
                    "contenido", "Total de ventas, impuestos recaudados, promedios"
                )
            ),
            "ejemploUso", Map.of(
                "curl", "curl -X GET 'http://localhost:8080/api/informes/ventas'",
                "descripcion", "Obtiene el informe de ventas en formato texto"
            ),
            "características", new String[]{
                "✅ Informes en tiempo real basados en datos actuales",
                "✅ Desglose detallado de impuestos por tipo de vehículo",
                "✅ Análisis de formas de pago con recargos y descuentos",
                "✅ Encabezados con información de la concesionaria",
                "✅ Formatos legibles para impresión o visualización"
            }
        ));
    }
} 