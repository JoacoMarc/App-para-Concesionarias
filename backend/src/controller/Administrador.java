
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Administrador extends Usuario {

    /**
     * Default constructor
     */
    public Administrador() {
    }

    /**
     * 
     */
    private String area;

    /**
     * 
     */
    public void gestionarClientes() {
        System.out.println("Administrador: Gestionando clientes...");
    }

    /**
     * 
     */
    public void gestionarVehiculos() {
        System.out.println("Administrador: Gestionando vehículos...");
    }

    /**
     * 
     */
    public void gestionarPedidos() {
        System.out.println("Administrador: Gestionando pedidos...");
    }

    /**
     * 
     */
    public void generarInformes() {
        System.out.println("Administrador: Generando informes...");
    }

    /**
     * 
     */
    public void configurarSistema() {
        System.out.println("Administrador: Configurando sistema...");
    }

    /**
     * Sets the area for the administrator.
     * @param area The area to set.
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * Gets the area of the administrator.
     * @return The area of the administrator.
     */
    public String getArea() {
        return this.area;
    }

}