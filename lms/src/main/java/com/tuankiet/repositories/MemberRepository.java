package com.tuankiet.repositories;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.search.MemberSearchCriteria;
import com.tuankiet.entities.Member;

import java.util.Optional;

/**
* Repository interface for Member entities.
* Extends BaseRepository for common CRUD operations.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface MemberRepository extends BaseRepository<Member> {

  /**
   * Finds a member by their email address.
   * @param email The email address of the member.
   * @return An Optional containing the member if found, or empty otherwise.
   */
  Optional<Member> findByEmail(String email);

  /**
   * Checks if a member with the given email address exists.
   * @param email The email to check.
   * @return True if a member with the email exists, false otherwise.
   */
  boolean existsByEmail(String email);

  /**
   * Retrieves a paginated list of members based on search criteria.
   * @param criteria The search criteria for members.
   * @param pageRequest The pagination and sorting information.
   * @return A Page object containing the requested members.
   */
  Page<Member> searchMembers(MemberSearchCriteria criteria, PageRequest pageRequest);
}
