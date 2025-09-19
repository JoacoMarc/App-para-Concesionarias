package controller;

/**
 * Exception thrown when an attempt is made to add a system user that already exists.
 */
public class DuplicateUsuarioException extends ConcesionariaException {

    /**
     * Constructs a new DuplicateUsuarioException with the specified detail message.
     * @param message the detail message.
     */
    public DuplicateUsuarioException(String message) {
        super(message);
    }
}
