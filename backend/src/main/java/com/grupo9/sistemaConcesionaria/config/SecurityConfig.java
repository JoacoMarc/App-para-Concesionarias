package com.grupo9.sistemaConcesionaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuración de seguridad de Spring Security
 * 
 * Define roles y permisos según los requerimientos:
 * - ADMINISTRADOR: Acceso completo a todas las funcionalidades
 * - VENDEDOR: Solo vehículos disponibles, sin acceso a pedidos/clientes
 * - COMPRADOR: Solo sus propios pedidos y vehículos disponibles
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Configuración de la cadena de filtros de seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // === ENDPOINTS PÚBLICOS ===
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/index.html", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                
                // === TEMPORAL: Permitir creación de usuarios para demo ===
                .requestMatchers("/api/usuarios/**").permitAll()
                .requestMatchers("/api/vehiculos/**").permitAll()
                
                // === ADMINISTRADOR - ACCESO COMPLETO ===
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/configuracion/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/informes/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/pedidos/todos").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/pedidos/estadisticas").hasRole("ADMINISTRADOR")
                
                // === VENDEDOR - SOLO VEHÍCULOS DISPONIBLES ===
                .requestMatchers("/api/vehiculos/roles/vendedor/**").hasRole("VENDEDOR")
                .requestMatchers("/api/vehiculos/roles/*/disponibilidad").hasRole("VENDEDOR")
                
                // === COMPRADOR - SUS PROPIOS PEDIDOS Y VEHÍCULOS DISPONIBLES ===
                .requestMatchers("/api/vehiculos/roles/comprador/**").hasRole("COMPRADOR")
                .requestMatchers("/api/pedidos/mis-pedidos").hasRole("COMPRADOR")
                .requestMatchers("/api/pedidos/crear").hasRole("COMPRADOR")
                .requestMatchers("/api/pedidos/*/estado").hasRole("COMPRADOR")
                
                // === ENDPOINTS MIXTOS ===
                // Pedidos individuales - Administrador ve todos, Comprador solo los suyos
                .requestMatchers("/api/pedidos/*").hasAnyRole("ADMINISTRADOR", "COMPRADOR")
                
                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Configuración especial para H2 Console
        http.headers(headers -> headers.frameOptions().disable());

        // Agregar filtro JWT antes del filtro de autenticación estándar
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Codificador de contraseñas usando BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usar BCryptPasswordEncoder para hashear contraseñas de forma segura
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para el administrador de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
} 