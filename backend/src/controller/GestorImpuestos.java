
import java.io.*;
import java.util.*;

/**
 * 
 */
public class GestorImpuestos implements InterfazGestorImpuestos {

    /**
     * Default constructor
     */
    public GestorImpuestos() {
        this.impuestos = new HashMap<>();
    }

    /**
     * 
     */
    private HashMap<Integer, Impuesto> impuestos;


    @Override
    public void agregarImpuesto(Impuesto impuesto) {
        // TODO implement here
        // Example: this.impuestos.put(impuesto.getId(), impuesto);
    }

    @Override
    public void eliminarImpuesto(int idImpuesto) {
        // TODO implement here
        // Example: this.impuestos.remove(idImpuesto);
    }

    @Override
    public void actualizarImpuesto(Impuesto impuesto) {
        // TODO implement here
        // Example: this.impuestos.put(impuesto.getId(), impuesto);
    }

    @Override
    public Impuesto getImpuesto(int idImpuesto) {
        // TODO implement here
        // Example: return this.impuestos.get(idImpuesto);
        return null; 
    }

    /**
     * Example of a method to get all impuestos, if needed by the interface later.
     */
    public Map<Integer, Impuesto> getAllImpuestos() {
        return new HashMap<>(this.impuestos);
    }
}