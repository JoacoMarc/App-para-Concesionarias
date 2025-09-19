package controller;

/**
 * Exception thrown when a requested client is not found.
 */
public class ClienteNotFoundException extends ConcesionariaException {

    /**
     * Constructs a new ClienteNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public ClienteNotFoundException(String message) {
        super(message);
    }
}
