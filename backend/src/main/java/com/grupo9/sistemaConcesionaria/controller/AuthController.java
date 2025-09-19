package com.grupo9.sistemaConcesionaria.controller;

import com.grupo9.sistemaConcesionaria.service.JwtUserDetailsService;
import com.grupo9.sistemaConcesionaria.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de autenticación
 * Maneja login y autenticación por roles para el sistema de concesionaria
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * Endpoint para autenticación de usuarios
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Intento de login para email: {}", loginRequest.getEmail());
            
            // Verificar si el usuario existe
            if (!userDetailsService.userExists(loginRequest.getEmail())) {
                logger.warn("Usuario no encontrado: {}", loginRequest.getEmail());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Usuario no encontrado");
                errorResponse.put("mensaje", "El usuario no existe en el sistema");
                return ResponseEntity.status(401).body(errorResponse);
            }
            
            logger.info("Usuario encontrado, intentando autenticación...");
            
            // Autenticar usuario
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );

            logger.info("Autenticación exitosa para: {}", loginRequest.getEmail());

            // Cargar detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Generar token JWT
            String token = jwtTokenUtil.generateToken(userDetails);

            // Obtener rol del usuario
            String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", userDetails.getUsername());
            response.put("rol", role);
            response.put("mensaje", "Autenticación exitosa");
            
            // Agregar permisos específicos por rol
            response.put("permisos", getPermisosPorRol(role));

            logger.info("Login exitoso para usuario: {} con rol: {}", loginRequest.getEmail(), role);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error en autenticación para {}: {}", loginRequest.getEmail(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales inválidas");
            errorResponse.put("mensaje", "Email o contraseña incorrectos");
            errorResponse.put("detalle", e.getMessage()); // Temporal para debugging
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    /**
     * Endpoint para verificar token válido
     * GET /api/auth/verify
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String email = jwtTokenUtil.getUsernameFromToken(jwtToken);
            String role = jwtTokenUtil.getRoleFromToken(jwtToken);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valido", true);
                response.put("email", email);
                response.put("rol", role.replace("ROLE_", ""));
                response.put("permisos", getPermisosPorRol(role.replace("ROLE_", "")));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of("valido", false, "mensaje", "Token inválido"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valido", false, "mensaje", "Token malformado"));
        }
    }

    /**
     * Endpoint para información de roles disponibles
     * GET /api/auth/roles
     */
    @GetMapping("/roles")
    public ResponseEntity<?> getRolesInfo() {
        Map<String, Object> rolesInfo = new HashMap<>();
        
        rolesInfo.put("ADMINISTRADOR", Map.of(
            "descripcion", "Acceso completo al sistema",
            "permisos", getPermisosPorRol("ADMINISTRADOR")
        ));
        
        rolesInfo.put("VENDEDOR", Map.of(
            "descripcion", "Solo vehículos disponibles",
            "permisos", getPermisosPorRol("VENDEDOR")
        ));
        
        rolesInfo.put("COMPRADOR", Map.of(
            "descripcion", "Sus pedidos y vehículos disponibles",
            "permisos", getPermisosPorRol("COMPRADOR")
        ));

        return ResponseEntity.ok(rolesInfo);
    }

    /**
     * Obtiene los permisos específicos por rol según los requerimientos
     */
    private String[] getPermisosPorRol(String rol) {
        return switch (rol) {
            case "ADMINISTRADOR" -> new String[]{
                "Ver todos los clientes",
                "Gestionar clientes", 
                "Ver todos los vehículos",
                "Gestionar vehículos",
                "Ver todos los pedidos",
                "Gestionar pedidos en todas las etapas",
                "Generar informes",
                "Administrar configuración del sistema"
            };
            case "VENDEDOR" -> new String[]{
                "Ver vehículos disponibles",
                "Ver disponibilidad de vehículos",
                "NO acceso a pedidos de compra",
                "NO acceso a datos de clientes"
            };
            case "COMPRADOR" -> new String[]{
                "Ver estado de sus propios pedidos",
                "Ver lista de vehículos disponibles",
                "Crear nuevos pedidos",
                "NO ver vehículos en proceso de venta",
                "NO ver pedidos de otros compradores"
            };
            default -> new String[]{"Sin permisos definidos"};
        };
    }

    /**
     * DTO para petición de login
     */
    public static class LoginRequest {
        private String email;
        private String password;

        // Constructors
        public LoginRequest() {}

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
} 