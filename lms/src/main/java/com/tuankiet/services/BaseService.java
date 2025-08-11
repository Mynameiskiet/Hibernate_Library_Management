package com.tuankiet.services;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.entities.BaseEntity;

import java.util.List;
import java.util.UUID;

/**
* Generic service interface for common business logic operations.
* 
* @param <T> The entity type.
* @param <R> The response DTO type.
* @param <C> The create request DTO type.
* @param <U> The update request DTO type.
* @param <S> The search criteria DTO type.
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface BaseService<T extends BaseEntity, R, C, U, S> {

  /**
   * Creates a new entity based on the provided create request DTO.
   * @param createRequest The DTO containing data for creating the entity.
   * @return The response DTO of the created entity.
   */
  R create(C createRequest);

  /**
   * Retrieves an entity by its ID.
   * @param id The ID of the entity.
   * @return The response DTO of the found entity.
   * @throws com.tuankiet.exceptions.EntityNotFoundException if the entity is not found.
   */
  R getById(UUID id);

  /**
   * Updates an existing entity based on the provided update request DTO.
   * @param updateRequest The DTO containing data for updating the entity.
   * @return The response DTO of the updated entity.
   * @throws com.tuankiet.exceptions.EntityNotFoundException if the entity to update is not found.
   */
  R update(U updateRequest);

  /**
   * Deletes an entity by its ID.
   * @param id The ID of the entity to delete.
   * @return True if the entity was successfully deleted, false otherwise.
   */
  boolean delete(UUID id);

  /**
   * Retrieves all entities.
   * @return A list of response DTOs for all entities.
   */
  List<R> getAll();

  /**
   * Searches for entities based on provided criteria with pagination and sorting.
   * @param criteria The search criteria DTO.
   * @param pageRequest The pagination and sorting information.
   * @return A Page of response DTOs matching the criteria.
   */
  Page<R> search(S criteria, PageRequest pageRequest);

  /**
   * Counts the total number of entities.
   * @return The total count of entities.
   */
  long count();
}
