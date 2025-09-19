
import java.io.*;
import java.util.*;
import java.util.stream.Collectors; // For getVehiculosDisponibles

// Assuming Vehiculo, DuplicateVehiculoException, VehiculoNotFoundException are in controller package or imported

/**
 * Manages the catalog of available vehicles, implementing the Singleton pattern.
 */
public class Catalogo {

    private static Catalogo instancia;
    private HashMap<String, Vehiculo> disponibles; // Key: numeroChasis

    /**
     * Private constructor for Singleton pattern.
     * Initializes the vehicle storage.
     */
    private Catalogo() {
        this.disponibles = new HashMap<>();
    }

    /**
     * Provides the global access point to the Catalogo instance.
     * @return The single instance of Catalogo.
     */
    public static Catalogo getInstancia() {
        if (instancia == null) {
            instancia = new Catalogo();
        }
        return instancia;
    }

    /**
     * Adds a vehicle to the catalog.
     * @param vehiculo The vehicle to add.
     * @throws DuplicateVehiculoException if a vehicle with the same chassis number already exists.
     */
    public void agregarVehiculoAlCatalogo(Vehiculo vehiculo) throws DuplicateVehiculoException {
        if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
            throw new IllegalArgumentException("El vehículo o su número de chasis no pueden ser nulos o vacíos.");
        }
        if (this.disponibles.containsKey(vehiculo.getNumeroChasis())) {
            throw new DuplicateVehiculoException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " ya existe en el catálogo.");
        }
        this.disponibles.put(vehiculo.getNumeroChasis(), vehiculo);
        System.out.println("Vehículo agregado al catálogo: " + vehiculo.getMarca() + " " + vehiculo.getModelo() + " (Chasis: " + vehiculo.getNumeroChasis() + ")");
    }

    /**
     * Removes a vehicle from the catalog by its chassis number.
     * @param numeroChasis The chassis number of the vehicle to remove.
     * @throws VehiculoNotFoundException if the vehicle is not found in the catalog.
     */
    public void eliminarVehiculoDelCatalogo(String numeroChasis) throws VehiculoNotFoundException {
        if (numeroChasis == null || numeroChasis.isEmpty()) {
            throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para eliminar del catálogo.");
        }
        Vehiculo vehiculoRemovido = this.disponibles.remove(numeroChasis);
        if (vehiculoRemovido == null) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado en el catálogo.");
        }
        System.out.println("Vehículo eliminado del catálogo: " + vehiculoRemovido.getMarca() + " " + vehiculoRemovido.getModelo() + " (Chasis: " + numeroChasis + ")");
    }

    /**
     * Retrieves a vehicle from the catalog by its chassis number.
     * @param numeroChasis The chassis number of the vehicle to retrieve.
     * @return The found vehicle.
     * @throws VehiculoNotFoundException if the vehicle is not found in the catalog.
     */
    public Vehiculo getVehiculoDelCatalogo(String numeroChasis) throws VehiculoNotFoundException {
        if (numeroChasis == null || numeroChasis.isEmpty()) {
            throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para buscar en el catálogo.");
        }
        Vehiculo vehiculo = this.disponibles.get(numeroChasis);
        if (vehiculo == null) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado en el catálogo.");
        }
        return vehiculo;
    }

    /**
     * Returns a list of vehicles currently available for sale.
     * @return A list of vehicles where `disponibleVenta` is true.
     */
    public List<Vehiculo> getVehiculosDisponibles() {
        return this.disponibles.values().stream()
                .filter(Vehiculo::isDisponibleVenta)
                .collect(Collectors.toList());
    }

    // Old placeholder methods agregarVehiculo() and eliminarVehiculo() are removed.
}