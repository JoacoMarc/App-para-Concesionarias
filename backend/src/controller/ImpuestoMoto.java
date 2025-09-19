
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ImpuestoMoto implements InterfazImpuestoStrategy {

    /**
     * Default constructor
     */
    public ImpuestoMoto() {
    }

    /**
     * Calculates the total tax for a Moto based on its base price.
     * The tax logic is as follows:
     * - Impuesto Nacional: 0% of precioBase (Not applicable)
     * - Impuesto Provincial General: 5% of precioBase
     * - Impuesto Provincial Adicional: 1% of precioBase
     * 
     * @param precioBase The base price of the vehicle.
     * @return The total calculated tax amount.
     */
    @Override
    public double calcularImpuesto(double precioBase) {
        double impuestoNacional = 0.0 * precioBase; // Or simply 0.0
        double impuestoProvincialGeneral = 0.05 * precioBase;
        double impuestoProvincialAdicional = 0.01 * precioBase;
        
        return impuestoNacional + impuestoProvincialGeneral + impuestoProvincialAdicional;
    }

}