package com.grupo9.sistemaConcesionaria.model;

/**
 * Enum TipoVehiculo - Tipos de vehículos disponibles
 * Según consignas: Auto | Camioneta | Moto | Camión
 */
public enum TipoVehiculo {
    AUTO("Auto", "Automóvil"),
    CAMIONETA("Camioneta", "Camioneta"),
    MOTO("Moto", "Motocicleta"),
    CAMION("Camión", "Camión");

    private final String codigo;
    private final String descripcion;

    TipoVehiculo(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el tipo de vehículo desde un string
     * @param tipo string del tipo
     * @return TipoVehiculo correspondiente
     */
    public static TipoVehiculo fromString(String tipo) {
        if (tipo == null) return AUTO;
        
        switch (tipo.toLowerCase().trim()) {
            case "auto":
            case "automóvil":
            case "coche":
                return AUTO;
            case "camioneta":
            case "pickup":
                return CAMIONETA;
            case "moto":
            case "motocicleta":
            case "motorcycle":
                return MOTO;
            case "camión":
            case "camion":
            case "truck":
                return CAMION;
            default:
                return AUTO;
        }
    }

    /**
     * Verifica si el tipo de vehículo requiere licencia especial
     * @return true si requiere licencia especial
     */
    public boolean requiereLicenciaEspecial() {
        return this == CAMION;
    }
} 