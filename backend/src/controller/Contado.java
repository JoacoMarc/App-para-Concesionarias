
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Contado extends FormaDePago {

    /**
     * 
     */
    private String moneda;

    /**
     * Default constructor
     */
    public Contado(String moneda) {
        this.moneda = moneda;
    }

    /**
     * 
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * 
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param montoTotal
     */
    @Override
    public void procesarPago(double montoTotal) {
        // TODO implement here
        System.out.println("Procesando pago en efectivo: " + montoTotal + " en " + this.moneda);
    }

}