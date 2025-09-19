package com.grupo9.sistemaConcesionaria.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grupo9.sistemaConcesionaria.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ClienteJsonRepository - Repositorio JSON para entidades Cliente
 * Maneja la persistencia de clientes en archivo clientes.json
 * Actualizado para trabajar con la nueva estructura de Cliente
 */
@Repository
public class ClienteJsonRepository extends BaseJsonRepository<Cliente, Long> {

    public ClienteJsonRepository() {
        super(
            "clientes.json",
            new TypeReference<List<Cliente>>() {},
            Cliente::getId,
            cliente -> cliente // El setter se maneja en generateId
        );
    }

    @Override
    protected Cliente generateId(Cliente cliente, List<Cliente> existingEntities) {
        // Generar ID auto-incremental
        Long maxId = existingEntities.stream()
                .mapToLong(c -> c.getId() != null ? c.getId() : 0L)
                .max()
                .orElse(0L);
        
        Long newId = maxId + 1;
        cliente.setId(newId); // Esto llamará a setIdUsuario(newId)
        
        // Asegurarse de que el idUsuario esté establecido correctamente
        if (cliente.getIdUsuario() == null) {
            cliente.setIdUsuario(newId);
        }
        
        return cliente;
    }

    /**
     * Busca cliente por documento
     * @param documento Documento del cliente
     * @return Optional con el cliente encontrado
     */
    public Optional<Cliente> findByDocumento(String documento) {
        return findAll().stream()
                .filter(c -> documento.equals(c.getDocumento()))
                .findFirst();
    }

    /**
     * Busca clientes por nombre (parcial, case insensitive)
     * @param nombre Nombre a buscar
     * @return Lista de clientes que coinciden
     */
    public List<Cliente> findByNombreContaining(String nombre) {
        return findAll().stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    /**
     * Busca clientes por apellido (parcial, case insensitive)
     * @param apellido Apellido a buscar
     * @return Lista de clientes que coinciden
     */
    public List<Cliente> findByApellidoContaining(String apellido) {
        return findAll().stream()
                .filter(c -> c.getApellido().toLowerCase().contains(apellido.toLowerCase()))
                .toList();
    }

    /**
     * Busca cliente por email
     * @param correo Correo electrónico
     * @return Optional con el cliente encontrado
     */
    public Optional<Cliente> findByCorreoElectronico(String correo) {
        return findAll().stream()
                .filter(c -> correo.equals(c.getCorreoElectronico()))
                .findFirst();
    }

    /**
     * Busca clientes por teléfono
     * @param telefono Teléfono del cliente
     * @return Optional con el cliente encontrado
     */
    public Optional<Cliente> findByTelefono(String telefono) {
        return findAll().stream()
                .filter(c -> telefono.equals(c.getTelefono()))
                .findFirst();
    }

    /**
     * Verifica si existe un cliente con el documento dado
     * @param documento Documento a verificar
     * @return true si existe, false si no
     */
    public boolean existsByDocumento(String documento) {
        return findByDocumento(documento).isPresent();
    }

    /**
     * Verifica si existe un cliente con el email dado
     * @param correo Email a verificar
     * @return true si existe, false si no
     */
    public boolean existsByCorreoElectronico(String correo) {
        return findByCorreoElectronico(correo).isPresent();
    }
} 