package com.grupo9.sistemaConcesionaria.model.state;

import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.exception.PedidoNotFoundException;

/**
 * Patrón State - Interfaz base para los estados del proceso de pedidos
 * Define las operaciones que cada estado debe implementar
 */
public interface EstadoPedidoState {


    void procesar(Pedido pedido) throws PedidoNotFoundException;

    boolean puedeAvanzar(Pedido pedido);

    EstadoPedidoState getSiguienteEstado();

    String getNombreEstado();
    String getDescripcionEstado();

    boolean esFinal();
} 