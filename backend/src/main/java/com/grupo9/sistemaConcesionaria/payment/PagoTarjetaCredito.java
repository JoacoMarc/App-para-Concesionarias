package com.grupo9.sistemaConcesionaria.payment;

/**
 * PagoTarjetaCredito - Implementación específica para pagos con tarjeta de crédito
 * 
 * Hereda de FormaPagoBase e implementa el comportamiento específico
 * para pagos con tarjeta de crédito.
 */
public class PagoTarjetaCredito extends FormaPagoBase {
    
    private String numeroTarjeta;
    private String titularTarjeta;
    private String fechaVencimiento;
    private int cuotas;
    private double recargoPorCuotas;
    private String codigoAutorizacion;
    
    public PagoTarjetaCredito(String identificador, double monto, String numeroTarjeta, 
                             String titularTarjeta, String fechaVencimiento, int cuotas) {
        super(identificador, monto, "Pago con tarjeta de crédito");
        this.numeroTarjeta = enmascarar(numeroTarjeta);
        this.titularTarjeta = titularTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.cuotas = cuotas;
        this.recargoPorCuotas = calcularRecargoCuotas(cuotas);
        this.codigoAutorizacion = "AUTH-" + System.currentTimeMillis();
    }
    
    @Override
    public String getTipoFormaPago() {
        return "TARJETA_CREDITO";
    }
    
    @Override
    public void validarDatosEspecificos() {
        if (numeroTarjeta == null || numeroTarjeta.length() < 16) {
            throw new IllegalArgumentException("Número de tarjeta inválido");
        }
        if (titularTarjeta == null || titularTarjeta.trim().isEmpty()) {
            throw new IllegalArgumentException("El titular de la tarjeta es requerido");
        }
        if (fechaVencimiento == null || !fechaVencimiento.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Fecha de vencimiento inválida (formato MM/YY)");
        }
        if (cuotas < 1 || cuotas > 12) {
            throw new IllegalArgumentException("El número de cuotas debe estar entre 1 y 12");
        }
    }
    
    @Override
    public void ejecutarPago() {
        // Simular procesamiento con entidad bancaria
        // En un sistema real, aquí se conectaría con el procesador de pagos
        try {
            Thread.sleep(200); // Simular tiempo de autorización
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public String getDetallesPago() {
        return String.format("Tarjeta ****%s - %d cuotas - Titular: %s - Auth: %s", 
                           numeroTarjeta.substring(numeroTarjeta.length()-4),
                           cuotas,
                           titularTarjeta,
                           codigoAutorizacion);
    }
    
    @Override
    public double calcularComisionORecargo() {
        // Recargo por cuotas
        return monto * recargoPorCuotas;
    }
    
    @Override
    protected void generarComprobante() {
        super.generarComprobante();
        // Generar ticket específico con datos de la tarjeta
    }
    
    /**
     * Calcula el recargo por cuotas según la cantidad
     */
    private double calcularRecargoCuotas(int cuotas) {
        if (cuotas <= 1) return 0.0;
        if (cuotas <= 3) return 0.05; // 5%
        if (cuotas <= 6) return 0.10; // 10%
        if (cuotas <= 9) return 0.15; // 15%
        return 0.20; // 20%
    }
    
    /**
     * Enmascara el número de tarjeta para seguridad
     */
    private String enmascarar(String numeroTarjeta) {
        if (numeroTarjeta == null || numeroTarjeta.length() < 4) {
            return numeroTarjeta;
        }
        return "****-****-****-" + numeroTarjeta.substring(numeroTarjeta.length()-4);
    }
    
    /**
     * Calcula el valor de cada cuota
     */
    public double calcularValorCuota() {
        double montoFinal = calcularMontoFinal();
        return montoFinal / cuotas;
    }
    
    // Getters específicos
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
    
    public String getTitularTarjeta() {
        return titularTarjeta;
    }
    
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public int getCuotas() {
        return cuotas;
    }
    
    public double getRecargoPorCuotas() {
        return recargoPorCuotas;
    }
    
    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }
} 