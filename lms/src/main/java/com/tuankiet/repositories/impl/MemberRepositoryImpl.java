package com.tuankiet.repositories.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.search.MemberSearchCriteria;
import com.tuankiet.entities.Member;
import com.tuankiet.repositories.MemberRepository;
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
* Implementation of the MemberRepository interface.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Repository
public class MemberRepositoryImpl extends BaseRepositoryImpl<Member> implements MemberRepository {

  private static final Logger logger = LoggerFactory.getLogger(MemberRepositoryImpl.class);

  public MemberRepositoryImpl(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
  }

  @Override
  public Optional<Member> findByEmail(String email) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Member> cq = cb.createQuery(Member.class);
          Root<Member> root = cq.from(Member.class);
          cq.where(cb.equal(cb.lower(root.get("email")), email.toLowerCase()));
          Member member = session.createQuery(cq).uniqueResult();
          logger.debug("Found member with email {}: {}", email, member != null);
          return Optional.ofNullable(member);
      } catch (Exception e) {
          logger.error("Error finding member by email {}: {}", email, e.getMessage(), e);
          throw new RuntimeException("Failed to find member by email: " + e.getMessage(), e);
      }
  }

  @Override
  public boolean existsByEmail(String email) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Long> cq = cb.createQuery(Long.class);
          Root<Member> root = cq.from(Member.class);
          cq.select(cb.count(root)).where(cb.equal(cb.lower(root.get("email")), email.toLowerCase()));
          Long count = session.createQuery(cq).getSingleResult();
          boolean exists = count > 0;
          logger.debug("Member with email {} exists: {}", email, exists);
          return exists;
      } catch (Exception e) {
          logger.error("Error checking existence of member by email {}: {}", email, e.getMessage(), e);
          throw new RuntimeException("Failed to check member existence by email: " + e.getMessage(), e);
      }
  }

  @Override
  public Page<Member> searchMembers(MemberSearchCriteria criteria, PageRequest pageRequest) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Member> cq = cb.createQuery(Member.class);
          Root<Member> root = cq.from(Member.class);
          List<Predicate> predicates = new ArrayList<>();

          if (criteria.getFirstName() != null && !criteria.getFirstName().isEmpty()) {
              predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
          }
          if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
              predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + criteria.getLastName().toLowerCase() + "%"));
          }
          if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
              predicates.add(cb.like(cb.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
          }
          if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isEmpty()) {
              predicates.add(cb.like(root.get("phoneNumber"), "%" + criteria.getPhoneNumber() + "%"));
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

          Query<Member> query = session.createQuery(cq);
          query.setFirstResult((int) pageRequest.getOffset());
          query.setMaxResults(pageRequest.getSize());

          List<Member> content = query.getResultList();

          // Count total elements for pagination
          CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
          Root<Member> countRoot = countQuery.from(Member.class);
          countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
          long totalElements = session.createQuery(countQuery).getSingleResult();

          logger.debug("Found {} members for search criteria and page {}", content.size(), pageRequest.getPage());
          return new Page<>(content, totalElements, pageRequest.getPage(), pageRequest.getSize());
      } catch (Exception e) {
          logger.error("Error searching members: {}", e.getMessage(), e);
          throw new RuntimeException("Failed to search members: " + e.getMessage(), e);
      }
  }
}
