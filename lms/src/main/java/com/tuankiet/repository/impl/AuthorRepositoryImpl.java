package com.tuankiet.repository.impl;

import com.tuankiet.entities.Author;
import com.tuankiet.repository.AuthorRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public Author save(Author author) {
        Session session = getCurrentSession();
        if (author.getId() == null) {
            session.persist(author);
        } else {
            session.merge(author);
        }
        return author;
    }
    
    @Override
    public Optional<Author> findById(Long id) {
        Session session = getCurrentSession();
        Author author = session.get(Author.class, id);
        return Optional.ofNullable(author);
    }
    
    @Override
    public List<Author> findAll() {
        Session session = getCurrentSession();
        return session.createQuery("FROM Author", Author.class).list();
    }
    
    @Override
    public void delete(Author author) {
        Session session = getCurrentSession();
        session.remove(author);
    }
    
    @Override
    public boolean existsById(Long id) {
        Session session = getCurrentSession();
        Long count = session.createQuery(
            "SELECT COUNT(a) FROM Author a WHERE a.id = :id", Long.class)
            .setParameter("id", id)
            .uniqueResult();
        return count > 0;
    }
    
    @Override
    public List<Author> findByIds(List<Long> ids) {
        Session session = getCurrentSession();
        return session.createQuery(
            "FROM Author WHERE id IN (:ids)", Author.class)
            .setParameterList("ids", ids)
            .list();
    }
}
