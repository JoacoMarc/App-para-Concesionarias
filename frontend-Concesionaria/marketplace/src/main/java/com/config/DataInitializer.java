package com.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Role;
import com.entity.User;
import com.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Inicializando datos por defecto...");
        
        // Creamos usuario admin por defecto
        createDefaultAdmin();
        
        log.info("Inicialización de datos completada.");
    }

    private void createDefaultAdmin() {
        String adminEmail = "admin@marketplace.com";
        
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName("Admin");
            admin.setSurname("Sistema");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
    
            userRepository.save(admin);
            log.info("Usuario admin creado: {}", adminEmail);
            log.warn("IMPORTANTE: Cambiar contraseña del admin en producción!");
        }
    }
} 