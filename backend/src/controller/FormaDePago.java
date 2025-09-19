
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class FormaDePago implements IFormaDePago {

    /**
     * Default constructor
     */
    public FormaDePago() {
    }

    /**
     * 
     */
    private int idFormaDePago;

    /**
     * 
     */
    private double monto;

    /**
     * 
     */
    private String estado;


    /**
     * @param montoTotal
     */
    @Override
    public abstract void procesarPago(double montoTotal);

    public int getIdFormaDePago() {
        return idFormaDePago;
    }

    public void setIdFormaDePago(int idFormaDePago) {
        this.idFormaDePago = idFormaDePago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @Override
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}