
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Vehiculo implements IVehiculo {

    /**
     * Default constructor
     */
    public Vehiculo() {
    }

    /**
     * 
     */
    private int idVehiculo;

    /**
     * 
     */
    private String marca;

    /**
     * 
     */
    private String modelo;

    /**
     * 
     */
    private String color;

    /**
     * 
     */
    private String numeroChasis;

    /**
     * 
     */
    private String numeroMotor;

    /**
     * 
     */
    private boolean disponibleVenta;

    /**
     * 
     */
    private double precioBase;

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    @Override
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumeroChasis() {
        return numeroChasis;
    }

    public void setNumeroChasis(String numeroChasis) {
        this.numeroChasis = numeroChasis;
    }

    public String getNumeroMotor() {
        return numeroMotor;
    }

    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    public boolean isDisponibleVenta() {
        return disponibleVenta;
    }

    public void setDisponibleVenta(boolean disponibleVenta) {
        this.disponibleVenta = disponibleVenta;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

}