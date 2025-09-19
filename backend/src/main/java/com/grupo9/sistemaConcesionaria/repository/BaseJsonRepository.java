package com.grupo9.sistemaConcesionaria.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * BaseJsonRepository - Implementación base para persistencia JSON
 * Proporciona funcionalidad común para todas las entidades JSON
 */
public abstract class BaseJsonRepository<T, ID> implements JsonRepository<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(BaseJsonRepository.class);
    
    protected final ObjectMapper objectMapper;
    protected final String fileName;
    protected final TypeReference<List<T>> typeReference;
    protected final Function<T, ID> idExtractor;
    protected final Function<T, T> idSetter;
    
    private final Path dataDirectory;

    /**
     * Constructor base
     * @param fileName Nombre del archivo JSON (ej: "vehiculos.json")
     * @param typeReference Referencia de tipo para deserialización
     * @param idExtractor Función para extraer el ID de una entidad
     * @param idSetter Función para establecer el ID en una entidad (para auto-generación)
     */
    public BaseJsonRepository(String fileName, TypeReference<List<T>> typeReference, 
                             Function<T, ID> idExtractor, Function<T, T> idSetter) {
        this.fileName = fileName;
        this.typeReference = typeReference;
        this.idExtractor = idExtractor;
        this.idSetter = idSetter;
        
        // Configurar ObjectMapper con soporte para Java 8 Time API
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        // Crear directorio de datos si no existe
        this.dataDirectory = Paths.get("data");
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            logger.error("Error creando directorio de datos: {}", e.getMessage());
        }
    }

    /**
     * Obtiene la ruta completa del archivo
     */
    protected Path getFilePath() {
        return dataDirectory.resolve(fileName);
    }

    /**
     * Carga todas las entidades desde el archivo JSON
     */
    protected List<T> loadFromFile() {
        Path filePath = getFilePath();
        
        if (!Files.exists(filePath)) {
            logger.debug("Archivo {} no existe, retornando lista vacía", fileName);
            return new ArrayList<>();
        }

        try {
            List<T> entities = objectMapper.readValue(filePath.toFile(), typeReference);
            logger.debug("Cargadas {} entidades desde {}", entities.size(), fileName);
            return entities;
        } catch (IOException e) {
            logger.error("Error leyendo archivo {}: {}", fileName, e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Guarda todas las entidades en el archivo JSON
     */
    protected void saveToFile(List<T> entities) {
        Path filePath = getFilePath();
        
        try {
            // Escribir con formato bonito
            objectMapper.writerWithDefaultPrettyPrinter()
                       .writeValue(filePath.toFile(), entities);
            logger.debug("Guardadas {} entidades en {}", entities.size(), fileName);
        } catch (IOException e) {
            logger.error("Error escribiendo archivo {}: {}", fileName, e.getMessage());
        }
    }

    @Override
    public T save(T entity) {
        List<T> entities = loadFromFile();
        ID entityId = idExtractor.apply(entity);
        
        if (entityId == null) {
            // Auto-generar ID si no existe
            entity = generateId(entity, entities);
            entityId = idExtractor.apply(entity);
            
            // Verificar que el ID fue generado correctamente
            if (entityId == null) {
                logger.error("No se pudo generar ID para la entidad: {}", entity);
                throw new IllegalStateException("Error generando ID para la entidad");
            }
        }
        
        // Buscar y reemplazar si existe, o agregar si es nuevo
        boolean found = false;
        for (int i = 0; i < entities.size(); i++) {
            ID existingId = idExtractor.apply(entities.get(i));
            if (existingId != null && existingId.equals(entityId)) {
                entities.set(i, entity);
                found = true;
                break;
            }
        }
        
        if (!found) {
            entities.add(entity);
        }
        
        saveToFile(entities);
        logger.info("Entidad guardada con ID: {}", entityId);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return loadFromFile().stream()
                .filter(entity -> idExtractor.apply(entity).equals(id))
                .findFirst();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(loadFromFile());
    }

    @Override
    public boolean deleteById(ID id) {
        List<T> entities = loadFromFile();
        boolean removed = entities.removeIf(entity -> idExtractor.apply(entity).equals(id));
        
        if (removed) {
            saveToFile(entities);
            logger.info("Entidad eliminada con ID: {}", id);
        }
        
        return removed;
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        return loadFromFile().size();
    }

    @Override
    public void deleteAll() {
        saveToFile(new ArrayList<>());
        logger.info("Todas las entidades eliminadas de {}", fileName);
    }

    /**
     * Método abstracto para generar IDs automáticamente
     * @param entity Entidad sin ID
     * @param existingEntities Lista de entidades existentes
     * @return Entidad con ID generado
     */
    protected abstract T generateId(T entity, List<T> existingEntities);
} 