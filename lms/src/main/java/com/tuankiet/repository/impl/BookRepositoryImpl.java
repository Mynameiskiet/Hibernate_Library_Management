package com.tuankiet.repository.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.common.Sort;
import com.tuankiet.entities.Book;
import com.tuankiet.entities.BorrowingStatus;
import com.tuankiet.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public Book save(Book book) {
        Session session = getCurrentSession();
        if (book.getId() == null) {
            session.persist(book);
        } else {
            session.merge(book);
        }
        return book;
    }
    
    @Override
    public Optional<Book> findById(Long id) {
        Session session = getCurrentSession();
        Book book = session.get(Book.class, id);
        return Optional.ofNullable(book);
    }
    
    @Override
    public List<Book> findAll() {
        Session session = getCurrentSession();
        return session.createQuery("FROM Book", Book.class).list();
    }
    
    @Override
    public void delete(Book book) {
        Session session = getCurrentSession();
        session.remove(book);
    }
    
    @Override
    public boolean existsById(Long id) {
        Session session = getCurrentSession();
        Long count = session.createQuery(
            "SELECT COUNT(b) FROM Book b WHERE b.id = :id", Long.class)
            .setParameter("id", id)
            .uniqueResult();
        return count > 0;
    }
    
    @Override
    public Page<Book> search(String title, String category, Boolean available, 
                           Long authorId, LocalDateTime createdFrom, LocalDateTime createdTo,
                           PageRequest pageRequest) {
        Session session = getCurrentSession();
        
        StringBuilder hql = new StringBuilder("FROM Book b");
        StringBuilder whereClause = new StringBuilder();
        
        if (authorId != null) {
            hql.append(" JOIN b.authors a");
        }
        
        if (title != null && !title.trim().isEmpty()) {
            addWhereCondition(whereClause, "LOWER(b.title) LIKE LOWER(:title)");
        }
        if (category != null && !category.trim().isEmpty()) {
            addWhereCondition(whereClause, "LOWER(b.category) LIKE LOWER(:category)");
        }
        if (available != null) {
            addWhereCondition(whereClause, "b.available = :available");
        }
        if (authorId != null) {
            addWhereCondition(whereClause, "a.id = :authorId");
        }
        if (createdFrom != null) {
            addWhereCondition(whereClause, "b.createdDate >= :createdFrom");
        }
        if (createdTo != null) {
            addWhereCondition(whereClause, "b.createdDate <= :createdTo");
        }
        
        if (whereClause.length() > 0) {
            hql.append(" WHERE ").append(whereClause);
        }
        
        // Add sorting
        if (pageRequest.getSorts() != null && !pageRequest.getSorts().isEmpty()) {
            hql.append(" ORDER BY ");
            for (int i = 0; i < pageRequest.getSorts().size(); i++) {
                Sort sort = pageRequest.getSorts().get(i);
                if (i > 0) hql.append(", ");
                hql.append("b.").append(sort.getField()).append(" ").append(sort.getDirection());
            }
        }
        
        Query<Book> query = session.createQuery(hql.toString(), Book.class);
        setParameters(query, title, category, available, authorId, createdFrom, createdTo);
        
        // Get total count
        String countHql = hql.toString().replaceFirst("FROM Book b", "SELECT COUNT(DISTINCT b) FROM Book b");
        Query<Long> countQuery = session.createQuery(countHql, Long.class);
        setParameters(countQuery, title, category, available, authorId, createdFrom, createdTo);
        Long totalElements = countQuery.uniqueResult();
        
        // Apply pagination
        query.setFirstResult(pageRequest.getOffset());
        query.setMaxResults(pageRequest.getSize());
        
        List<Book> content = query.list();
        
        return new Page<>(content, totalElements, pageRequest.getPage(), pageRequest.getSize());
    }
    
    @Override
    public List<Book> findByAuthor(Long authorId, int limit) {
        Session session = getCurrentSession();
        return session.createQuery(
            "SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE a.id = :authorId", Book.class)
            .setParameter("authorId", authorId)
            .setMaxResults(limit)
            .list();
    }
    
    @Override
    public List<Book> findTopBorrowed(int limit) {
        Session session = getCurrentSession();
        return session.createQuery(
            "SELECT b FROM Book b JOIN b.borrowings br " +
            "GROUP BY b ORDER BY COUNT(br) DESC", Book.class)
            .setMaxResults(limit)
            .list();
    }
    
    @Override
    public List<Book> findOverdueByDays(int days) {
        Session session = getCurrentSession();
        return session.createNativeQuery(
            "EXEC GetOverdueBooksByDays :days", Book.class)
            .setParameter("days", days)
            .list();
    }
    
    @Override
    public boolean hasActiveBorrowings(Long bookId) {
        Session session = getCurrentSession();
        Long count = session.createQuery(
            "SELECT COUNT(br) FROM Borrowing br WHERE br.book.id = :bookId AND br.status = :status", Long.class)
            .setParameter("bookId", bookId)
            .setParameter("status", BorrowingStatus.BORROWED)
            .uniqueResult();
        return count > 0;
    }
    
    @Override
    public void bulkUpdateAvailability(List<Long> bookIds, boolean available) {
        Session session = getCurrentSession();
        session.createQuery("UPDATE Book SET available = :available WHERE id IN (:ids)")
            .setParameter("available", available)
            .setParameterList("ids", bookIds)
            .executeUpdate();
    }
    
    @Override
    public void bulkSave(List<Book> books) {
        Session session = getCurrentSession();
        for (int i = 0; i < books.size(); i++) {
            session.persist(books.get(i));
            if (i % 20 == 0) { // Batch size
                session.flush();
                session.clear();
            }
        }
    }
    
    private void addWhereCondition(StringBuilder whereClause, String condition) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(condition);
    }
    
    private void setParameters(Query<?> query, String title, String category, Boolean available, 
                             Long authorId, LocalDateTime createdFrom, LocalDateTime createdTo) {
        if (title != null && !title.trim().isEmpty()) {
            query.setParameter("title", "%" + title + "%");
        }
        if (category != null && !category.trim().isEmpty()) {
            query.setParameter("category", "%" + category + "%");
        }
        if (available != null) {
            query.setParameter("available", available);
        }
        if (authorId != null) {
            query.setParameter("authorId", authorId);
        }
        if (createdFrom != null) {
            query.setParameter("createdFrom", createdFrom);
        }
        if (createdTo != null) {
            query.setParameter("createdTo", createdTo);
        }
    }
}
