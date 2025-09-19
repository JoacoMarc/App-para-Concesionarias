package controller; // Assuming it's in the controller package

import java.util.List; // For potential return types like List<Pedido> or List<Vehiculo>

// Assuming these interfaces/classes will exist in the controller package or are imported.
// import controller.InterfazGestorDePedidos;
// import controller.Catalogo;
// import controller.Pedido; // If Pedido objects are directly handled
// import controller.Vehiculo; // If Vehiculo objects are directly handled

/**
 * Represents a buyer in the system, extending the base Usuario class.
 * Comprador can view their order statuses and consult available vehicles.
 */
public class Comprador extends Usuario {

    /**
     * Default constructor for Comprador.
     * Calls the superclass (Usuario) default constructor.
     */
    public Comprador() {
        super(); // Call to Usuario's default constructor
    }

    /**
     * Allows the buyer to see the status of their orders.
     * This method will interact with a gestor de pedidos to fetch and display order statuses.
     * 
     * @param gestorPedidos The order management system interface.
     * @param idCliente The ID of the client whose orders are to be viewed.
     */
    public void verEstadoSusPedidos(InterfazGestorDePedidos gestorPedidos, int idCliente) {
        // Placeholder implementation:
        System.out.println("Fetching orders for client ID: " + idCliente);
        
        // Expected future implementation:
        // List<Pedido> pedidos = gestorPedidos.getPedidosPorCliente(idCliente);
        // if (pedidos != null && !pedidos.isEmpty()) {
        //     System.out.println("Estado de sus pedidos:");
        //     for (Pedido pedido : pedidos) {
        //         System.out.println("Pedido ID: " + pedido.getIdPedido() + " - Estado: " + pedido.getEstado());
        //     }
        // } else {
        //     System.out.println("No se encontraron pedidos para el cliente ID: " + idCliente);
        // }
    }

    /**
     * Allows the buyer to consult available vehicles in the catalog.
     * This method will interact with a catalogo to fetch and display available vehicles.
     * 
     * @param catalogo The catalog system containing vehicle information.
     */
    public void consultarVehiculosDisponibles(Catalogo catalogo) {
        // Placeholder implementation:
        System.out.println("Fetching available vehicles from catalog.");

        // Expected future implementation:
        // List<Vehiculo> vehiculos = catalogo.getVehiculosDisponibles();
        // if (vehiculos != null && !vehiculos.isEmpty()) {
        //     System.out.println("Vehículos disponibles:");
        //     for (Vehiculo vehiculo : vehiculos) {
        //         System.out.println(
        //             "Vehiculo ID: " + vehiculo.getIdVehiculo() +
        //             ", Marca: " + vehiculo.getMarca() +
        //             ", Modelo: " + vehiculo.getModelo() +
        //             (vehiculo.isDisponibleVenta() ? " (Disponible)" : " (No disponible)")
        //         );
        //     }
        // } else {
        //     System.out.println("No hay vehículos disponibles actualmente.");
        // }
    }
}
