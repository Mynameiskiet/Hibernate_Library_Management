package com.tuankiet.repositories.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.common.SortCriteria;
import com.tuankiet.entities.BaseEntity;
import com.tuankiet.repositories.BaseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
* Generic implementation of the BaseRepository interface.
* Provides common CRUD operations for entities.
* 
* @param <T> The entity type.
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Repository
public abstract class BaseRepositoryImpl<T extends BaseEntity> implements BaseRepository<T> {

  private static final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);

  @Autowired
  protected SessionFactory sessionFactory;

  private final Class<T> entityClass;

  @SuppressWarnings("unchecked")
  public BaseRepositoryImpl() {
      this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @Override
  public T save(T entity) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
          transaction = session.beginTransaction();
          if (entity.getId() == null) {
              session.persist(entity);
              logger.debug("Persisted new entity: {}", entity);
          } else {
              session.merge(entity);
              logger.debug("Merged existing entity: {}", entity);
          }
          transaction.commit();
          return entity;
      } catch (Exception e) {
          if (transaction != null) {
              transaction.rollback();
          }
          logger.error("Error saving entity {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
          throw new RuntimeException("Failed to save entity: " + e.getMessage(), e);
      }
  }

  @Override
  public Optional<T> findById(UUID id) {
      try (Session session = sessionFactory.openSession()) {
          T entity = session.get(entityClass, id);
          logger.debug("Found entity {} with ID {}: {}", entityClass.getSimpleName(), id, entity != null);
          return Optional.ofNullable(entity);
      } catch (Exception e) {
          logger.error("Error finding entity {} by ID {}: {}", entityClass.getSimpleName(), id, e.getMessage(), e);
          throw new RuntimeException("Failed to find entity by ID: " + e.getMessage(), e);
      }
  }

  @Override
  public List<T> findAll() {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<T> cq = cb.createQuery(entityClass);
          Root<T> root = cq.from(entityClass);
          cq.select(root);
          List<T> result = session.createQuery(cq).getResultList();
          logger.debug("Found {} entities of type {}", result.size(), entityClass.getSimpleName());
          return result;
      } catch (Exception e) {
          logger.error("Error finding all entities of type {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
          throw new RuntimeException("Failed to find all entities: " + e.getMessage(), e);
      }
  }

  @Override
  public Page<T> findAll(PageRequest pageRequest) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<T> cq = cb.createQuery(entityClass);
          Root<T> root = cq.from(entityClass);
          cq.select(root);

          if (pageRequest.getSort().isSorted()) {
              pageRequest.getSort().getCriteria().forEach(sortCriteria -> {
                  if (sortCriteria.getDirection() == com.tuankiet.dto.common.SortDirection.ASC) {
                      cq.orderBy(cb.asc(root.get(sortCriteria.getField())));
                  } else {
                      cq.orderBy(cb.desc(root.get(sortCriteria.getField())));
                  }
              });
          }

          Query<T> query = session.createQuery(cq);
          query.setFirstResult((int) pageRequest.getOffset());
          query.setMaxResults(pageRequest.getSize());

          List<T> content = query.getResultList();

          // Count total elements
          CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
          countQuery.select(cb.count(countQuery.from(entityClass)));
          long totalElements = session.createQuery(countQuery).getSingleResult();

          logger.debug("Found {} entities of type {} for page {} with size {}", content.size(), entityClass.getSimpleName(), pageRequest.getPage(), pageRequest.getSize());
          return new Page<>(content, totalElements, pageRequest.getPage(), pageRequest.getSize());
      } catch (Exception e) {
          logger.error("Error finding all entities of type {} with pagination: {}", entityClass.getSimpleName(), e.getMessage(), e);
          throw new RuntimeException("Failed to find all entities with pagination: " + e.getMessage(), e);
      }
  }

  @Override
  public boolean deleteById(UUID id) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
          transaction = session.beginTransaction();
          T entity = session.get(entityClass, id);
          if (entity != null) {
              session.remove(entity);
              transaction.commit();
              logger.debug("Deleted entity {} with ID {}", entityClass.getSimpleName(), id);
              return true;
          }
          transaction.rollback(); // No entity to delete, so rollback
          logger.debug("Attempted to delete entity {} with ID {} but it was not found.", entityClass.getSimpleName(), id);
          return false;
      } catch (Exception e) {
          if (transaction != null) {
              transaction.rollback();
          }
          logger.error("Error deleting entity {} by ID {}: {}", entityClass.getSimpleName(), id, e.getMessage(), e);
          throw new RuntimeException("Failed to delete entity by ID: " + e.getMessage(), e);
      }
  }

  @Override
  public boolean existsById(UUID id) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Long> cq = cb.createQuery(Long.class);
          Root<T> root = cq.from(entityClass);
          cq.select(cb.count(root)).where(cb.equal(root.get("id"), id));
          Long count = session.createQuery(cq).getSingleResult();
          boolean exists = count > 0;
          logger.debug("Entity {} with ID {} exists: {}", entityClass.getSimpleName(), id, exists);
          return exists;
      } catch (Exception e) {
          logger.error("Error checking existence of entity {} by ID {}: {}", entityClass.getSimpleName(), id, e.getMessage(), e);
          throw new RuntimeException("Failed to check entity existence by ID: " + e.getMessage(), e);
      }
  }

  @Override
  public long count() {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Long> cq = cb.createQuery(Long.class);
          cq.select(cb.count(cq.from(entityClass)));
          long count = session.createQuery(cq).getSingleResult();
          logger.debug("Counted {} entities of type {}", count, entityClass.getSimpleName());
          return count;
      } catch (Exception e) {
          logger.error("Error counting entities of type {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
          throw new RuntimeException("Failed to count entities: " + e.getMessage(), e);
      }
  }
}
