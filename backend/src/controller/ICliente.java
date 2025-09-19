package controller;

public interface ICliente {
    String getNombre();
    int getIdCliente(); // Added method to get client ID
    // Add other methods that Pedido might need from Cliente
    // For now, getNombre is a placeholder.
    // Based on Pedido's potential needs, we might add:
    // String getEmail();
    // String getDireccion();
}
