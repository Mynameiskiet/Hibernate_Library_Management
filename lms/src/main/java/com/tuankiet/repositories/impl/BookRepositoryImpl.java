package com.tuankiet.repositories.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.common.SortCriteria;
import com.tuankiet.dto.search.BookSearchCriteria;
import com.tuankiet.entities.Book;
import com.tuankiet.enums.BorrowingStatus;
import com.tuankiet.repositories.BookRepository;
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
import java.util.UUID;

/**
 * Implementation of the BookRepository interface.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public class BookRepositoryImpl extends BaseRepositoryImpl<Book> implements BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);

    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Session session = sessionFactory.getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book b WHERE b.isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);
        return query.uniqueResultOptional();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Book b WHERE b.isbn = :isbn", Long.class);
        query.setParameter("isbn", isbn);
        return query.uniqueResult() > 0;
    }

    @Override
    public Page<Book> searchBooks(BookSearchCriteria criteria, PageRequest pageRequest) {
        Session session = sessionFactory.getCurrentSession();
        
        StringBuilder hql = new StringBuilder("FROM Book b LEFT JOIN FETCH b.author WHERE 1=1");
        
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            hql.append(" AND LOWER(b.title) LIKE LOWER(:title)");
        }
        if (criteria.getIsbn() != null && !criteria.getIsbn().trim().isEmpty()) {
            hql.append(" AND b.isbn LIKE :isbn");
        }
        if (criteria.getAuthorName() != null && !criteria.getAuthorName().trim().isEmpty()) {
            hql.append(" AND (LOWER(b.author.firstName) LIKE LOWER(:authorName) OR LOWER(b.author.lastName) LIKE LOWER(:authorName))");
        }
        if (criteria.getCategory() != null) {
            hql.append(" AND b.category = :category");
        }
        if (criteria.getPublicationYear() != null) {
            hql.append(" AND b.publicationYear = :publicationYear");
        }
        if (criteria.getAvailableOnly() != null && criteria.getAvailableOnly()) {
            hql.append(" AND b.availableCopies > 0");
        }

        // Add sorting
        if (pageRequest.getSort() != null && !pageRequest.getSort().getCriteria().isEmpty()) {
            hql.append(" ORDER BY ");
            for (int i = 0; i < pageRequest.getSort().getCriteria().size(); i++) {
                if (i > 0) hql.append(", ");
                SortCriteria sortCriteria = pageRequest.getSort().getCriteria().get(i);
                hql.append("b.").append(sortCriteria.getProperty()).append(" ").append(sortCriteria.getDirection().name());
            }
        } else {
            hql.append(" ORDER BY b.title ASC");
        }

        Query<Book> query = session.createQuery(hql.toString(), Book.class);
        
        // Set parameters
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            query.setParameter("title", "%" + criteria.getTitle().trim() + "%");
        }
        if (criteria.getIsbn() != null && !criteria.getIsbn().trim().isEmpty()) {
            query.setParameter("isbn", "%" + criteria.getIsbn().trim() + "%");
        }
        if (criteria.getAuthorName() != null && !criteria.getAuthorName().trim().isEmpty()) {
            query.setParameter("authorName", "%" + criteria.getAuthorName().trim() + "%");
        }
        if (criteria.getCategory() != null) {
            query.setParameter("category", criteria.getCategory());
        }
        if (criteria.getPublicationYear() != null) {
            query.setParameter("publicationYear", criteria.getPublicationYear());
        }

        // Count query for total elements
        String countHql = hql.toString().replaceFirst("FROM Book b LEFT JOIN FETCH b.author", "SELECT COUNT(DISTINCT b) FROM Book b LEFT JOIN b.author");
        countHql = countHql.substring(0, countHql.indexOf(" ORDER BY"));
        Query<Long> countQuery = session.createQuery(countHql, Long.class);
        
        // Set parameters for count query
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            countQuery.setParameter("title", "%" + criteria.getTitle().trim() + "%");
        }
        if (criteria.getIsbn() != null && !criteria.getIsbn().trim().isEmpty()) {
            countQuery.setParameter("isbn", "%" + criteria.getIsbn().trim() + "%");
        }
        if (criteria.getAuthorName() != null && !criteria.getAuthorName().trim().isEmpty()) {
            countQuery.setParameter("authorName", "%" + criteria.getAuthorName().trim() + "%");
        }
        if (criteria.getCategory() != null) {
            countQuery.setParameter("category", criteria.getCategory());
        }
        if (criteria.getPublicationYear() != null) {
            countQuery.setParameter("publicationYear", criteria.getPublicationYear());
        }

        long totalElements = countQuery.uniqueResult();

        // Set pagination
        query.setFirstResult(pageRequest.getPage() * pageRequest.getSize());
        query.setMaxResults(pageRequest.getSize());

        List<Book> content = query.getResultList();
        return new Page<>(content, totalElements, pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public long countBorrowedCopies(UUID bookId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(
            "SELECT COUNT(br) FROM Borrowing br WHERE br.book.id = :bookId AND br.status IN (:statuses)", 
            Long.class
        );
        query.setParameter("bookId", bookId);
        query.setParameter("statuses", List.of(BorrowingStatus.BORROWED, BorrowingStatus.OVERDUE));
        return query.uniqueResult();
    }
}
