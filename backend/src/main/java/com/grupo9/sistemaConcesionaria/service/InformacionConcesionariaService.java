package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.InformacionConcesionaria;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

/**
 * Servicio para manejar la información de la concesionaria
 * Implementa patrón Singleton para datos de la empresa
 */
@Service
public class InformacionConcesionariaService {

    private static final Logger logger = LoggerFactory.getLogger(InformacionConcesionariaService.class);
    
    private InformacionConcesionaria informacionConcesionaria;

    @PostConstruct
    public void inicializar() {
        // Inicializar con datos predeterminados de la concesionaria
        this.informacionConcesionaria = new InformacionConcesionaria(
            "AutoMax Concesionaria",
            "30-12345678-9",
            "Av. del Libertador 1234"
        );
        
        // Configurar datos adicionales
        informacionConcesionaria.setCiudad("Buenos Aires");
        informacionConcesionaria.setProvincia("Buenos Aires");
        informacionConcesionaria.setCodigoPostal("C1425ABC");
        informacionConcesionaria.setTelefono("011-4567-8900");
        informacionConcesionaria.setEmail("info@automax.com.ar");
        informacionConcesionaria.setSitioWeb("www.automax.com.ar");
        informacionConcesionaria.setNumeroHabilitacion("HAB-2024-001");
        
        logger.info("Información de concesionaria inicializada: {}", 
                   informacionConcesionaria.getNombre());
    }

    /**
     * Obtiene la información actual de la concesionaria
     * @return InformacionConcesionaria
     */
    public InformacionConcesionaria getInformacionConcesionaria() {
        return informacionConcesionaria;
    }

    /**
     * Actualiza la información de la concesionaria
     * @param nuevaInformacion Nueva información
     */
    public void actualizarInformacion(InformacionConcesionaria nuevaInformacion) {
        if (nuevaInformacion != null) {
            this.informacionConcesionaria = nuevaInformacion;
            logger.info("Información de concesionaria actualizada");
        }
    }

    /**
     * Obtiene el encabezado para facturas
     * @return String con el encabezado formateado
     */
    public String getEncabezadoFactura() {
        return informacionConcesionaria.getEncabezadoFactura();
    }

    /**
     * Obtiene los datos de contacto
     * @return String con los datos de contacto
     */
    public String getDatosContacto() {
        return informacionConcesionaria.getDatosContacto();
    }

    /**
     * Obtiene la dirección completa
     * @return String con la dirección completa
     */
    public String getDireccionCompleta() {
        return informacionConcesionaria.getDireccionCompleta();
    }

    /**
     * Obtiene el nombre de la concesionaria
     * @return Nombre de la concesionaria
     */
    public String getNombre() {
        return informacionConcesionaria.getNombre();
    }

    /**
     * Obtiene el CUIT de la concesionaria
     * @return CUIT de la concesionaria
     */
    public String getCuit() {
        return informacionConcesionaria.getCuit();
    }

    /**
     * Genera información completa para reportes
     * @return String con información completa
     */
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        info.append(informacionConcesionaria.getEncabezadoFactura());
        info.append("\n");
        info.append(informacionConcesionaria.getDatosContacto());
        if (informacionConcesionaria.getNumeroHabilitacion() != null) {
            info.append("\nHabilitación N°: ").append(informacionConcesionaria.getNumeroHabilitacion());
        }
        return info.toString();
    }
} 