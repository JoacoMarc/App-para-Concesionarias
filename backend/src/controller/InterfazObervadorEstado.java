package controller;

public interface InterfazObervadorEstado {
    void adjuntar(Observador observador);
    void quitar(Observador observador); // Good practice to have a detach method
    void notificar();
}
