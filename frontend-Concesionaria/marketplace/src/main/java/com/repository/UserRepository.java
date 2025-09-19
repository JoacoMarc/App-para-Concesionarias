package com.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    
    // Método personalizado para buscar por UUID como String
    default Optional<User> findByUuidString(String uuidString) {
        try {
            UUID uuid = UUID.fromString(uuidString);
            return findById(uuid);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
