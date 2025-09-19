package com.grupo9.sistemaConcesionaria.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grupo9.sistemaConcesionaria.model.Pedido;
import com.grupo9.sistemaConcesionaria.model.EstadoPedido;
import com.grupo9.sistemaConcesionaria.model.FormaDePago;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PedidoJsonRepository - Repositorio JSON para entidades Pedido
 * Maneja la persistencia de pedidos en archivo pedidos.json
 */
@Repository
public class PedidoJsonRepository extends BaseJsonRepository<Pedido, Long> {

    public PedidoJsonRepository() {
        super(
            "pedidos.json",
            new TypeReference<List<Pedido>>() {},
            Pedido::getIdPedido,
            pedido -> pedido // El setter se maneja en generateId
        );
    }

    @Override
    protected Pedido generateId(Pedido pedido, List<Pedido> existingEntities) {
        // Generar ID auto-incremental
        Long maxId = existingEntities.stream()
                .mapToLong(Pedido::getIdPedido)
                .max()
                .orElse(0L);
        
        pedido.setIdPedido(maxId + 1);
        return pedido;
    }

    /**
     * Busca pedido por número de pedido
     * @param numeroPedido Número del pedido
     * @return Optional con el pedido encontrado
     */
    public Optional<Pedido> findByNumeroDePedido(String numeroPedido) {
        return findAll().stream()
                .filter(p -> numeroPedido.equals(p.getNumeroDePedido()))
                .findFirst();
    }

    /**
     * Busca pedidos por estado
     * @param estado Estado del pedido
     * @return Lista de pedidos en el estado especificado
     */
    public List<Pedido> findByEstadoActual(EstadoPedido estado) {
        return findAll().stream()
                .filter(p -> estado.equals(p.getEstadoActual()))
                .toList();
    }

    /**
     * Busca pedidos por cliente
     * @param clienteId ID del cliente
     * @return Lista de pedidos del cliente
     */
    public List<Pedido> findByClienteId(Long clienteId) {
        return findAll().stream()
                .filter(p -> p.getCliente() != null && clienteId.equals(p.getCliente().getIdCliente()))
                .toList();
    }

    /**
     * Busca pedidos por vehículo
     * @param vehiculoChasis Número de chasis del vehículo
     * @return Lista de pedidos para el vehículo
     */
    public List<Pedido> findByVehiculoChasis(String vehiculoChasis) {
        return findAll().stream()
                .filter(p -> p.getVehiculo() != null && vehiculoChasis.equals(p.getVehiculo().getNumeroChasis()))
                .toList();
    }

    /**
     * Busca pedidos por forma de pago
     * @param formaPago Forma de pago
     * @return Lista de pedidos con la forma de pago especificada
     */
    public List<Pedido> findByFormaDePago(FormaDePago formaPago) {
        return findAll().stream()
                .filter(p -> formaPago.equals(p.getFormaDePago()))
                .toList();
    }

    /**
     * Busca pedidos por rango de fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de pedidos en el rango de fechas
     */
    public List<Pedido> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return findAll().stream()
                .filter(p -> p.getFechaCreacion().isAfter(fechaInicio) && p.getFechaCreacion().isBefore(fechaFin))
                .toList();
    }

    /**
     * Busca pedidos por rango de costo total
     * @param costoMin Costo mínimo
     * @param costoMax Costo máximo
     * @return Lista de pedidos en el rango de costo
     */
    public List<Pedido> findByCostoTotalBetween(double costoMin, double costoMax) {
        return findAll().stream()
                .filter(p -> p.getCostoTotal() >= costoMin && p.getCostoTotal() <= costoMax)
                .toList();
    }

    /**
     * Busca pedidos por área responsable actual
     * @param area Área responsable
     * @return Lista de pedidos en el área especificada
     */
    public List<Pedido> findByAreaResponsableActual(String area) {
        return findAll().stream()
                .filter(p -> area.equals(p.getAreaResponsableActual()))
                .toList();
    }

    /**
     * Cuenta pedidos por estado
     * @param estado Estado del pedido
     * @return Cantidad de pedidos en el estado
     */
    public long countByEstadoActual(EstadoPedido estado) {
        return findAll().stream()
                .filter(p -> estado.equals(p.getEstadoActual()))
                .count();
    }

    /**
     * Cuenta pedidos por cliente
     * @param clienteId ID del cliente
     * @return Cantidad de pedidos del cliente
     */
    public long countByClienteId(Long clienteId) {
        return findAll().stream()
                .filter(p -> p.getCliente() != null && clienteId.equals(p.getCliente().getIdCliente()))
                .count();
    }

    /**
     * Calcula el total de ventas (suma de costos totales)
     * @return Total de ventas
     */
    public double sumCostoTotal() {
        return findAll().stream()
                .mapToDouble(Pedido::getCostoTotal)
                .sum();
    }

    /**
     * Calcula el total de ventas por estado
     * @param estado Estado del pedido
     * @return Total de ventas para el estado
     */
    public double sumCostoTotalByEstado(EstadoPedido estado) {
        return findAll().stream()
                .filter(p -> estado.equals(p.getEstadoActual()))
                .mapToDouble(Pedido::getCostoTotal)
                .sum();
    }
} 