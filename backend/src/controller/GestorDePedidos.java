import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat; // For parsing filtroFecha in generarInforme

// Assuming Pedido, DuplicatePedidoException, PedidoNotFoundException, ICliente are in controller package or imported

/**
 * Manages orders (pedidos), implementing the Singleton pattern.
 */
public class GestorDePedidos implements InterfazGestorDePedidos {

    private static GestorDePedidos instancia;
    private HashMap<Integer, Pedido> pedidos; // Key: idPedido

    /**
     * Private constructor for Singleton pattern.
     * Initializes the pedidos storage.
     */
    private GestorDePedidos() {
        this.pedidos = new HashMap<>(); // Initialize the map
    }

    /**
     * Provides the global access point to the GestorDePedidos instance.
     * @return The single instance of GestorDePedidos.
     */
    public static GestorDePedidos getInstancia() {
        if (instancia == null) {
            instancia = new GestorDePedidos();
        }
        return instancia;
    }

    @Override
    public void registrarPedido(Pedido pedido) throws DuplicatePedidoException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        if (this.pedidos.containsKey(pedido.getIdPedido())) {
            throw new DuplicatePedidoException("Pedido con ID " + pedido.getIdPedido() + " ya existe.");
        }
        this.pedidos.put(pedido.getIdPedido(), pedido);
        System.out.println("Pedido registrado con ID: " + pedido.getIdPedido());
    }

    @Override
    public Pedido getPedido(int idPedido) throws PedidoNotFoundException {
        Pedido pedido = this.pedidos.get(idPedido);
        if (pedido == null) {
            throw new PedidoNotFoundException("Pedido con ID " + idPedido + " no encontrado.");
        }
        return pedido;
    }

    @Override
    public void actualizarPedido(Pedido pedido) throws PedidoNotFoundException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido para actualizar no puede ser nulo.");
        }
        if (!this.pedidos.containsKey(pedido.getIdPedido())) {
            throw new PedidoNotFoundException("Pedido con ID " + pedido.getIdPedido() + " no encontrado para actualizar.");
        }
        this.pedidos.put(pedido.getIdPedido(), pedido);
        System.out.println("Pedido actualizado con ID: " + pedido.getIdPedido());
    }

    @Override
    public void eliminarPedido(int idPedido) throws PedidoNotFoundException {
        Pedido pedidoRemovido = this.pedidos.remove(idPedido);
        if (pedidoRemovido == null) {
            throw new PedidoNotFoundException("Pedido con ID " + idPedido + " no encontrado para eliminar.");
        }
        System.out.println("Pedido eliminado con ID: " + idPedido);
    }

    @Override
    public Collection<Pedido> getTodosLosPedidos() {
        return new ArrayList<>(this.pedidos.values());
    }

    @Override
    public List<Pedido> getPedidosPorCliente(int idCliente) {
        List<Pedido> pedidosCliente = new ArrayList<>();
        for (Pedido pedido : this.pedidos.values()) {
            if (pedido.getCliente() != null && pedido.getCliente().getIdCliente() == idCliente) {
                pedidosCliente.add(pedido);
            }
        }
        return pedidosCliente;
    }

    @Override
    public List<Pedido> getPedidosPorEstado(String estado) {
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío para filtrar.");
        }
        List<Pedido> pedidosEstado = new ArrayList<>();
        for (Pedido pedido : this.pedidos.values()) {
            if (pedido.getEstado() != null && pedido.getEstado().equalsIgnoreCase(estado)) {
                pedidosEstado.add(pedido);
            }
        }
        return pedidosEstado;
    }

    @Override
    public void procesarPedidos() {
        // Placeholder implementation
        System.out.println("Procesando todos los pedidos...");
        // Future logic: iterate through pedidos and trigger state changes or other actions.
    }

    @Override
    public String generarInforme(String filtroFecha, String filtroEstado) {
        StringBuilder informe = new StringBuilder();
        // Use static getters from SistemaFacade for dealership details
        informe.append(SistemaFacade.getNombreConcesionaria()).append(" - CUIT: ").append(SistemaFacade.getCuitConcesionaria()).append("\n");
        informe.append("Informe de Pedidos\n");
        informe.append("========================================\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaFiltro = null;
        if (filtroFecha != null && !filtroFecha.isEmpty()) {
            try {
                fechaFiltro = sdf.parse(filtroFecha);
            } catch (java.text.ParseException e) {
                informe.append("Formato de fecha inválido: ").append(filtroFecha).append(". Se ignora filtro de fecha.\n");
            }
        }

        boolean hasEstadoFilter = filtroEstado != null && !filtroEstado.isEmpty();

        for (Pedido pedido : this.pedidos.values()) {
            boolean matches = true;
            if (fechaFiltro != null) {
                // Compare dates by formatting pedido's date to string and comparing,
                // or by setting time components to zero for both dates.
                // For simplicity, let's assume a match if the day is the same.
                // A more robust solution would involve a date range.
                String pedidoFechaStr = sdf.format(pedido.getFechaDeCreacion());
                if (!pedidoFechaStr.equals(filtroFecha)) {
                    matches = false;
                }
            }
            if (hasEstadoFilter && matches) {
                if (pedido.getEstado() == null || !pedido.getEstado().equalsIgnoreCase(filtroEstado)) {
                    matches = false;
                }
            }

            if (matches) {
                informe.append("Pedido Nro: ").append(pedido.getNumeroDePedido()).append("\n");
                informe.append("  Fecha Creación: ").append(sdf.format(pedido.getFechaDeCreacion())).append("\n");
                informe.append("  Cliente: ").append(pedido.getCliente() != null ? pedido.getCliente().getNombre() : "N/A").append("\n");
                informe.append("  Vehículo: ").append(pedido.getVehiculo() != null ? pedido.getVehiculo().getMarca() + " " + pedido.getVehiculo().getModelo() : "N/A").append("\n");
                informe.append("  Costo Total: ").append(String.format("%.2f", pedido.getCostoTotal())).append("\n");
                informe.append("  Estado: ").append(pedido.getEstado() != null ? pedido.getEstado() : "N/A").append("\n");
                informe.append("----------------------------------------\n");
            }
        }
        if (informe.length() == 0) { // Should not happen due to header
            informe.append("No hay pedidos que coincidan con los filtros.\n");
        }
        return informe.toString();
    }

    // The old getPedio() method is removed as it's replaced by getPedido(int idPedido).

    @Override
    public String generarInformeTotalesPorFormaDePago() {
        StringBuilder informe = new StringBuilder();
        // Header
        informe.append(SistemaFacade.getNombreConcesionaria()).append(" - CUIT: ").append(SistemaFacade.getCuitConcesionaria()).append("\n");
        informe.append("=====================================\n");
        informe.append("Informe de Totales por Forma de Pago\n");
        informe.append("-------------------------------------\n");
        informe.append(String.format("%-15s | %-18s | %-15s\n", "Forma de Pago", "Cantidad Pedidos", "Total Recaudado"));
        informe.append("-------------------------------------\n");

        Map<String, Double> totalesPorFormaPago = new HashMap<>();
        Map<String, Integer> cantidadPorFormaPago = new HashMap<>();
        double totalGeneralRecaudado = 0;
        int totalGeneralPedidos = 0;

        for (Pedido pedido : this.pedidos.values()) {
            IFormaDePago formaDePago = pedido.getFormaDePago();
            if (formaDePago != null) {
                String nombreMetodo = formaDePago.getClass().getSimpleName();
                // Capitalize common names if needed, e.g., "Tarjeta" from "TarjetaCredito"
                if (nombreMetodo.contains("Contado")) nombreMetodo = "Contado";
                else if (nombreMetodo.contains("Transferencia")) nombreMetodo = "Transferencia";
                else if (nombreMetodo.contains("Tarjeta")) nombreMetodo = "Tarjeta";
                // Add more specific mappings if class names are more complex

                double costoPedido = pedido.getCostoTotal();
                totalesPorFormaPago.put(nombreMetodo, totalesPorFormaPago.getOrDefault(nombreMetodo, 0.0) + costoPedido);
                cantidadPorFormaPago.put(nombreMetodo, cantidadPorFormaPago.getOrDefault(nombreMetodo, 0) + 1);
                totalGeneralRecaudado += costoPedido;
            }
            totalGeneralPedidos++; // Count all pedidos, even if formaDePago is null for some reason
        }
        
        // Define a list of expected payment methods for ordered printing, including those that might have 0 pedidos
        List<String> formasDePagoConocidas = Arrays.asList("Contado", "Transferencia", "Tarjeta");

        for (String forma : formasDePagoConocidas) {
            int cantidad = cantidadPorFormaPago.getOrDefault(forma, 0);
            double totalRecaudado = totalesPorFormaPago.getOrDefault(forma, 0.0);
            informe.append(String.format("%-15s | %-18d | $%-14.2f\n", forma, cantidad, totalRecaudado));
        }
        
        // It's possible some payment methods exist in pedidos but are not in formasDePagoConocidas
        // Add them if they exist
        for (Map.Entry<String, Integer> entry : cantidadPorFormaPago.entrySet()) {
            if (!formasDePagoConocidas.contains(entry.getKey())) {
                int cantidad = entry.getValue();
                double totalRecaudado = totalesPorFormaPago.getOrDefault(entry.getKey(), 0.0);
                informe.append(String.format("%-15s | %-18d | $%-14.2f\n", entry.getKey(), cantidad, totalRecaudado));
            }
        }


        informe.append("-------------------------------------\n");
        informe.append(String.format("%-15s | %-18d | $%-14.2f\n", "TOTAL GENERAL", totalGeneralPedidos, totalGeneralRecaudado));
        informe.append("=====================================\n");

        return informe.toString();
    }
}
