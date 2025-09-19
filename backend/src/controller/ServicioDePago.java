package controller;

public interface ServicioDePago {
    boolean procesarPago(IFormaDePago formaDePago, double monto);
}
