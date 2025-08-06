package com.tuankiet.service;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.request.MemberCreateRequest;
import com.tuankiet.dto.request.MemberUpdateRequest;
import com.tuankiet.dto.response.MemberResponse;
import java.util.Optional;

public interface MemberService {
    
    MemberResponse register(MemberCreateRequest request);
    
    MemberResponse getById(Long id);
    
    MemberResponse update(Long id, MemberUpdateRequest request);
    
    void delete(Long id);
    
    Optional<MemberResponse> findByEmail(String email);
    
    Page<MemberResponse> search(String name, String email, String phone, PageRequest pageRequest);
    
    boolean isEligibleToBorrow(Long memberId);
    
    int countActiveBorrowings(Long memberId);
}
