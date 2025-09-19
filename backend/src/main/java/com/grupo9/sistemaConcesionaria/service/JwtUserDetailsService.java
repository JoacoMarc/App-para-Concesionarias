package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Usuario;
import com.grupo9.sistemaConcesionaria.model.Cliente;
import com.grupo9.sistemaConcesionaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * Servicio para cargar detalles de usuario para autenticación JWT
 * Implementa UserDetailsService de Spring Security
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private GestorDeUsuarios gestorUsuarios;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Buscando usuario con email: {}", email);
        
        // Buscar en usuarios del sistema (Administrador, Vendedor, Cliente) usando JPA
        Optional<Usuario> usuarioOpt = usuarioRepository.findByMail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            logger.info("Usuario encontrado en BD: {} con rol: {}", usuario.getMail(), usuario.getRol());
            
            String role = "ROLE_" + usuario.getRol().name();
            // Mapear CLIENTE a COMPRADOR para compatibilidad con el sistema de roles
            if ("ROLE_CLIENTE".equals(role)) {
                role = "ROLE_COMPRADOR";
            }
            
            return User.builder()
                    .username(usuario.getMail())
                    .password(usuario.getPassword()) // Usar la contraseña almacenada
                    .authorities(Arrays.asList(new SimpleGrantedAuthority(role)))
                    .build();
        }

        logger.info("Usuario no encontrado en BD, buscando en clientes del gestor...");
        
        // Si no está en BD, buscar en el gestor de usuarios (sistema legacy)
        try {
            Cliente cliente = gestorUsuarios.getCliente(email); // Usar email como documento por simplicidad
            logger.info("Cliente encontrado en gestor: {}", cliente.getMail());
            return User.builder()
                    .username(cliente.getMail())
                    .password(getDefaultPasswordForLegacyClients()) // Solo para clientes legacy
                    .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_COMPRADOR")))
                    .build();
        } catch (Exception e) {
            logger.error("Usuario no encontrado en ningún lugar: {}", email);
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
    }

    /**
     * Método temporal para contraseña por defecto de clientes legacy
     * TODO: Migrar todos los clientes a la base de datos
     */
    private String getDefaultPasswordForLegacyClients() {
        return "password123";
    }

    /**
     * Obtiene UserDetails por email con manejo de roles específico
     */
    public UserDetails getUserDetailsByEmail(String email) throws UsernameNotFoundException {
        return loadUserByUsername(email);
    }

    /**
     * Verifica si un usuario existe en el sistema
     */
    public boolean userExists(String email) {
        logger.info("Verificando si existe usuario: {}", email);
        
        // Verificar en usuarios del sistema
        Optional<Usuario> usuarioOpt = usuarioRepository.findByMail(email);
        if (usuarioOpt.isPresent()) {
            logger.info("Usuario encontrado en BD: {}", email);
            return true;
        }
        
        // Verificar en clientes
        try {
            Cliente cliente = gestorUsuarios.getCliente(email);
            logger.info("Cliente encontrado: {}", email);
            return true;
        } catch (Exception e) {
            logger.warn("Usuario no encontrado: {}", email);
            return false;
        }
    }
} 