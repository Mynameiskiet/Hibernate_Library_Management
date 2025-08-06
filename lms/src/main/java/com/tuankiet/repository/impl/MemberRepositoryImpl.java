package com.tuankiet.repository.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.common.Sort;
import com.tuankiet.entities.BorrowingStatus;
import com.tuankiet.entities.Member;
import com.tuankiet.repository.MemberRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public Member save(Member member) {
        Session session = getCurrentSession();
        if (member.getId() == null) {
            session.persist(member);
        } else {
            session.merge(member);
        }
        return member;
    }
    
    @Override
    public Optional<Member> findById(Long id) {
        Session session = getCurrentSession();
        Member member = session.get(Member.class, id);
        return Optional.ofNullable(member);
    }
    
    @Override
    public Optional<Member> findByEmail(String email) {
        Session session = getCurrentSession();
        Member member = session.createQuery(
            "FROM Member WHERE email = :email", Member.class)
            .setParameter("email", email)
            .uniqueResult();
        return Optional.ofNullable(member);
    }
    
    @Override
    public List<Member> findAll() {
        Session session = getCurrentSession();
        return session.createQuery("FROM Member", Member.class).list();
    }
    
    @Override
    public void delete(Member member) {
        Session session = getCurrentSession();
        session.remove(member);
    }
    
    @Override
    public boolean existsById(Long id) {
        Session session = getCurrentSession();
        Long count = session.createQuery(
            "SELECT COUNT(m) FROM Member m WHERE m.id = :id", Long.class)
            .setParameter("id", id)
            .uniqueResult();
        return count > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        Session session = getCurrentSession();
        Long count = session.createQuery(
            "SELECT COUNT(m) FROM Member m WHERE m.email = :email", Long.class)
            .setParameter("email", email)
            .uniqueResult();
        return count > 0;
    }
    
    @Override
    public Page<Member> search(String name, String email, String phone, PageRequest pageRequest) {
        Session session = getCurrentSession();
        
        StringBuilder hql = new StringBuilder("FROM Member m");
        StringBuilder whereClause = new StringBuilder();
        
        if (name != null && !name.trim().isEmpty()) {
            addWhereCondition(whereClause, "LOWER(m.name) LIKE LOWER(:name)");
        }
        if (email != null && !email.trim().isEmpty()) {
            addWhereCondition(whereClause, "LOWER(m.email) LIKE LOWER(:email)");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            addWhereCondition(whereClause, "m.phone LIKE :phone");
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
                hql.append("m.").append(sort.getField()).append(" ").append(sort.getDirection());
            }
        }
        
        Query<Member> query = session.createQuery(hql.toString(), Member.class);
        setParameters(query, name, email, phone);
        
        // Get total count
        String countHql = hql.toString().replaceFirst("FROM Member m", "SELECT COUNT(m) FROM Member m");
        Query<Long> countQuery = session.createQuery(countHql, Long.class);
        setParameters(countQuery, name, email, phone);
        Long totalElements = countQuery.uniqueResult();
        
        // Apply pagination
        query.setFirstResult(pageRequest.getOffset());
        query.setMaxResults(pageRequest.getSize());
        
        List<Member> content = query.list();
        
        return new Page<>(content, totalElements, pageRequest.getPage(), pageRequest.getSize());
    }
    
    @Override
    public int countActiveBorrowings(Long memberId) {
        Session session = getCurrentSession();
        Long count = session.createQuery(
            "SELECT COUNT(br) FROM Borrowing br WHERE br.member.id = :memberId AND br.status = :status", Long.class)
            .setParameter("memberId", memberId)
            .setParameter("status", BorrowingStatus.BORROWED)
            .uniqueResult();
        return count.intValue();
    }
    
    @Override
    public boolean hasActiveBorrowings(Long memberId) {
        return countActiveBorrowings(memberId) > 0;
    }
    
    private void addWhereCondition(StringBuilder whereClause, String condition) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(condition);
    }
    
    private void setParameters(Query<?> query, String name, String email, String phone) {
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            query.setParameter("email", "%" + email + "%");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            query.setParameter("phone", "%" + phone + "%");
        }
    }
}
