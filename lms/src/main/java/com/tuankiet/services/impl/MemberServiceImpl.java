package com.tuankiet.services.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.request.CreateMemberRequest;
import com.tuankiet.dto.request.UpdateMemberRequest;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.dto.search.MemberSearchCriteria;
import com.tuankiet.entities.Member;
import com.tuankiet.exceptions.BusinessRuleViolationException;
import com.tuankiet.exceptions.DuplicateEntityException;
import com.tuankiet.exceptions.EntityNotFoundException;
import com.tuankiet.repositories.BorrowingRepository;
import com.tuankiet.repositories.MemberRepository;
import com.tuankiet.services.MemberService;
import com.tuankiet.services.validation.ValidationService;
import com.tuankiet.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
* Implementation of the MemberService interface.
* Handles business logic for Member entities.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Service
public class MemberServiceImpl extends BaseServiceImpl<Member, MemberResponse, CreateMemberRequest, UpdateMemberRequest, MemberSearchCriteria> implements MemberService {

  private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

  private final MemberRepository memberRepository;
  private final BorrowingRepository borrowingRepository; // To check for active borrowings before deletion

  @Autowired
  public MemberServiceImpl(MemberRepository memberRepository, BorrowingRepository borrowingRepository, ValidationService validationService, MapperUtil mapperUtil) {
      super(memberRepository, validationService, mapperUtil);
      this.memberRepository = memberRepository;
      this.borrowingRepository = borrowingRepository;
  }

  @Override
  @Transactional
  public MemberResponse create(CreateMemberRequest createRequest) {
      logger.info("Attempting to create new member with email: {}", createRequest.getEmail());
      validationService.validate(createRequest);

      if (memberRepository.existsByEmail(createRequest.getEmail())) {
          throw new DuplicateEntityException("Member", "email", createRequest.getEmail());
      }

      Member member = mapperUtil.map(createRequest, Member.class);
      Member savedMember = memberRepository.save(member);
      logger.info("Successfully created member with ID: {}", savedMember.getId());
      return mapperUtil.map(savedMember, MemberResponse.class);
  }

  @Override
  @Transactional(readOnly = true)
  public MemberResponse getById(UUID id) {
      logger.debug("Retrieving member with ID: {}", id);
      Member member = memberRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Member", id));
      return mapperUtil.map(member, MemberResponse.class);
  }

  @Override
  @Transactional(readOnly = true)
  public MemberResponse getByEmail(String email) {
      logger.debug("Retrieving member with email: {}", email);
      Member member = memberRepository.findByEmail(email)
              .orElseThrow(() -> new EntityNotFoundException("Member with email " + email + " not found."));
      return mapperUtil.map(member, MemberResponse.class);
  }

  @Override
  @Transactional
  public MemberResponse update(UpdateMemberRequest updateRequest) {
      logger.info("Attempting to update member with ID: {}", updateRequest.getId());
      validationService.validate(updateRequest);

      Member existingMember = memberRepository.findById(updateRequest.getId())
              .orElseThrow(() -> new EntityNotFoundException("Member", updateRequest.getId()));

      // Check for email uniqueness if it's changed and not the current member's email
      if (!existingMember.getEmail().equalsIgnoreCase(updateRequest.getEmail()) && memberRepository.existsByEmail(updateRequest.getEmail())) {
          throw new DuplicateEntityException("Member", "email", updateRequest.getEmail());
      }

      mapperUtil.map(updateRequest, existingMember);
      Member updatedMember = memberRepository.save(existingMember);
      logger.info("Successfully updated member with ID: {}", updatedMember.getId());
      return mapperUtil.map(updatedMember, MemberResponse.class);
  }

  @Override
  @Transactional
  public boolean delete(UUID id) {
      logger.info("Attempting to delete member with ID: {}", id);
      // Check if there are any active borrowings for this member
      long activeBorrowings = borrowingRepository.findByMemberId(id).stream()
              .filter(b -> b.getStatus() == com.tuankiet.enums.BorrowingStatus.BORROWED || b.getStatus() == com.tuankiet.enums.BorrowingStatus.OVERDUE)
              .count();

      if (activeBorrowings > 0) {
          throw new BusinessRuleViolationException("Cannot delete member with ID " + id + " as there are " + activeBorrowings + " active borrowings.");
      }

      boolean deleted = memberRepository.deleteById(id);
      if (deleted) {
          logger.info("Successfully deleted member with ID: {}", id);
      } else {
          logger.warn("Failed to delete member: ID {} not found.", id);
      }
      return deleted;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MemberResponse> getAll() {
      logger.debug("Retrieving all members.");
      return memberRepository.findAll().stream()
              .map(member -> mapperUtil.map(member, MemberResponse.class))
              .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MemberResponse> search(MemberSearchCriteria criteria, PageRequest pageRequest) {
      logger.debug("Searching members with criteria: {} and page request: {}", criteria, pageRequest);
      validationService.validate(pageRequest);
      Page<Member> memberPage = memberRepository.searchMembers(criteria, pageRequest);
      List<MemberResponse> content = memberPage.getContent().stream()
              .map(member -> mapperUtil.map(member, MemberResponse.class))
              .collect(Collectors.toList());
      return new Page<>(content, memberPage.getTotalElements(), memberPage.getCurrentPage(), memberPage.getPageSize());
  }
}
