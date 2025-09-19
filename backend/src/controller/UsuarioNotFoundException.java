package controller;

/**
 * Exception thrown when a requested system user is not found.
 */
public class UsuarioNotFoundException extends ConcesionariaException {

    /**
     * Constructs a new UsuarioNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
