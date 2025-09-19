
import java.io.*;
import java.util.*;

/**
 * 
 */
public interface InterfazDeSistema {

    /**
     * 
     */
    private GestorDePedido gestorDePedidos;

    /**
     * 
     */
    private GestorDeUsuario gestorDeUsuario;

    /**
     * 
     */
    private GestorDeVehiculo gestorDeVehiculo;




    /**
     * 
     */
    public void realizarPedido();

    /**
     * 
     */
    public void procesarPedido();

    /**
     * 
     */
    public void generarReporteDeVentas();

    /**
     * 
     */
    public void agregarUsuario();

    /**
     * 
     */
    public void eliminarUsuario();

    /**
     * 
     */
    public void actualizarUsuario();

    /**
     * 
     */
    public void agregarVehiculo();

    /**
     * 
     */
    public void actualizarVehiculo();

    /**
     * 
     */
    public void eliminarVehiculo();

}