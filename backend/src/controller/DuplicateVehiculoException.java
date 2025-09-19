package controller;

/**
 * Exception thrown when an attempt is made to add a vehicle that already exists.
 */
public class DuplicateVehiculoException extends ConcesionariaException {

    /**
     * Constructs a new DuplicateVehiculoException with the specified detail message.
     * @param message the detail message.
     */
    public DuplicateVehiculoException(String message) {
        super(message);
    }
}
