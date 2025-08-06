package com.tuankiet.repository;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.entities.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    
    Member save(Member member);
    
    Optional<Member> findById(Long id);
    
    Optional<Member> findByEmail(String email);
    
    List<Member> findAll();
    
    void delete(Member member);
    
    boolean existsById(Long id);
    
    boolean existsByEmail(String email);
    
    Page<Member> search(String name, String email, String phone, PageRequest pageRequest);
    
    int countActiveBorrowings(Long memberId);
    
    boolean hasActiveBorrowings(Long memberId);
}
