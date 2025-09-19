
import java.io.*;
import java.util.*;
import java.util.HashMap; // Explicit import
import java.util.List;    // Explicit import
import java.util.ArrayList; // Explicit import
import java.util.Collection; // Explicit import

// Assuming Vehiculo, DuplicateVehiculoException, VehiculoNotFoundException are in the same package (controller)
// or have been imported if they were in different packages.

/**
 * Manages vehicle inventory, implementing the Singleton pattern.
 * Allows registering, updating, deleting, and retrieving vehicles.
 */
public class GestorVehiculos implements InterfazGestorVehiculos {

    private static GestorVehiculos instancia;
    private HashMap<String, Vehiculo> vehiculos; // Key is numeroChasis

    /**
     * Private constructor for Singleton pattern.
     * Initializes the vehicle storage.
     */
    private GestorVehiculos() {
        this.vehiculos = new HashMap<>();
    }

    /**
     * Provides the global access point to the GestorVehiculos instance.
     * @return The single instance of GestorVehiculos.
     */
    public static GestorVehiculos getInstancia() {
        if (instancia == null) {
            instancia = new GestorVehiculos();
        }
        return instancia;
    }

    /**
     * Registers a new vehicle.
     * @param vehiculo The vehicle to register.
     * @throws DuplicateVehiculoException if a vehicle with the same chassis number already exists.
     */
    @Override
    public void registrarVehiculo(Vehiculo vehiculo) throws DuplicateVehiculoException {
        if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
            throw new IllegalArgumentException("El vehículo o su número de chasis no pueden ser nulos o vacíos.");
        }
        if (this.vehiculos.containsKey(vehiculo.getNumeroChasis())) {
            throw new DuplicateVehiculoException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " ya existe.");
        }
        this.vehiculos.put(vehiculo.getNumeroChasis(), vehiculo);
        System.out.println("Vehículo registrado: " + vehiculo.getMarca() + " " + vehiculo.getModelo() + " (Chasis: " + vehiculo.getNumeroChasis() + ")");
    }

    /**
     * Updates the data of an existing vehicle.
     * @param vehiculo The vehicle with updated data. Its numeroChasis is used as the key.
     * @throws VehiculoNotFoundException if the vehicle to update is not found.
     */
    @Override
    public void actualizarDatosVehiculo(Vehiculo vehiculo) throws VehiculoNotFoundException {
        if (vehiculo == null || vehiculo.getNumeroChasis() == null || vehiculo.getNumeroChasis().isEmpty()) {
            throw new IllegalArgumentException("El vehículo o su número de chasis no pueden ser nulos o vacíos para actualizar.");
        }
        if (!this.vehiculos.containsKey(vehiculo.getNumeroChasis())) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + vehiculo.getNumeroChasis() + " no encontrado para actualizar.");
        }
        this.vehiculos.put(vehiculo.getNumeroChasis(), vehiculo);
        System.out.println("Vehículo actualizado: " + vehiculo.getMarca() + " " + vehiculo.getModelo() + " (Chasis: " + vehiculo.getNumeroChasis() + ")");
    }

    /**
     * Eliminates a vehicle based on its chassis number.
     * @param numeroChasis The chassis number of the vehicle to eliminate.
     * @throws VehiculoNotFoundException if the vehicle with the given chassis number is not found.
     */
    @Override
    public void eliminarVehiculo(String numeroChasis) throws VehiculoNotFoundException {
        if (numeroChasis == null || numeroChasis.isEmpty()) {
            throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para eliminar.");
        }
        if (!this.vehiculos.containsKey(numeroChasis)) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado para eliminar.");
        }
        Vehiculo vRemovido = this.vehiculos.remove(numeroChasis);
        System.out.println("Vehículo eliminado: " + vRemovido.getMarca() + " " + vRemovido.getModelo() + " (Chasis: " + numeroChasis + ")");
    }

    /**
     * Retrieves a vehicle by its chassis number.
     * @param numeroChasis The chassis number of the vehicle to retrieve.
     * @return The found vehicle.
     * @throws VehiculoNotFoundException if the vehicle with the given chassis number is not found.
     */
    @Override
    public Vehiculo getVehiculo(String numeroChasis) throws VehiculoNotFoundException {
        if (numeroChasis == null || numeroChasis.isEmpty()) {
            throw new IllegalArgumentException("El número de chasis no puede ser nulo o vacío para buscar.");
        }
        Vehiculo vehiculo = this.vehiculos.get(numeroChasis);
        if (vehiculo == null) {
            throw new VehiculoNotFoundException("Vehículo con chasis " + numeroChasis + " no encontrado.");
        }
        return vehiculo;
    }

    /**
     * Retrieves all registered vehicles.
     * @return A new ArrayList containing all vehicles.
     */
    @Override
    public Collection<Vehiculo> getTodosLosVehiculos() {
        return new ArrayList<>(this.vehiculos.values());
    }

    // Old placeholder methods like agregarVehiculo(), eliminarVehiculo() (no params),
    // actualizarVehiculo(), getVehiculos() are removed as their functionality
    // is now covered by the specific methods defined in InterfazGestorVehiculos
    // and implemented above.
}