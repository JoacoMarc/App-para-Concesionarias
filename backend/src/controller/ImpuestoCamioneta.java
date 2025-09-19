
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ImpuestoCamioneta implements InterfazImpuestoStrategy {

    /**
     * Default constructor
     */
    public ImpuestoCamioneta() {
    }

    /**
     * Calculates the total tax for a Camioneta based on its base price.
     * The tax logic is as follows:
     * - Impuesto Nacional: 10% of precioBase
     * - Impuesto Provincial General: 5% of precioBase
     * - Impuesto Provincial Adicional: 2% of precioBase
     * 
     * @param precioBase The base price of the vehicle.
     * @return The total calculated tax amount.
     */
    @Override
    public double calcularImpuesto(double precioBase) {
        double impuestoNacional = 0.10 * precioBase;
        double impuestoProvincialGeneral = 0.05 * precioBase;
        double impuestoProvincialAdicional = 0.02 * precioBase;
        
        return impuestoNacional + impuestoProvincialGeneral + impuestoProvincialAdicional;
    }

}