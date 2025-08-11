package com.tuankiet.repositories.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.search.AuthorSearchCriteria;
import com.tuankiet.entities.Author;
import com.tuankiet.repositories.AuthorRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
* Implementation of the AuthorRepository interface.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Repository
public class AuthorRepositoryImpl extends BaseRepositoryImpl<Author> implements AuthorRepository {

  private static final Logger logger = LoggerFactory.getLogger(AuthorRepositoryImpl.class);

  public AuthorRepositoryImpl(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
  }

  @Override
  public Optional<Author> findByFirstNameAndLastName(String firstName, String lastName) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Author> cq = cb.createQuery(Author.class);
          Root<Author> root = cq.from(Author.class);
          cq.where(cb.equal(cb.lower(root.get("firstName")), firstName.toLowerCase()),
                   cb.equal(cb.lower(root.get("lastName")), lastName.toLowerCase()));
          Author author = session.createQuery(cq).uniqueResult();
          logger.debug("Found author with name {} {}: {}", firstName, lastName, author != null);
          return Optional.ofNullable(author);
      } catch (Exception e) {
          logger.error("Error finding author by name {} {}: {}", firstName, lastName, e.getMessage(), e);
          throw new RuntimeException("Failed to find author by name: " + e.getMessage(), e);
      }
  }

  @Override
  public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Long> cq = cb.createQuery(Long.class);
          Root<Author> root = cq.from(Author.class);
          cq.select(cb.count(root)).where(cb.equal(cb.lower(root.get("firstName")), firstName.toLowerCase()),
                                          cb.equal(cb.lower(root.get("lastName")), lastName.toLowerCase()));
          Long count = session.createQuery(cq).getSingleResult();
          boolean exists = count > 0;
          logger.debug("Author with name {} {} exists: {}", firstName, lastName, exists);
          return exists;
      } catch (Exception e) {
          logger.error("Error checking existence of author by name {} {}: {}", firstName, lastName, e.getMessage(), e);
          throw new RuntimeException("Failed to check author existence by name: " + e.getMessage(), e);
      }
  }

  @Override
  public Page<Author> searchAuthors(AuthorSearchCriteria criteria, PageRequest pageRequest) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Author> cq = cb.createQuery(Author.class);
          Root<Author> root = cq.from(Author.class);
          List<Predicate> predicates = new ArrayList<>();

          if (criteria.getFirstName() != null && !criteria.getFirstName().isEmpty()) {
              predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
          }
          if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
              predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + criteria.getLastName().toLowerCase() + "%"));
          }

          cq.where(predicates.toArray(new Predicate[0]));

          if (pageRequest.getSort().isSorted()) {
              pageRequest.getSort().getCriteria().forEach(sortCriteria -> {
                  if (sortCriteria.getDirection() == com.tuankiet.dto.common.SortDirection.ASC) {
                      cq.orderBy(cb.asc(root.get(sortCriteria.getField())));
                  } else {
                      cq.orderBy(cb.desc(root.get(sortCriteria.getField())));
                  }
              });
          }

          Query<Author> query = session.createQuery(cq);
          query.setFirstResult((int) pageRequest.getOffset());
          query.setMaxResults(pageRequest.getSize());

          List<Author> content = query.getResultList();

          // Count total elements for pagination
          CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
          Root<Author> countRoot = countQuery.from(Author.class);
          countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
          long totalElements = session.createQuery(countQuery).getSingleResult();

          logger.debug("Found {} authors for search criteria and page {}", content.size(), pageRequest.getPage());
          return new Page<>(content, totalElements, pageRequest.getPage(), pageRequest.getSize());
      } catch (Exception e) {
          logger.error("Error searching authors: {}", e.getMessage(), e);
          throw new RuntimeException("Failed to search authors: " + e.getMessage(), e);
      }
  }
}
