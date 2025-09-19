package controller;

/**
 * Exception thrown when a requested vehicle is not found.
 */
public class VehiculoNotFoundException extends ConcesionariaException {

    /**
     * Constructs a new VehiculoNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public VehiculoNotFoundException(String message) {
        super(message);
    }
}
