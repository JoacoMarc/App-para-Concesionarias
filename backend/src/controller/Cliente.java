
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Cliente implements ICliente {

    /**
     * Default constructor
     */
    public Cliente() {
    }

    /**
     * 
     */
    private String nombre;
    private String documento;
    private String correoElectronico;
    private String telefono;
    private int idCliente; // Added field for client ID
    // Assuming other fields like email, direccion might exist or be added later.
    // For now, only implementing what's in ICliente.

    /**
     * 
     */
    public void verEstadoPedidos() {
        // TODO implement here
    }

    /**
     * 
     */
    public void consultarCatalogoVehiculos() {
        // TODO implement here
    }

    /**
     * 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * 
     */
    @Override
    public String getNombre() {
        return nombre;
    }

    /**
     * 
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * 
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * 
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * 
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /**
     * 
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * 
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the idCliente
     */
    @Override
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

}