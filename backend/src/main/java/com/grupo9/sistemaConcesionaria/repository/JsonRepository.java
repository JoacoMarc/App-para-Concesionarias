package com.grupo9.sistemaConcesionaria.repository;

import java.util.List;
import java.util.Optional;

/**
 * JsonRepository - Interface genérica para persistencia en archivos JSON
 * Proporciona operaciones CRUD básicas para cualquier entidad
 * @param <T> Tipo de entidad
 * @param <ID> Tipo del identificador
 */
public interface JsonRepository<T, ID> {
    
    /**
     * Guarda una entidad en el archivo JSON
     * @param entity Entidad a guardar
     * @return Entidad guardada
     */
    T save(T entity);
    
    /**
     * Busca una entidad por ID
     * @param id Identificador
     * @return Optional con la entidad encontrada
     */
    Optional<T> findById(ID id);
    
    /**
     * Obtiene todas las entidades
     * @return Lista de todas las entidades
     */
    List<T> findAll();
    
    /**
     * Elimina una entidad por ID
     * @param id Identificador
     * @return true si se eliminó, false si no existía
     */
    boolean deleteById(ID id);
    
    /**
     * Verifica si existe una entidad con el ID dado
     * @param id Identificador
     * @return true si existe, false si no
     */
    boolean existsById(ID id);
    
    /**
     * Cuenta el total de entidades
     * @return Número total de entidades
     */
    long count();
    
    /**
     * Elimina todas las entidades
     */
    void deleteAll();
} 