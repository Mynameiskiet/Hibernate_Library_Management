package com.tuankiet.services.impl;

import com.tuankiet.dto.request.CreateAuthorRequest;
import com.tuankiet.dto.request.CreateBookRequest;
import com.tuankiet.dto.request.CreateBorrowingRequest;
import com.tuankiet.dto.request.CreateMemberRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.response.BorrowingResponse;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.enums.BookCategory;
import com.tuankiet.services.AuthorService;
import com.tuankiet.services.BookService;
import com.tuankiet.services.BorrowingService;
import com.tuankiet.services.MemberService;
import com.tuankiet.services.SampleDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the SampleDataService interface.
 * Provides methods to generate and clear sample data for the LMS.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class SampleDataServiceImpl implements SampleDataService {

    private static final Logger logger = LoggerFactory.getLogger(SampleDataServiceImpl.class);

    private final BookService bookService;
    private final AuthorService authorService;
    private final MemberService memberService;
    private final BorrowingService borrowingService;

    @Autowired
    public SampleDataServiceImpl(BookService bookService, AuthorService authorService, MemberService memberService, BorrowingService borrowingService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.memberService = memberService;
        this.borrowingService = borrowingService;
    }

    @Override
    @Transactional
    public void generateSampleData() {
        logger.info("Generating sample data...");

        // Clear existing data first to avoid duplicates on repeated runs
        clearAllData();

        // 1. Create Authors
        AuthorResponse author1 = authorService.create(new CreateAuthorRequest("Stephen", "King", "Prolific American author of horror, supernatural fiction, suspense, crime, science-fiction, and fantasy novels."));
        AuthorResponse author2 = authorService.create(new CreateAuthorRequest("J.K.", "Rowling", "British author, best known for writing the Harry Potter fantasy series."));
        AuthorResponse author3 = authorService.create(new CreateAuthorRequest("George", "Orwell", "English novelist, essayist, journalist, and critic."));
        AuthorResponse author4 = authorService.create(new CreateAuthorRequest("Agatha", "Christie", "English writer known for her 66 detective novels and 14 short story collections."));
        AuthorResponse author5 = authorService.create(new CreateAuthorRequest("Isaac", "Asimov", "American writer and professor of biochemistry, known for his works of science fiction and popular science."));

        // 2. Create Books
        BookResponse book1 = bookService.create(new CreateBookRequest("It", "978-0-451-16951-8", 1986, BookCategory.HORROR, 3, author1.getId()));
        BookResponse book2 = bookService.create(new CreateBookRequest("Harry Potter and the Sorcerer's Stone", "978-0-7475-3274-3", 1997, BookCategory.FANTASY, 5, author2.getId()));
        BookResponse book3 = bookService.create(new CreateBookRequest("1984", "978-0-452-28423-4", 1949, BookCategory.FICTION, 2, author3.getId()));
        BookResponse book4 = bookService.create(new CreateBookRequest("And Then There Were None", "978-0-06-207348-8", 1939, BookCategory.MYSTERY, 4, author4.getId()));
        BookResponse book5 = bookService.create(new CreateBookRequest("I, Robot", "978-0-553-38256-3", 1950, BookCategory.SCIENCE, 3, author5.getId()));
        BookResponse book6 = bookService.create(new CreateBookRequest("The Shining", "978-0-385-12167-5", 1977, BookCategory.HORROR, 2, author1.getId()));
        BookResponse book7 = bookService.create(new CreateBookRequest("Animal Farm", "978-0-451-52634-2", 1945, BookCategory.FICTION, 3, author3.getId()));

        // 3. Create Members
        MemberResponse member1 = memberService.create(new CreateMemberRequest("Alice", "Smith", "alice.smith@example.com", "111-222-3333", "123 Main St"));
        MemberResponse member2 = memberService.create(new CreateMemberRequest("Bob", "Johnson", "bob.j@example.com", "444-555-6666", "456 Oak Ave"));
        MemberResponse member3 = memberService.create(new CreateMemberRequest("Charlie", "Brown", "charlie.b@example.com", "777-888-9999", "789 Pine Ln"));
        MemberResponse member4 = memberService.create(new CreateMemberRequest("Diana", "Prince", "diana.p@example.com", "101-202-3030", "101 Amazon Way"));

        // 4. Create Borrowing Records
        // Alice borrows "It"
        borrowingService.create(new CreateBorrowingRequest(book1.getId(), member1.getId(), LocalDate.now(), LocalDate.now().plusDays(14)));
        // Bob borrows "Harry Potter"
        borrowingService.create(new CreateBorrowingRequest(book2.getId(), member2.getId(), LocalDate.now(), LocalDate.now().plusDays(21)));
        // Charlie borrows "1984" (overdue)
        borrowingService.create(new CreateBorrowingRequest(book3.getId(), member3.getId(), LocalDate.now().minusMonths(1), LocalDate.now().minusDays(7)));
        // Diana borrows "And Then There Were None"
        borrowingService.create(new CreateBorrowingRequest(book4.getId(), member4.getId(), LocalDate.now().minusDays(5), LocalDate.now().plusDays(9)));
        // Alice borrows "The Shining"
        borrowingService.create(new CreateBorrowingRequest(book6.getId(), member1.getId(), LocalDate.now().minusDays(2), LocalDate.now().plusDays(12)));

        logger.info("Sample data generation complete.");
    }

    @Override
    @Transactional
    public void clearAllData() {
        logger.info("Clearing all existing data...");
        // Delete borrowings first due to foreign key constraints
        List<BorrowingResponse> allBorrowings = borrowingService.getAll();
        for (BorrowingResponse borrowing : allBorrowings) {
            borrowingService.delete(borrowing.getId());
        }
        logger.debug("Deleted all borrowing records.");

        // Then delete books
        List<BookResponse> allBooks = bookService.getAll();
        for (BookResponse book : allBooks) {
            bookService.delete(book.getId());
        }
        logger.debug("Deleted all book records.");

        // Then delete members
        List<MemberResponse> allMembers = memberService.getAll();
        for (MemberResponse member : allMembers) {
            memberService.delete(member.getId());
        }
        logger.debug("Deleted all member records.");

        // Finally delete authors
        List<AuthorResponse> allAuthors = authorService.getAll();
        for (AuthorResponse author : allAuthors) {
            authorService.delete(author.getId());
        }
        logger.debug("Deleted all author records.");

        logger.info("All existing data cleared.");
    }
}
