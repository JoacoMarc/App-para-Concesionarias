package controller;

/**
 * Exception thrown when an attempt is made to add a pedido (order) that already exists.
 */
public class DuplicatePedidoException extends ConcesionariaException {

    /**
     * Constructs a new DuplicatePedidoException with the specified detail message.
     * @param message the detail message.
     */
    public DuplicatePedidoException(String message) {
        super(message);
    }
}
