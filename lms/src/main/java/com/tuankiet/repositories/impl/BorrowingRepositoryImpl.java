package com.tuankiet.repositories.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.search.BorrowingSearchCriteria;
import com.tuankiet.entities.Book;
import com.tuankiet.entities.Borrowing;
import com.tuankiet.entities.Member;
import com.tuankiet.enums.BorrowingStatus;
import com.tuankiet.repositories.BorrowingRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
* Implementation of the BorrowingRepository interface.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Repository
public class BorrowingRepositoryImpl extends BaseRepositoryImpl<Borrowing> implements BorrowingRepository {

  private static final Logger logger = LoggerFactory.getLogger(BorrowingRepositoryImpl.class);

  public BorrowingRepositoryImpl(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
  }

  @Override
  public List<Borrowing> findByBookId(UUID bookId) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
          Root<Borrowing> root = cq.from(Borrowing.class);
          cq.where(cb.equal(root.get("book").get("id"), bookId));
          List<Borrowing> result = session.createQuery(cq).getResultList();
          logger.debug("Found {} borrowings for book ID {}", result.size(), bookId);
          return result;
      } catch (Exception e) {
          logger.error("Error finding borrowings by book ID {}: {}", bookId, e.getMessage(), e);
          throw new RuntimeException("Failed to find borrowings by book ID: " + e.getMessage(), e);
      }
  }

  @Override
  public List<Borrowing> findByMemberId(UUID memberId) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
          Root<Borrowing> root = cq.from(Borrowing.class);
          cq.where(cb.equal(root.get("member").get("id"), memberId));
          List<Borrowing> result = session.createQuery(cq).getResultList();
          logger.debug("Found {} borrowings for member ID {}", result.size(), memberId);
          return result;
      } catch (Exception e) {
          logger.error("Error finding borrowings by member ID {}: {}", memberId, e.getMessage(), e);
          throw new RuntimeException("Failed to find borrowings by member ID: " + e.getMessage(), e);
      }
  }

  @Override
  public List<Borrowing> findByStatus(BorrowingStatus status) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
          Root<Borrowing> root = cq.from(Borrowing.class);
          cq.where(cb.equal(root.get("status"), status));
          List<Borrowing> result = session.createQuery(cq).getResultList();
          logger.debug("Found {} borrowings with status {}", result.size(), status);
          return result;
      } catch (Exception e) {
          logger.error("Error finding borrowings by status {}: {}", status, e.getMessage(), e);
          throw new RuntimeException("Failed to find borrowings by status: " + e.getMessage(), e);
      }
  }

  @Override
  public List<Borrowing> findActiveBorrowings(Book book, Member member) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
          Root<Borrowing> root = cq.from(Borrowing.class);
          Predicate bookPredicate = cb.equal(root.get("book"), book);
          Predicate memberPredicate = cb.equal(root.get("member"), member);
          Predicate statusPredicate = cb.equal(root.get("status"), BorrowingStatus.BORROWED);
          cq.where(cb.and(bookPredicate, memberPredicate, statusPredicate));
          List<Borrowing> result = session.createQuery(cq).getResultList();
          logger.debug("Found {} active borrowings for book {} and member {}", result.size(), book.getId(), member.getId());
          return result;
      } catch (Exception e) {
          logger.error("Error finding active borrowings for book {} and member {}: {}", book.getId(), member.getId(), e.getMessage(), e);
          throw new RuntimeException("Failed to find active borrowings: " + e.getMessage(), e);
      }
  }

  @Override
  public long countBorrowedCopies(UUID bookId) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Long> cq = cb.createQuery(Long.class);
          Root<Borrowing> root = cq.from(Borrowing.class);
          cq.select(cb.count(root)).where(
              cb.equal(root.get("book").get("id"), bookId),
              cb.equal(root.get("status"), BorrowingStatus.BORROWED)
          );
          long count = session.createQuery(cq).getSingleResult();
          logger.debug("Counted {} borrowed copies for book ID {}", count, bookId);
          return count;
      } catch (Exception e) {
          logger.error("Error counting borrowed copies for book ID {}: {}", bookId, e.getMessage(), e);
          throw new RuntimeException("Failed to count borrowed copies: " + e.getMessage(), e);
      }
  }

  @Override
  public Page<Borrowing> searchBorrowings(BorrowingSearchCriteria criteria, PageRequest pageRequest) {
      try (Session session = sessionFactory.openSession()) {
          CriteriaBuilder cb = session.getCriteriaBuilder();
          CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
          Root<Borrowing> root = cq.from(Borrowing.class);
          List<Predicate> predicates = new ArrayList<>();

          if (criteria.getBookId() != null) {
              predicates.add(cb.equal(root.get("book").get("id"), criteria.getBookId()));
          }
          if (criteria.getMemberId() != null) {
              predicates.add(cb.equal(root.get("member").get("id"), criteria.getMemberId()));
          }
          if (criteria.getBorrowDateFrom() != null) {
              predicates.add(cb.greaterThanOrEqualTo(root.get("borrowDate"), criteria.getBorrowDateFrom()));
          }
          if (criteria.getBorrowDateTo() != null) {
              predicates.add(cb.lessThanOrEqualTo(root.get("borrowDate"), criteria.getBorrowDateTo()));
          }
          if (criteria.getDueDateFrom() != null) {
              predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), criteria.getDueDateFrom()));
          }
          if (criteria.getDueDateTo() != null) {
              predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), criteria.getDueDateTo()));
          }
          if (criteria.getStatus() != null) {
              predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
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

          Query<Borrowing> query = session.createQuery(cq);
          query.setFirstResult((int) pageRequest.getOffset());
          query.setMaxResults(pageRequest.getSize());

          List<Borrowing> content = query.getResultList();

          // Count total elements for pagination
          CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
          Root<Borrowing> countRoot = countQuery.from(Borrowing.class);
          countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
          long totalElements = session.createQuery(countQuery).getSingleResult();

          logger.debug("Found {} borrowings for search criteria and page {}", content.size(), pageRequest.getPage());
          return new Page<>(content, totalElements, pageRequest.getPage(), pageRequest.getSize());
      } catch (Exception e) {
          logger.error("Error searching borrowings: {}", e.getMessage(), e);
          throw new RuntimeException("Failed to search borrowings: " + e.getMessage(), e);
      }
  }
}
