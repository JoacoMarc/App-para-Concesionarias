package controller;

import java.util.ArrayList;
import java.util.List;

public class SubjectObservadorEstado implements InterfazObervadorEstado {
    private List<Observador> observadores = new ArrayList<>();
    private String estadoActual; // The state to notify observers about

    public void setEstadoActual(String estado) {
        this.estadoActual = estado;
        notificar(); // Notify when state changes
    }

    @Override
    public void adjuntar(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void quitar(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar() {
        for (Observador observador : observadores) {
            observador.actualizar(estadoActual);
        }
    }
}
