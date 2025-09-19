package com.grupo9.sistemaConcesionaria.payment;

/**
 * PagoTransferencia - Implementación específica para pagos por transferencia bancaria
 * 
 * Hereda de FormaPagoBase e implementa el comportamiento específico
 * para transferencias bancarias.
 */
public class PagoTransferencia extends FormaPagoBase {
    
    private String cuentaOrigen;
    private String cuentaDestino;
    private String banco;
    private String numeroReferencia;
    
    public PagoTransferencia(String identificador, double monto, String cuentaOrigen, String banco) {
        super(identificador, monto, "Pago por transferencia bancaria");
        this.cuentaOrigen = cuentaOrigen;
        this.banco = banco;
        this.cuentaDestino = "1234567890"; // Cuenta de la concesionaria
        this.numeroReferencia = "TRF-" + System.currentTimeMillis();
    }
    
    @Override
    public String getTipoFormaPago() {
        return "TRANSFERENCIA";
    }
    
    @Override
    public void validarDatosEspecificos() {
        if (cuentaOrigen == null || cuentaOrigen.trim().isEmpty()) {
            throw new IllegalArgumentException("La cuenta origen es requerida");
        }
        if (banco == null || banco.trim().isEmpty()) {
            throw new IllegalArgumentException("El banco es requerido");
        }
        if (cuentaOrigen.length() < 10) {
            throw new IllegalArgumentException("Número de cuenta inválido");
        }
    }
    
    @Override
    public void ejecutarPago() {
        // Simular procesamiento de transferencia bancaria
        // En un sistema real, aquí se conectaría con la API del banco
        try {
            Thread.sleep(100); // Simular tiempo de procesamiento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public String getDetallesPago() {
        return String.format("Transferencia desde %s (%s) - Ref: %s", 
                           cuentaOrigen.substring(cuentaOrigen.length()-4), 
                           banco, 
                           numeroReferencia);
    }
    
    @Override
    public double calcularComisionORecargo() {
        // Las transferencias no tienen recargo adicional
        return 0.0;
    }
    
    @Override
    protected void generarComprobante() {
        super.generarComprobante();
        // Generar comprobante específico con datos de la transferencia
    }
    
    // Getters y Setters específicos
    public String getCuentaOrigen() {
        return cuentaOrigen;
    }
    
    public String getCuentaDestino() {
        return cuentaDestino;
    }
    
    public String getBanco() {
        return banco;
    }
    
    public String getNumeroReferencia() {
        return numeroReferencia;
    }
} 