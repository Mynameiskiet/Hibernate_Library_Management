package com.tuankiet.cli.menus;

import com.tuankiet.cli.helpers.InputHelper;
import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.common.Sort;
import com.tuankiet.dto.common.SortCriteria;
import com.tuankiet.dto.common.SortDirection;
import com.tuankiet.dto.request.CreateBorrowingRequest;
import com.tuankiet.dto.response.BorrowingResponse;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.dto.search.BorrowingSearchCriteria;
import com.tuankiet.enums.BorrowingStatus;
import com.tuankiet.services.BookService;
import com.tuankiet.services.BorrowingService;
import com.tuankiet.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * CLI menu for borrowing management
 * 
 * @author tuankiet
 * @since 1.0.0
 */
@Component
public class BorrowingMenu {
    
    private static final Logger logger = LoggerFactory.getLogger(BorrowingMenu.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final InputHelper inputHelper;
    private final BorrowingService borrowingService;
    private final BookService bookService;
    private final MemberService memberService;
    
    @Autowired
    public BorrowingMenu(InputHelper inputHelper, BorrowingService borrowingService, 
                        BookService bookService, MemberService memberService) {
        this.inputHelper = inputHelper;
        this.borrowingService = borrowingService;
        this.bookService = bookService;
        this.memberService = memberService;
    }
    
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📋 BORROWING & RETURNS");
            System.out.println("=".repeat(50));
            System.out.println("1. 📚 Borrow Book");
            System.out.println("2. 📤 Return Book");
            System.out.println("3. ⏰ Extend Due Date");
            System.out.println("4. 👁️  View Borrowing Details");
            System.out.println("5. 🔍 Search Borrowings");
            System.out.println("6. ⚠️  View Overdue Borrowings");
            System.out.println("7. 👤 View Member Borrowings");
            System.out.println("0. ⬅️  Back to Main Menu");
            System.out.println("=".repeat(50));
            
            choice = inputHelper.readInt("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1 -> borrowBook();
                    case 2 -> returnBook();
                    case 3 -> extendDueDate();
                    case 4 -> viewBorrowing();
                    case 5 -> searchBorrowings();
                    case 6 -> viewOverdueBorrowings();
                    case 7 -> viewMemberBorrowings();
                    case 0 -> logger.info("Returning to main menu from Borrowing Management.");
                    default -> System.out.println("❌ Invalid choice! Please try again.");
                }
                
            } catch (Exception e) {
                logger.error("Error in borrowing menu", e);
                System.out.println("❌ An error occurred: " + e.getMessage());
            }
        } while (choice != 0);
    }
    
    private void borrowBook() {
        System.out.println("\n📚 BORROW BOOK");
        System.out.println("-".repeat(30));
        
        try {
            UUID memberId = inputHelper.readUuid("Enter Member ID: ");
            if (memberId == null) {
                System.out.println("❌ Invalid Member ID.");
                return;
            }
            
            UUID bookId = inputHelper.readUuid("Enter Book ID: ");
            if (bookId == null) {
                System.out.println("❌ Invalid Book ID.");
                return;
            }
            
            int daysToReturn = inputHelper.readInt("Days to return (default 14): ");
            if (daysToReturn <= 0) daysToReturn = 14;
            
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(daysToReturn);
            
            CreateBorrowingRequest request = new CreateBorrowingRequest(bookId, memberId, borrowDate, dueDate);
            BorrowingResponse response = borrowingService.create(request);
            
            System.out.println("✅ Book borrowed successfully!");
            displayBorrowingDetails(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error borrowing book: " + e.getMessage());
        }
    }
    
    private void returnBook() {
        System.out.println("\n📤 RETURN BOOK");
        System.out.println("-".repeat(30));
        
        try {
            UUID borrowingId = inputHelper.readUuid("Enter Borrowing ID: ");
            if (borrowingId == null) {
                System.out.println("❌ Invalid Borrowing ID.");
                return;
            }
            
            BorrowingResponse borrowing = borrowingService.getById(borrowingId);
            System.out.println("Borrowing to return:");
            displayBorrowingDetails(borrowing);
            
            String confirm = inputHelper.readString("Confirm return? (yes/no): ", false);
            if ("yes".equalsIgnoreCase(confirm.trim())) {
                BorrowingResponse response = borrowingService.returnBook(borrowingId);
                System.out.println("✅ Book returned successfully!");
                displayBorrowingDetails(response);
            } else {
                System.out.println("❌ Return operation cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error returning book: " + e.getMessage());
        }
    }
    
    private void extendDueDate() {
        System.out.println("\n⏰ EXTEND DUE DATE");
        System.out.println("-".repeat(30));
        
        try {
            UUID borrowingId = inputHelper.readUuid("Enter Borrowing ID: ");
            if (borrowingId == null) {
                System.out.println("❌ Invalid Borrowing ID.");
                return;
            }
            
            BorrowingResponse borrowing = borrowingService.getById(borrowingId);
            System.out.println("Current borrowing details:");
            displayBorrowingDetails(borrowing);
            
            if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
                System.out.println("❌ Cannot extend due date for returned books.");
                return;
            }
            
            LocalDate newDueDate = inputHelper.readDate("Enter new due date");
            if (newDueDate == null || !newDueDate.isAfter(borrowing.getDueDate())) {
                System.out.println("❌ New due date must be after current due date.");
                return;
            }
            
            // Create update request with new due date
            // This would need an update method in BorrowingService
            System.out.println("✅ Due date extension feature needs to be implemented in service layer.");
            
        } catch (Exception e) {
            System.out.println("❌ Error extending due date: " + e.getMessage());
        }
    }
    
    private void viewBorrowing() {
        System.out.println("\n👁️ VIEW BORROWING DETAILS");
        System.out.println("-".repeat(30));
        
        try {
            UUID id = inputHelper.readUuid("Enter Borrowing ID: ");
            if (id == null) {
                System.out.println("❌ Invalid Borrowing ID.");
                return;
            }
            
            BorrowingResponse borrowing = borrowingService.getById(id);
            displayBorrowingDetails(borrowing);
            
        } catch (Exception e) {
            System.out.println("❌ Error viewing borrowing: " + e.getMessage());
        }
    }
    
    private void searchBorrowings() {
        System.out.println("\n🔍 SEARCH BORROWINGS");
        System.out.println("-".repeat(30));
        
        try {
            BorrowingSearchCriteria criteria = new BorrowingSearchCriteria();
            
            String memberIdInput = inputHelper.readString("Member ID (optional): ", true);
            if (!memberIdInput.trim().isEmpty()) {
                try {
                    criteria.setMemberId(UUID.fromString(memberIdInput));
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Invalid Member ID format. Ignoring member filter.");
                }
            }
            
            String statusInput = inputHelper.readString("Status (BORROWED/RETURNED, optional): ", true);
            if (!statusInput.trim().isEmpty()) {
                try {
                    criteria.setStatus(BorrowingStatus.valueOf(statusInput.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Invalid status. Ignoring status filter.");
                }
            }
            
            int page = inputHelper.readInt("Page number (0-based, default 0): ");
            int size = inputHelper.readInt("Page size (default 10): ");
            if (size <= 0) size = 10;
            
            Sort sort = Sort.by(new SortCriteria("borrowDate", SortDirection.DESC));
            PageRequest pageRequest = new PageRequest(page, size, sort);
            Page<BorrowingResponse> result = borrowingService.search(criteria, pageRequest);
            
            displayBorrowingList(result);
            
        } catch (Exception e) {
            System.out.println("❌ Error searching borrowings: " + e.getMessage());
        }
    }
    
    private void viewOverdueBorrowings() {
        System.out.println("\n⚠️ OVERDUE BORROWINGS");
        System.out.println("-".repeat(30));
        
        try {
            BorrowingSearchCriteria criteria = new BorrowingSearchCriteria();
            criteria.setStatus(BorrowingStatus.BORROWED);
            criteria.setDueDateTo(LocalDate.now().minusDays(1)); // Books due before yesterday
            
            Sort sort = Sort.by(new SortCriteria("dueDate", SortDirection.ASC));
            PageRequest pageRequest = new PageRequest(0, 50, sort);
            Page<BorrowingResponse> overdueBorrowings = borrowingService.search(criteria, pageRequest);
            
            if (overdueBorrowings.getContent().isEmpty()) {
                System.out.println("✅ No overdue borrowings found!");
                return;
            }
            
            System.out.println("Found " + overdueBorrowings.getTotalElements() + " overdue borrowings:");
            
            for (BorrowingResponse borrowing : overdueBorrowings.getContent()) {
                System.out.println("\n" + "-".repeat(40));
                displayBorrowingDetails(borrowing);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error viewing overdue borrowings: " + e.getMessage());
        }
    }
    
    private void viewMemberBorrowings() {
        System.out.println("\n👤 MEMBER BORROWINGS");
        System.out.println("-".repeat(30));
        
        try {
            UUID memberId = inputHelper.readUuid("Enter Member ID: ");
            if (memberId == null) {
                System.out.println("❌ Invalid Member ID.");
                return;
            }
            
            BorrowingSearchCriteria criteria = new BorrowingSearchCriteria();
            criteria.setMemberId(memberId);
            
            Sort sort = Sort.by(new SortCriteria("borrowDate", SortDirection.DESC));
            PageRequest pageRequest = new PageRequest(0, 20, sort);
            Page<BorrowingResponse> borrowings = borrowingService.search(criteria, pageRequest);
            
            if (borrowings.getContent().isEmpty()) {
                System.out.println("No borrowings found for this member.");
                return;
            }
            
            System.out.println("Borrowings for member " + memberId + ":");
            
            for (BorrowingResponse borrowing : borrowings.getContent()) {
                System.out.println("\n" + "-".repeat(40));
                displayBorrowingDetails(borrowing);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error viewing member borrowings: " + e.getMessage());
        }
    }
    
    private void displayBorrowingDetails(BorrowingResponse borrowing) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 BORROWING DETAILS");
        System.out.println("=".repeat(50));
        System.out.println("ID: " + borrowing.getId());
        
        if (borrowing.getMember() != null) {
            System.out.println("Member: " + borrowing.getMember().getFirstName() + " " + 
                              borrowing.getMember().getLastName() + " (ID: " + borrowing.getMember().getId() + ")");
        }
        
        if (borrowing.getBook() != null) {
            System.out.println("Book: " + borrowing.getBook().getTitle() + " (ID: " + borrowing.getBook().getId() + ")");
        }
        
        System.out.println("Status: " + borrowing.getStatus());
        System.out.println("Borrow Date: " + borrowing.getBorrowDate().format(DATE_FORMATTER));
        System.out.println("Due Date: " + borrowing.getDueDate().format(DATE_FORMATTER));
        
        if (borrowing.getReturnDate() != null) {
            System.out.println("Return Date: " + borrowing.getReturnDate().format(DATE_FORMATTER));
        }
        
        // Check if overdue
        if (borrowing.getStatus() == BorrowingStatus.BORROWED && 
            borrowing.getDueDate().isBefore(LocalDate.now())) {
            long daysOverdue = LocalDate.now().toEpochDay() - borrowing.getDueDate().toEpochDay();
            System.out.println("⚠️ OVERDUE: " + daysOverdue + " days");
        }
        
        System.out.println("=".repeat(50));
    }
    
    private void displayBorrowingList(Page<BorrowingResponse> page) {
        if (page.getContent().isEmpty()) {
            System.out.println("No borrowings found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("%-36s %-20s %-15s %-15s %-10s%n", 
                         "ID", "Member", "Borrow Date", "Due Date", "Status");
        System.out.println("=".repeat(90));
        
        for (BorrowingResponse borrowing : page.getContent()) {
            String memberName = borrowing.getMember() != null ? 
                               borrowing.getMember().getFirstName() + " " + 
                               borrowing.getMember().getLastName() : "Unknown";
            if (memberName.length() > 20) {
                memberName = memberName.substring(0, 17) + "...";
            }
            
            System.out.printf("%-36s %-20s %-15s %-15s %-10s%n",
                borrowing.getId().toString(),
                memberName,
                borrowing.getBorrowDate().format(DATE_FORMATTER),
                borrowing.getDueDate().format(DATE_FORMATTER),
                borrowing.getStatus());
        }
        
        System.out.println("=".repeat(90));
        System.out.printf("Page %d of %d | Total: %d borrowings%n",
            page.getCurrentPage() + 1,
            page.getTotalPages(),
            page.getTotalElements());
    }
}
