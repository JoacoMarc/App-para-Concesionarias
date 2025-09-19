
import java.io.*;
import java.util.*;

/**
 * 
 */
public class TarjetaDeCredito extends FormaDePago {

    /**
     * Default constructor
     */
    public TarjetaDeCredito() {
    }

    /**
     * 
     */
    private int cuotas;

    /**
     * 
     */
    private String banco;

    public TarjetaDeCredito(int cuotas, String banco) {
        this.cuotas = cuotas;
        this.banco = banco;
    }

    /**
     * 
     */
    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    /**
     * 
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * 
     */
    public int getCuotas() {
        return cuotas;
    }

    /**
     * 
     */
    public String getBanco() {
        return banco;
    }

    /**
     * @param montoTotal
     */
    @Override
    public void procesarPago(double montoTotal) {
        // Logic to process credit card payment using this.cuotas and this.banco
        // For example:
        System.out.println("Procesando pago con tarjeta de credito: " + montoTotal + 
                           " en " + this.cuotas + " cuotas con el banco " + this.banco);
        // TODO: Implement actual payment processing logic
    }

}