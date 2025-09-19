
import java.io.*;
import java.util.*;

/**
 * 
 */
import java.util.Collection;
import java.util.List;
// Assuming Pedido, DuplicatePedidoException, PedidoNotFoundException are in the controller package or imported.

public interface InterfazGestorDePedidos {

    /**
     * Registers a new pedido.
     * @param pedido The pedido to register.
     * @throws DuplicatePedidoException if a pedido with the same ID already exists.
     */
    void registrarPedido(Pedido pedido) throws DuplicatePedidoException;

    /**
     * Retrieves a pedido by its ID.
     * @param idPedido The ID of the pedido to retrieve.
     * @return The found pedido.
     * @throws PedidoNotFoundException if the pedido is not found.
     */
    Pedido getPedido(int idPedido) throws PedidoNotFoundException;

    /**
     * Updates an existing pedido.
     * @param pedido The pedido with updated information.
     * @throws PedidoNotFoundException if the pedido to update is not found.
     */
    void actualizarPedido(Pedido pedido) throws PedidoNotFoundException;

    /**
     * Eliminates a pedido by its ID.
     * @param idPedido The ID of the pedido to eliminate.
     * @throws PedidoNotFoundException if the pedido is not found.
     */
    void eliminarPedido(int idPedido) throws PedidoNotFoundException;

    /**
     * Retrieves all pedidos.
     * @return A collection of all pedidos.
     */
    Collection<Pedido> getTodosLosPedidos();

    /**
     * Retrieves all pedidos for a specific client.
     * @param idCliente The ID of the client.
     * @return A list of pedidos for the client.
     */
    List<Pedido> getPedidosPorCliente(int idCliente);

    /**
     * Retrieves all pedidos that are in a specific state.
     * @param estado The state to filter pedidos by.
     * @return A list of pedidos in the given state.
     */
    List<Pedido> getPedidosPorEstado(String estado);
    
    /**
     * Processes all pending pedidos or performs routine operations on pedidos.
     * (Further definition of "processing" needed if it's more than a placeholder).
     */
    void procesarPedidos();

    /**
     * Generates a report of pedidos, filtered by date and state.
     * @param filtroFecha Date filter string (e.g., "YYYY-MM-DD"). Can be null or empty.
     * @param filtroEstado State filter string. Can be null or empty.
     * @return A string containing the generated report.
     */
    String generarInforme(String filtroFecha, String filtroEstado);

    /**
     * Generates a report summarizing total amounts and counts by payment method.
     * @return A string containing the formatted report.
     */
    public String generarInformeTotalesPorFormaDePago();
}