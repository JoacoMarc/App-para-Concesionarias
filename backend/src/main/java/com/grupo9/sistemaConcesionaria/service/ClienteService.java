package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Cliente;
import com.grupo9.sistemaConcesionaria.repository.ClienteJsonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * ClienteService - Servicio para gestión de clientes con persistencia JSON
 * Actualizado para trabajar con la nueva estructura de Cliente
 */
@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);
    
    @Autowired
    private ClienteJsonRepository clienteRepository;

    /**
     * Constructor que inicializa con datos de prueba si no existen
     */
    @jakarta.annotation.PostConstruct
    public void init() {
        // Solo inicializar si no hay datos existentes
        if (clienteRepository.count() == 0) {
            inicializarDatosPrueba();
        } else {
            logger.info("Datos existentes cargados desde JSON: {} clientes", clienteRepository.count());
        }
    }

    /**
     * Obtiene un cliente por ID
     * @param id ID del cliente
     * @return El cliente encontrado o null
     */
    public Cliente getCliente(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    /**
     * Registra un nuevo cliente
     * @param cliente Cliente a registrar
     * @return Cliente registrado
     */
    public Cliente registrarCliente(Cliente cliente) {
        Cliente clienteGuardado = clienteRepository.save(cliente);
        logger.info("Cliente registrado: {} {} (Doc: {})", clienteGuardado.getNombre(), clienteGuardado.getApellido(), clienteGuardado.getDocumento());
        return clienteGuardado;
    }

    /**
     * Actualiza un cliente existente
     * @param cliente Cliente a actualizar
     * @return Cliente actualizado
     */
    public Cliente actualizarCliente(Cliente cliente) {
        if (cliente.getId() == null || !clienteRepository.existsById(cliente.getId())) {
            throw new IllegalArgumentException("Cliente no existe para actualizar");
        }
        
        Cliente clienteActualizado = clienteRepository.save(cliente);
        logger.info("Cliente actualizado: {} {} (Doc: {})", clienteActualizado.getNombre(), clienteActualizado.getApellido(), clienteActualizado.getDocumento());
        return clienteActualizado;
    }

    /**
     * Elimina un cliente por ID
     * @param id ID del cliente a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            logger.info("Cliente eliminado con ID: {}", id);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los clientes
     * @return Lista de clientes
     */
    public List<Cliente> getTodosLosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Busca clientes por documento
     * @param documento Documento del cliente
     * @return Cliente encontrado o null
     */
    public Cliente getClientePorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento).orElse(null);
    }

    /**
     * Busca cliente por email
     * @param correo Email del cliente
     * @return Cliente encontrado o null
     */
    public Cliente getClientePorCorreo(String correo) {
        return clienteRepository.findByCorreoElectronico(correo).orElse(null);
    }

    /**
     * Busca clientes por nombre (búsqueda parcial)
     * @param nombre Nombre a buscar
     * @return Lista de clientes que coinciden
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContaining(nombre);
    }

    /**
     * Busca clientes por apellido (búsqueda parcial)
     * @param apellido Apellido a buscar
     * @return Lista de clientes que coinciden
     */
    public List<Cliente> buscarPorApellido(String apellido) {
        return clienteRepository.findByApellidoContaining(apellido);
    }

    /**
     * Verifica si existe un cliente con el documento dado
     * @param documento Documento a verificar
     * @return true si existe, false si no
     */
    public boolean existeClienteConDocumento(String documento) {
        return clienteRepository.existsByDocumento(documento);
    }

    /**
     * Verifica si existe un cliente con el email dado
     * @param correo Email a verificar
     * @return true si existe, false si no
     */
    public boolean existeClienteConCorreo(String correo) {
        return clienteRepository.existsByCorreoElectronico(correo);
    }

    /**
     * Obtiene la cantidad total de clientes
     * @return Número total de clientes
     */
    public long getCantidadTotal() {
        return clienteRepository.count();
    }

    /**
     * Inicializa datos de prueba
     */
    private void inicializarDatosPrueba() {
        try {
            // Cliente 1
            Cliente cliente1 = new Cliente();
            cliente1.setNombre("Juan");
            cliente1.setApellido("Pérez");
            cliente1.setDocumento("12345678");
            cliente1.setCorreoElectronico("juan.perez@email.com");
            cliente1.setTelefono("11-1234-5678");
            cliente1.setDireccion("Av. Corrientes 1234, CABA");

            // Cliente 2
            Cliente cliente2 = new Cliente();
            cliente2.setNombre("María");
            cliente2.setApellido("González");
            cliente2.setDocumento("87654321");
            cliente2.setCorreoElectronico("maria.gonzalez@email.com");
            cliente2.setTelefono("11-8765-4321");
            cliente2.setDireccion("San Martín 567, Buenos Aires");

            clienteRepository.save(cliente1);
            clienteRepository.save(cliente2);

            logger.info("Datos de prueba inicializados: {} clientes", clienteRepository.count());
            
        } catch (Exception e) {
            logger.warn("Error inicializando datos de prueba: {}", e.getMessage());
        }
    }
} 