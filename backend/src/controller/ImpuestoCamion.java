
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ImpuestoCamion implements InterfazImpuestoStrategy {

    /**
     * Default constructor
     */
    public ImpuestoCamion() {
    }

    /**
     * Calculates the total tax for a Camion based on its base price.
     * The tax logic is as follows:
     * - Impuesto Nacional: 0% of precioBase (Not applicable)
     * - Impuesto Provincial General: 5% of precioBase
     * - Impuesto Provincial Adicional: 2% of precioBase
     * 
     * @param precioBase The base price of the vehicle.
     * @return The total calculated tax amount.
     */
    @Override
    public double calcularImpuesto(double precioBase) {
        double impuestoNacional = 0.0 * precioBase; // Or simply 0.0
        double impuestoProvincialGeneral = 0.05 * precioBase;
        double impuestoProvincialAdicional = 0.02 * precioBase;
        
        return impuestoNacional + impuestoProvincialGeneral + impuestoProvincialAdicional;
    }

}