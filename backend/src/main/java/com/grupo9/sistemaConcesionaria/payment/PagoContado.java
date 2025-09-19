package com.grupo9.sistemaConcesionaria.payment;

/**
 * PagoContado - Implementación específica para pagos al contado
 * 
 * Hereda de FormaPagoBase e implementa el comportamiento específico
 * para pagos al contado (efectivo).
 */
public class PagoContado extends FormaPagoBase {
    
    private double descuentoContado;
    
    public PagoContado(String identificador, double monto) {
        super(identificador, monto, "Pago al contado en efectivo");
        this.descuentoContado = 0.05; // 5% de descuento por pago al contado
    }
    
    @Override
    public String getTipoFormaPago() {
        return "CONTADO";
    }
    
    @Override
    public void validarDatosEspecificos() {
        // Para contado no hay validaciones adicionales específicas
        // El dinero debe estar físicamente disponible
    }
    
    @Override
    public void ejecutarPago() {
        // Simular procesamiento inmediato del pago al contado
        // En un sistema real, aquí se registraría la recepción del efectivo
    }
    
    @Override
    public String getDetallesPago() {
        return String.format("Efectivo - Descuento aplicado: %.1f%%", descuentoContado * 100);
    }
    
    @Override
    public double calcularComisionORecargo() {
        // El pago al contado tiene descuento (valor negativo)
        return -monto * descuentoContado;
    }
    
    @Override
    protected void generarComprobante() {
        super.generarComprobante();
        // Generar recibo específico para pago al contado
    }
    
    public double getDescuentoContado() {
        return descuentoContado;
    }
    
    public void setDescuentoContado(double descuentoContado) {
        this.descuentoContado = descuentoContado;
    }
} 