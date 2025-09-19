package controller;

/**
 * Base exception class for the concesionaria application.
 * All custom application-specific exceptions should extend this class.
 */
public class ConcesionariaException extends Exception {

    /**
     * Constructs a new ConcesionariaException with the specified detail message.
     * @param message the detail message.
     */
    public ConcesionariaException(String message) {
        super(message);
    }
}
