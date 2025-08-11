package com.tuankiet.services.impl;

import com.tuankiet.dto.response.BookBorrowingStats;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.response.BorrowingResponse;
import com.tuankiet.dto.response.MemberBorrowingStats;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.entities.Book;
import com.tuankiet.entities.Borrowing;
import com.tuankiet.entities.Member;
import com.tuankiet.enums.BorrowingStatus;
import com.tuankiet.exceptions.EntityNotFoundException;
import com.tuankiet.repositories.BookRepository;
import com.tuankiet.repositories.BorrowingRepository;
import com.tuankiet.repositories.MemberRepository;
import com.tuankiet.services.ReportService;
import com.tuankiet.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the ReportService interface.
 * Provides methods for generating various reports related to library activities.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowingRepository borrowingRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public ReportServiceImpl(BookRepository bookRepository, MemberRepository memberRepository, BorrowingRepository borrowingRepository, MapperUtil mapperUtil) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowingRepository = borrowingRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> getCurrentlyBorrowedBooks() {
        logger.info("Generating report for currently borrowed books.");
        List<Borrowing> borrowedBooks = borrowingRepository.findByStatus(BorrowingStatus.BORROWED);
        List<BorrowingResponse> responses = new ArrayList<>();
        for (Borrowing borrowing : borrowedBooks) {
            responses.add(mapBorrowingToResponse(borrowing));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> getOverdueBooks() {
        logger.info("Generating report for overdue books.");
        List<Borrowing> overdueBooks = borrowingRepository.findByStatus(BorrowingStatus.OVERDUE);
        // Additionally, check for BORROWED books whose due date has passed
        List<Borrowing> currentlyBorrowed = borrowingRepository.findByStatus(BorrowingStatus.BORROWED);
        for (Borrowing borrowing : currentlyBorrowed) {
            if (borrowing.getDueDate().isBefore(LocalDate.now())) {
                overdueBooks.add(borrowing);
            }
        }
        
        List<BorrowingResponse> responses = new ArrayList<>();
        for (Borrowing borrowing : overdueBooks) {
            responses.add(mapBorrowingToResponse(borrowing));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> getMemberBorrowingHistory(UUID memberId) {
        logger.info("Generating borrowing history for member ID: {}", memberId);
        if (!memberRepository.existsById(memberId)) {
            throw new EntityNotFoundException("Member", memberId);
        }
        List<Borrowing> memberBorrowings = borrowingRepository.findByMemberId(memberId);
        List<BorrowingResponse> responses = new ArrayList<>();
        for (Borrowing borrowing : memberBorrowings) {
            responses.add(mapBorrowingToResponse(borrowing));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> getBookBorrowingHistory(UUID bookId) {
        logger.info("Generating borrowing history for book ID: {}", bookId);
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book", bookId);
        }
        List<Borrowing> bookBorrowings = borrowingRepository.findByBookId(bookId);
        List<BorrowingResponse> responses = new ArrayList<>();
        for (Borrowing borrowing : bookBorrowings) {
            responses.add(mapBorrowingToResponse(borrowing));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public BookBorrowingStats getBookBorrowingStatistics(UUID bookId) {
        logger.info("Generating borrowing statistics for book ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book", bookId));

        List<Borrowing> allBorrows = borrowingRepository.findByBookId(bookId);
        long totalBorrows = allBorrows.size();
        long currentBorrows = 0;
        long overdueBorrows = 0;
        
        for (Borrowing borrowing : allBorrows) {
            if (borrowing.getStatus() == BorrowingStatus.BORROWED) {
                currentBorrows++;
            }
            if (borrowing.getStatus() == BorrowingStatus.OVERDUE || 
                (borrowing.getStatus() == BorrowingStatus.BORROWED && borrowing.getDueDate().isBefore(LocalDate.now()))) {
                overdueBorrows++;
            }
        }

        return new BookBorrowingStats(book.getId(), book.getTitle(), book.getIsbn(), totalBorrows, currentBorrows, overdueBorrows);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberBorrowingStats getMemberBorrowingStatistics(UUID memberId) {
        logger.info("Generating borrowing statistics for member ID: {}", memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member", memberId));

        List<Borrowing> allBorrows = borrowingRepository.findByMemberId(memberId);
        long totalBorrows = allBorrows.size();
        long currentBorrows = 0;
        long overdueBorrows = 0;
        
        for (Borrowing borrowing : allBorrows) {
            if (borrowing.getStatus() == BorrowingStatus.BORROWED) {
                currentBorrows++;
            }
            if (borrowing.getStatus() == BorrowingStatus.OVERDUE || 
                (borrowing.getStatus() == BorrowingStatus.BORROWED && borrowing.getDueDate().isBefore(LocalDate.now()))) {
                overdueBorrows++;
            }
        }

        return new MemberBorrowingStats(member.getId(), member.getFirstName() + " " + member.getLastName(), member.getEmail(), totalBorrows, currentBorrows, overdueBorrows);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> getBooksBorrowedInDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Generating report for books borrowed between {} and {}.", startDate, endDate);
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range provided.");
        }

        List<Borrowing> allBorrowings = borrowingRepository.findAll();
        List<Borrowing> filteredBorrowings = new ArrayList<>();
        
        for (Borrowing borrowing : allBorrowings) {
            if (!borrowing.getBorrowDate().isBefore(startDate) && !borrowing.getBorrowDate().isAfter(endDate)) {
                filteredBorrowings.add(borrowing);
            }
        }

        List<BorrowingResponse> responses = new ArrayList<>();
        for (Borrowing borrowing : filteredBorrowings) {
            responses.add(mapBorrowingToResponse(borrowing));
        }
        return responses;
    }

    private BorrowingResponse mapBorrowingToResponse(Borrowing borrowing) {
        BorrowingResponse response = mapperUtil.map(borrowing, BorrowingResponse.class);
        if (borrowing.getBook() != null) {
            response.setBook(mapperUtil.map(borrowing.getBook(), BookResponse.class));
            if (borrowing.getBook().getAuthor() != null) {
                response.getBook().setAuthor(mapperUtil.map(borrowing.getBook().getAuthor(), com.tuankiet.dto.response.AuthorResponse.class));
            }
        }
        if (borrowing.getMember() != null) {
            response.setMember(mapperUtil.map(borrowing.getMember(), MemberResponse.class));
        }
        return response;
    }
}
