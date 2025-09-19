
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ImpuestoAuto implements InterfazImpuestoStrategy {

    /**
     * Default constructor
     */
    public ImpuestoAuto() {
    }

    /**
     * Calculates the total tax for an Auto based on its base price.
     * Taxes include:
     * - Impuesto Nacional: 21% of precioBase
     * - Impuesto Provincial General: 5% of precioBase
     * - Impuesto Provincial Adicional: 1% of precioBase
     * @param precioBase The base price of the vehicle.
     * @return The total calculated tax amount.
     */
    @Override
    public double calcularImpuesto(double precioBase) {
        double impuestoNacional = 0.21 * precioBase;
        double impuestoProvincialGeneral = 0.05 * precioBase;
        double impuestoProvincialAdicional = 0.01 * precioBase;
        
        return impuestoNacional + impuestoProvincialGeneral + impuestoProvincialAdicional;
    }

}