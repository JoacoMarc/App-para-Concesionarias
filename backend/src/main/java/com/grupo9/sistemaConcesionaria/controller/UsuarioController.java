package com.grupo9.sistemaConcesionaria.controller;

import com.grupo9.sistemaConcesionaria.dto.AdministradorRequestDTO;
import com.grupo9.sistemaConcesionaria.dto.VendedorRequestDTO;
import com.grupo9.sistemaConcesionaria.dto.ClienteRequestDTO;
import com.grupo9.sistemaConcesionaria.model.Administrador;
import com.grupo9.sistemaConcesionaria.model.Usuario;
import com.grupo9.sistemaConcesionaria.model.Vendedor;
import com.grupo9.sistemaConcesionaria.model.Cliente;
import com.grupo9.sistemaConcesionaria.repository.UsuarioRepository;
import com.grupo9.sistemaConcesionaria.service.GestorDeUsuarios;
import com.grupo9.sistemaConcesionaria.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador REST para operaciones de usuarios del sistema
 * Maneja Clientes, Administradores y Vendedores
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private GestorDeUsuarios gestorDeUsuarios;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // === CLIENTES ===

    @PostMapping("/clientes")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody ClienteRequestDTO request) {
        try {
            logger.info("Creando cliente: {}", request.getMail());

            // Verificar si ya existe un usuario con ese email
            if (usuarioRepository.findByMail(request.getMail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body("Ya existe un usuario con el email: " + request.getMail());
            }

            // Crear cliente con contraseña hasheada
            Cliente cliente = new Cliente(
                    request.getNombre(),
                    request.getApellido(),
                    request.getMail(),
                    passwordEncoder.encode(request.getPassword()), // Hashear contraseña
                    request.getDocumento(),
                    request.getTelefono()
            );

            // Configurar datos adicionales
            cliente.setDireccion(request.getDireccion());
            cliente.setRazonSocial(request.getRazonSocial());
            cliente.setCuitCuil(request.getCuitCuil());
            cliente.setDireccionFacturacion(request.getDireccionFacturacion());
            cliente.setCiudadFacturacion(request.getCiudadFacturacion());
            cliente.setProvinciaFacturacion(request.getProvinciaFacturacion());
            cliente.setCodigoPostalFacturacion(request.getCodigoPostalFacturacion());
            cliente.setTipoFacturacion(request.getTipoFacturacion());
            cliente.setRequiereFacturaA(request.isRequiereFacturaA());

            Cliente clienteGuardado = usuarioRepository.save(cliente);
            logger.info("Cliente creado exitosamente: {}", clienteGuardado.getMail());

            return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);

        } catch (Exception e) {
            logger.error("Error al crear cliente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        try {
            List<Cliente> clientes = usuarioRepository.findByRol(com.grupo9.sistemaConcesionaria.model.RolUsuario.CLIENTE)
                    .stream()
                    .map(u -> (Cliente) u)
                    .toList();
            
            logger.info("Obtenidos {} clientes desde BD", clientes.size());
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            logger.error("Error al obtener clientes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clientes/{documento}")
    public ResponseEntity<?> obtenerCliente(@PathVariable String documento) {
        try {
            Cliente cliente = gestorDeUsuarios.getCliente(documento);
            return ResponseEntity.ok(cliente);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Cliente no encontrado", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    @PutMapping("/clientes")
    public ResponseEntity<?> actualizarCliente(@Valid @RequestBody Cliente cliente) {
        try {
            gestorDeUsuarios.actualizarCliente(cliente);
            return ResponseEntity.ok(cliente);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Cliente no encontrado", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    @DeleteMapping("/clientes/{documento}")
    public ResponseEntity<?> eliminarCliente(@PathVariable String documento) {
        try {
            gestorDeUsuarios.eliminarCliente(documento);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cliente eliminado exitosamente");
            response.put("documento", documento);
            return ResponseEntity.ok(response);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Cliente no encontrado", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    // === USUARIOS DEL SISTEMA - USANDO JPA ===

    @PostMapping("/sistema")
    public ResponseEntity<?> registrarUsuarioSistema(@Valid @RequestBody Usuario usuario) {
        try {
            if (usuarioRepository.existsByMail(usuario.getMail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(createErrorResponse("Error: Usuario duplicado", "Ya existe un usuario con este email"));
            }
            
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    @PostMapping("/administradores")
    public ResponseEntity<?> crearAdministrador(@Valid @RequestBody AdministradorRequestDTO request) {
        try {
            logger.info("Creando administrador: {}", request.getMail());

            // Verificar si ya existe un usuario con ese email
            if (usuarioRepository.findByMail(request.getMail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body("Ya existe un usuario con el email: " + request.getMail());
            }

            // Crear administrador con contraseña hasheada
            Administrador admin = new Administrador(
                    request.getNombre(),
                    request.getApellido(),
                    request.getMail(),
                    passwordEncoder.encode(request.getPassword()), // Hashear contraseña
                    request.getArea()
            );
            admin.setTelefono(request.getTelefono());

            Administrador adminGuardado = usuarioRepository.save(admin);
            logger.info("Administrador creado exitosamente: {}", adminGuardado.getMail());

            return ResponseEntity.status(HttpStatus.CREATED).body(adminGuardado);

        } catch (Exception e) {
            logger.error("Error al crear administrador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping("/administradores")
    public ResponseEntity<List<Administrador>> obtenerAdministradores() {
        try {
            List<Administrador> administradores = usuarioRepository.findByRol(com.grupo9.sistemaConcesionaria.model.RolUsuario.ADMINISTRADOR)
                    .stream()
                    .map(u -> (Administrador) u)
                    .toList();
            
            logger.info("Obtenidos {} administradores", administradores.size());
            return ResponseEntity.ok(administradores);
        } catch (Exception e) {
            logger.error("Error al obtener administradores: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/vendedores")
    public ResponseEntity<?> crearVendedor(@Valid @RequestBody VendedorRequestDTO request) {
        try {
            logger.info("Creando vendedor: {}", request.getMail());

            // Verificar si ya existe un usuario con ese email
            if (usuarioRepository.findByMail(request.getMail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body("Ya existe un usuario con el email: " + request.getMail());
            }

            // Crear vendedor con contraseña hasheada
            Vendedor vendedor = new Vendedor(
                    request.getNombre(),
                    request.getApellido(),
                    request.getMail(),
                    passwordEncoder.encode(request.getPassword()), // Hashear contraseña
                    request.getSucursal()
            );
            vendedor.setTelefono(request.getTelefono());

            Vendedor vendedorGuardado = usuarioRepository.save(vendedor);
            logger.info("Vendedor creado exitosamente: {}", vendedorGuardado.getMail());

            return ResponseEntity.status(HttpStatus.CREATED).body(vendedorGuardado);

        } catch (Exception e) {
            logger.error("Error al crear vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping("/vendedores")
    public ResponseEntity<List<Vendedor>> obtenerVendedores() {
        try {
            List<Vendedor> vendedores = usuarioRepository.findByRol(com.grupo9.sistemaConcesionaria.model.RolUsuario.VENDEDOR)
                    .stream()
                    .map(u -> (Vendedor) u)
                    .toList();
            
            logger.info("Obtenidos {} vendedores", vendedores.size());
            return ResponseEntity.ok(vendedores);
        } catch (Exception e) {
            logger.error("Error al obtener vendedores: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sistema/{mail}")
    public ResponseEntity<?> obtenerUsuarioSistema(@PathVariable String mail) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByMail(mail);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Usuario no encontrado", "No se encontró usuario con email: " + mail));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    @GetMapping("/sistema")
    public ResponseEntity<?> obtenerTodosLosUsuariosSistema() {
        try {
            return ResponseEntity.ok(usuarioRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    @PutMapping("/sistema")
    public ResponseEntity<?> actualizarUsuarioSistema(@Valid @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> usuarioExistente = usuarioRepository.findByMail(usuario.getMail());
            if (usuarioExistente.isPresent()) {
                usuario.setIdUsuario(usuarioExistente.get().getIdUsuario());
                Usuario usuarioActualizado = usuarioRepository.save(usuario);
                return ResponseEntity.ok(usuarioActualizado);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Usuario no encontrado", "No se encontró usuario con email: " + usuario.getMail()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    @DeleteMapping("/sistema/{mail}")
    public ResponseEntity<?> eliminarUsuarioSistema(@PathVariable String mail) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByMail(mail);
            if (usuario.isPresent()) {
                usuarioRepository.delete(usuario.get());
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Usuario eliminado exitosamente");
                response.put("mail", mail);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Usuario no encontrado", "No se encontró usuario con email: " + mail));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error interno", e.getMessage()));
        }
    }

    // === ENDPOINTS DE UTILIDAD ===

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, String>> obtenerEstadisticas() {
        Map<String, String> estadisticas = new HashMap<>();
        long totalUsuarios = usuarioRepository.count();
        long totalClientes = gestorDeUsuarios.getTodosLosClientes().size();
        estadisticas.put("informacion", String.format("Sistema - Clientes: %d, Usuarios: %d", totalClientes, totalUsuarios));
        return ResponseEntity.ok(estadisticas);
    }

    @PostMapping("/limpiar")
    public ResponseEntity<Map<String, String>> limpiarDatos() {
        usuarioRepository.deleteAll();
        gestorDeUsuarios.limpiarDatos();
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Datos limpiados exitosamente");
        return ResponseEntity.ok(response);
    }

    // === MÉTODOS AUXILIARES ===

    private Map<String, String> createErrorResponse(String titulo, String detalle) {
        Map<String, String> error = new HashMap<>();
        error.put("error", titulo);
        error.put("detalle", detalle);
        return error;
    }
} 