package com.tuankiet.services;

import com.tuankiet.dto.request.CreateMemberRequest;
import com.tuankiet.dto.request.UpdateMemberRequest;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.dto.search.MemberSearchCriteria;
import com.tuankiet.entities.Member;

/**
* Service interface for managing Member entities.
* Extends BaseService for common operations.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface MemberService extends BaseService<Member, MemberResponse, CreateMemberRequest, UpdateMemberRequest, MemberSearchCriteria> {

  /**
   * Finds a member by their email address.
   * @param email The email address of the member.
   * @return The response DTO of the found member.
   * @throws com.tuankiet.exceptions.EntityNotFoundException if the member is not found.
   */
  MemberResponse getByEmail(String email);
}
