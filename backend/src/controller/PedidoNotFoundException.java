package controller;

/**
 * Exception thrown when a requested pedido (order) is not found.
 */
public class PedidoNotFoundException extends ConcesionariaException {

    /**
     * Constructs a new PedidoNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public PedidoNotFoundException(String message) {
        super(message);
    }
}
