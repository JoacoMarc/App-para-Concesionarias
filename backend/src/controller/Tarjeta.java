import java.io.*;
import java.util.*;

/**
 * 
 */
public class Tarjeta extends FormaDePago {

    /**
     * 
     */
    private String numeroTarjeta;

    /**
     * 
     */
    private String fechaVencimiento;

    /**
     * 
     */
    private String cvv;

    /**
     * 
     */
    private String tipo;

    /**
     * Default constructor
     */
    public Tarjeta() {
        super(); // Call to superclass constructor
    }

    /**
     * Constructor with parameters
     * @param numeroTarjeta
     * @param fechaVencimiento
     * @param cvv
     * @param tipo
     */
    public Tarjeta(String numeroTarjeta, String fechaVencimiento, String cvv, String tipo) {
        super(); // Call to superclass constructor
        this.numeroTarjeta = numeroTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
        this.tipo = tipo;
    }

    /**
     * @return the numeroTarjeta
     */
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    /**
     * @param numeroTarjeta the numeroTarjeta to set
     */
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    /**
     * @return the fechaVencimiento
     */
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the cvv
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @param cvv the cvv to set
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @param montoTotal
     */
    @Override
    public void procesarPago(double montoTotal) {
        // TODO implement here
        // For now, this method can just print a message to the console 
        // indicating that a card payment is being processed, 
        // including the amount and the last four digits of the card number. 
        // For example: "Procesando pago con tarjeta XXXX-XXXX-XXXX-1234 por monto: [montoTotal]".
        String lastFourDigits = "XXXX";
        if (this.numeroTarjeta != null && this.numeroTarjeta.length() > 4) {
            lastFourDigits = this.numeroTarjeta.substring(this.numeroTarjeta.length() - 4);
        }
        System.out.println("Procesando pago con tarjeta XXXX-XXXX-XXXX-" + lastFourDigits + " por monto: " + montoTotal);
        // Potentially update the estado of the payment
        setMonto(montoTotal);
        setEstado("Procesado");
    }

}
