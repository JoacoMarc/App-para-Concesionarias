package com.grupo9.sistemaConcesionaria.repository;

import com.grupo9.sistemaConcesionaria.model.Usuario;
import com.grupo9.sistemaConcesionaria.model.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByMail(String mail);
    
    List<Usuario> findByRol(RolUsuario rol);
    
    boolean existsByMail(String mail);
} 