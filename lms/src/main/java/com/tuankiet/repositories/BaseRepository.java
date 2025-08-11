package com.tuankiet.repositories;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.entities.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
* Generic repository interface for common CRUD operations.
* 
* @param <T> The entity type.
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface BaseRepository<T extends BaseEntity> {

  /**
   * Saves a new entity or updates an existing one.
   * @param entity The entity to save or update.
   * @return The saved or updated entity.
   */
  T save(T entity);

  /**
   * Finds an entity by its ID.
   * @param id The ID of the entity.
   * @return An Optional containing the entity if found, or empty otherwise.
   */
  Optional<T> findById(UUID id);

  /**
   * Retrieves all entities of the given type.
   * @return A list of all entities.
   */
  List<T> findAll();

  /**
   * Retrieves a paginated list of entities.
   * @param pageRequest The pagination and sorting information.
   * @return A Page object containing the requested entities.
   */
  Page<T> findAll(PageRequest pageRequest);

  /**
   * Deletes an entity by its ID.
   * @param id The ID of the entity to delete.
   * @return True if the entity was deleted, false otherwise.
   */
  boolean deleteById(UUID id);

  /**
   * Checks if an entity with the given ID exists.
   * @param id The ID of the entity.
   * @return True if the entity exists, false otherwise.
   */
  boolean existsById(UUID id);

  /**
   * Counts the total number of entities.
   * @return The total count of entities.
   */
  long count();
}
