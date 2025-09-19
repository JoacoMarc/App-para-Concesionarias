package controller;

import java.util.HashMap; // Or appropriate Map/List type

public interface InterfazGestorImpuestos {
    void agregarImpuesto(Impuesto impuesto); // Assuming Impuesto is the class for tax data
    void eliminarImpuesto(int idImpuesto);
    void actualizarImpuesto(Impuesto impuesto);
    Impuesto getImpuesto(int idImpuesto); // Or Map/List getImpuestos();
    // Define methods based on GestorImpuestos.java's public API
}
