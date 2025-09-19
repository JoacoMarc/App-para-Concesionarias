
import java.io.*;
import java.util.*;

/**
 * 
 */
// Added necessary imports for exceptions and collections
import java.util.Collection; // Or java.util.List
// Assuming Vehiculo, DuplicateVehiculoException, VehiculoNotFoundException are in the same package (controller)
// or have been imported if they were in different packages.

public interface InterfazGestorVehiculos {

    /**
     * Registers a new vehicle.
     * @param vehiculo The vehicle to register.
     * @throws DuplicateVehiculoException if a vehicle with the same chassis number already exists.
     */
    void registrarVehiculo(Vehiculo vehiculo) throws DuplicateVehiculoException;

    /**
     * Updates the data of an existing vehicle.
     * @param vehiculo The vehicle with updated data.
     * @throws VehiculoNotFoundException if the vehicle to update is not found.
     */
    void actualizarDatosVehiculo(Vehiculo vehiculo) throws VehiculoNotFoundException;

    /**
     * Eliminates a vehicle based on its chassis number.
     * @param numeroChasis The chassis number of the vehicle to eliminate.
     * @throws VehiculoNotFoundException if the vehicle with the given chassis number is not found.
     */
    void eliminarVehiculo(String numeroChasis) throws VehiculoNotFoundException;

    /**
     * Retrieves a vehicle by its chassis number.
     * @param numeroChasis The chassis number of the vehicle to retrieve.
     * @return The found vehicle.
     * @throws VehiculoNotFoundException if the vehicle with the given chassis number is not found.
     */
    Vehiculo getVehiculo(String numeroChasis) throws VehiculoNotFoundException;

    /**
     * Retrieves all registered vehicles.
     * @return A collection of all vehicles.
     */
    Collection<Vehiculo> getTodosLosVehiculos();

    // Old methods like agregarVehiculo(), eliminarVehiculo(), actualizarVehiculo(), getVehiculos()
    // have been removed as their functionalities are covered by the new specific methods.
    // The old eliminarVehiculo(Vehiculo vehiculo) is replaced by eliminarVehiculo(String numeroChasis).
}