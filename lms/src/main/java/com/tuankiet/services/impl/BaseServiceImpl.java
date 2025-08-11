package com.tuankiet.services.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.entities.BaseEntity;
import com.tuankiet.exceptions.EntityNotFoundException;
import com.tuankiet.repositories.BaseRepository;
import com.tuankiet.services.BaseService;
import com.tuankiet.services.validation.ValidationService;
import com.tuankiet.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Generic implementation of the BaseService interface.
 * Provides common business logic operations for entities.
 * 
 * @param <T> The entity type.
 * @param <R> The response DTO type.
 * @param <C> The create request DTO type.
 * @param <U> The update request DTO type.
 * @param <S> The search criteria DTO type.
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseServiceImpl<T extends BaseEntity, R, C, U, S> implements BaseService<T, R, C, U, S> {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    protected final BaseRepository<T> repository;
    protected final ValidationService validationService;
    protected final MapperUtil mapperUtil;

    private final Class<T> entityClass;
    private final Class<R> responseClass;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl(BaseRepository<T> repository, ValidationService validationService, MapperUtil mapperUtil) {
        this.repository = repository;
        this.validationService = validationService;
        this.mapperUtil = mapperUtil;

        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        this.responseClass = (Class<R>) genericSuperclass.getActualTypeArguments()[1];
    }

    @Override
    @Transactional
    public R create(C createRequest) {
        logger.info("Creating new {} with data: {}", entityClass.getSimpleName(), createRequest);
        validationService.validate(createRequest);
        T entity = mapperUtil.map(createRequest, entityClass);
        T savedEntity = repository.save(entity);
        logger.info("Successfully created {}: {}", entityClass.getSimpleName(), savedEntity.getId());
        return mapperUtil.map(savedEntity, responseClass);
    }

    @Override
    @Transactional(readOnly = true)
    public R getById(UUID id) {
        logger.debug("Attempting to retrieve {} with ID: {}", entityClass.getSimpleName(), id);
        T entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName(), id));
        logger.debug("Successfully retrieved {} with ID: {}", entityClass.getSimpleName(), id);
        return mapperUtil.map(entity, responseClass);
    }

    @Override
    @Transactional
    public R update(U updateRequest) {
        logger.info("Updating {} with data: {}", entityClass.getSimpleName(), updateRequest);
        validationService.validate(updateRequest);

        UUID id = mapperUtil.getIdFromUpdateRequest(updateRequest);
        T existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName(), id));

        mapperUtil.map(updateRequest, existingEntity);
        T updatedEntity = repository.save(existingEntity);
        logger.info("Successfully updated {}: {}", entityClass.getSimpleName(), updatedEntity.getId());
        return mapperUtil.map(updatedEntity, responseClass);
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        logger.info("Attempting to delete {} with ID: {}", entityClass.getSimpleName(), id);
        boolean deleted = repository.deleteById(id);
        if (deleted) {
            logger.info("Successfully deleted {} with ID: {}", entityClass.getSimpleName(), id);
        } else {
            logger.warn("Failed to delete {}: ID {} not found.", entityClass.getSimpleName(), id);
        }
        return deleted;
    }

    @Override
    @Transactional(readOnly = true)
    public List<R> getAll() {
        logger.debug("Retrieving all {}s", entityClass.getSimpleName());
        List<T> entities = repository.findAll();
        logger.debug("Found {} {}s", entities.size(), entityClass.getSimpleName());
        return entities.stream()
                .map(entity -> mapperUtil.map(entity, responseClass))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<R> search(S criteria, PageRequest pageRequest) {
        // This method should be overridden in concrete implementations
        // as it requires specific search logic for each entity type
        throw new UnsupportedOperationException("Search method must be implemented by concrete service classes.");
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        logger.debug("Counting all {}s", entityClass.getSimpleName());
        long count = repository.count();
        logger.debug("Total {}s: {}", entityClass.getSimpleName(), count);
        return count;
    }
}
