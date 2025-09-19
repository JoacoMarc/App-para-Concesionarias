
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Vendedor extends Usuario {

    /**
     * Default constructor
     */
    public Vendedor() {
    }

    /**
     * 
     */
    private int ventasTotales; // Corrected type from Int to int

    /**
     * 
     */
    public void consultarCatalogoDeVehiculos() {
        System.out.println("Vendedor: Consultando catálogo de vehículos...");
    }

    /**
     * 
     */
    public void gestionarVentas() {
        System.out.println("Vendedor: Gestionando ventas...");
    }

    /**
     * Gets the total sales for the vendor.
     * @return The total sales.
     */
    public int getVentasTotales() {
        return this.ventasTotales;
    }

    /**
     * Sets the total sales for the vendor.
     * @param ventasTotales The total sales to set.
     */
    public void setVentasTotales(int ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

}