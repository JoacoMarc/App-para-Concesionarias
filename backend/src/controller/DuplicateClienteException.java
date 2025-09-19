package controller;

/**
 * Exception thrown when an attempt is made to add a client that already exists.
 */
public class DuplicateClienteException extends ConcesionariaException {

    /**
     * Constructs a new DuplicateClienteException with the specified detail message.
     * @param message the detail message.
     */
    public DuplicateClienteException(String message) {
        super(message);
    }
}
