package controller;

public interface IVehiculo {
    String getMarca();
    String getModelo();
    double getPrecioBase();
    // Add other methods that Pedido might need from Vehiculo
    // For example:
    // String getIdentificador(); // Like numeroChasis
}
