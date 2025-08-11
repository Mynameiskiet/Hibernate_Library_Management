package com.tuankiet.cli.menus;

import com.tuankiet.cli.helpers.InputHelper;
import com.tuankiet.dto.response.BorrowingResponse;
import com.tuankiet.services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CLI menu for reports and analytics
 * 
 * @author tuankiet
 * @since 1.0.0
 */
@Component
public class ReportMenu {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportMenu.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final InputHelper inputHelper;
    private final ReportService reportService;
    
    @Autowired
    public ReportMenu(InputHelper inputHelper, ReportService reportService) {
        this.inputHelper = inputHelper;
        this.reportService = reportService;
    }
    
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📊 REPORTS & ANALYTICS");
            System.out.println("=".repeat(50));
            System.out.println("1. ⚠️  Overdue Books Report");
            System.out.println("2. 📚 Currently Borrowed Books");
            System.out.println("3. 📈 Books Borrowed in Date Range");
            System.out.println("0. ⬅️  Back to Main Menu");
            System.out.println("=".repeat(50));
            
            choice = inputHelper.readInt("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1 -> generateOverdueReport();
                    case 2 -> generateCurrentlyBorrowedReport();
                    case 3 -> generateDateRangeReport();
                    case 0 -> logger.info("Returning to main menu from Reports.");
                    default -> System.out.println("❌ Invalid choice! Please try again.");
                }
                
            } catch (Exception e) {
                logger.error("Error in report menu", e);
                System.out.println("❌ An error occurred: " + e.getMessage());
            }
        } while (choice != 0);
    }
    
    private void generateOverdueReport() {
        System.out.println("\n⚠️ OVERDUE BOOKS REPORT");
        System.out.println("=".repeat(50));
        
        try {
            List<BorrowingResponse> overdueBorrowings = reportService.getOverdueBooks();
            
            if (overdueBorrowings.isEmpty()) {
                System.out.println("✅ No overdue books found!");
                return;
            }
            
            System.out.println("Total overdue borrowings: " + overdueBorrowings.size());
            System.out.println("\nDetailed Report:");
            System.out.println("-".repeat(80));
            System.out.printf("%-36s %-20s %-25s %-15s%n", 
                             "Borrowing ID", "Member", "Book", "Days Overdue");
            System.out.println("-".repeat(80));
            
            for (BorrowingResponse borrowing : overdueBorrowings) {
                String memberName = borrowing.getMember() != null ? 
                                   borrowing.getMember().getFirstName() + " " + 
                                   borrowing.getMember().getLastName() : "Unknown";
                if (memberName.length() > 20) {
                    memberName = memberName.substring(0, 17) + "...";
                }
                
                String bookTitle = borrowing.getBook() != null ? 
                                  borrowing.getBook().getTitle() : "Unknown";
                if (bookTitle.length() > 25) {
                    bookTitle = bookTitle.substring(0, 22) + "...";
                }
                
                long daysOverdue = LocalDate.now().toEpochDay() - borrowing.getDueDate().toEpochDay();
                
                System.out.printf("%-36s %-20s %-25s %-15d%n",
                    borrowing.getId().toString(),
                    memberName,
                    bookTitle,
                    daysOverdue);
            }
            
            System.out.println("-".repeat(80));
            
            // Summary statistics
            long totalDaysOverdue = overdueBorrowings.stream()
                .mapToLong(b -> LocalDate.now().toEpochDay() - b.getDueDate().toEpochDay())
                .sum();
            
            double averageDaysOverdue = overdueBorrowings.stream()
                .mapToLong(b -> LocalDate.now().toEpochDay() - b.getDueDate().toEpochDay())
                .average()
                .orElse(0.0);
            
            System.out.println("\nSummary:");
            System.out.println("Total days overdue: " + totalDaysOverdue);
            System.out.println("Average days overdue: " + String.format("%.1f", averageDaysOverdue));
            
        } catch (Exception e) {
            System.out.println("❌ Error generating overdue report: " + e.getMessage());
        }
    }
    
    private void generateCurrentlyBorrowedReport() {
        System.out.println("\n📚 CURRENTLY BORROWED BOOKS");
        System.out.println("=".repeat(50));
        
        try {
            List<BorrowingResponse> currentBorrowings = reportService.getCurrentlyBorrowedBooks();
            
            if (currentBorrowings.isEmpty()) {
                System.out.println("✅ No books are currently borrowed!");
                return;
            }
            
            System.out.println("Total currently borrowed books: " + currentBorrowings.size());
            System.out.println("\nDetailed Report:");
            System.out.println("-".repeat(100));
            System.out.printf("%-36s %-20s %-25s %-15s %-15s%n", 
                             "Borrowing ID", "Member", "Book", "Borrow Date", "Due Date");
            System.out.println("-".repeat(100));
            
            for (BorrowingResponse borrowing : currentBorrowings) {
                String memberName = borrowing.getMember() != null ? 
                                   borrowing.getMember().getFirstName() + " " + 
                                   borrowing.getMember().getLastName() : "Unknown";
                if (memberName.length() > 20) {
                    memberName = memberName.substring(0, 17) + "...";
                }
                
                String bookTitle = borrowing.getBook() != null ? 
                                  borrowing.getBook().getTitle() : "Unknown";
                if (bookTitle.length() > 25) {
                    bookTitle = bookTitle.substring(0, 22) + "...";
                }
                
                System.out.printf("%-36s %-20s %-25s %-15s %-15s%n",
                    borrowing.getId().toString(),
                    memberName,
                    bookTitle,
                    borrowing.getBorrowDate().format(DATE_FORMATTER),
                    borrowing.getDueDate().format(DATE_FORMATTER));
            }
            
            System.out.println("-".repeat(100));
            
        } catch (Exception e) {
            System.out.println("❌ Error generating currently borrowed report: " + e.getMessage());
        }
    }
    
    private void generateDateRangeReport() {
        System.out.println("\n📈 BOOKS BORROWED IN DATE RANGE");
        System.out.println("=".repeat(50));
        
        try {
            LocalDate startDate = inputHelper.readDate("Enter start date");
            if (startDate == null) {
                System.out.println("❌ Invalid start date.");
                return;
            }
            
            LocalDate endDate = inputHelper.readDate("Enter end date");
            if (endDate == null) {
                System.out.println("❌ Invalid end date.");
                return;
            }
            
            if (startDate.isAfter(endDate)) {
                System.out.println("❌ Start date cannot be after end date.");
                return;
            }
            
            List<BorrowingResponse> borrowings = reportService.getBooksBorrowedInDateRange(startDate, endDate);
            
            if (borrowings.isEmpty()) {
                System.out.println("✅ No books were borrowed in the specified date range!");
                return;
            }
            
            System.out.println("Books borrowed between " + startDate.format(DATE_FORMATTER) + 
                             " and " + endDate.format(DATE_FORMATTER) + ":");
            System.out.println("Total borrowings: " + borrowings.size());
            
            System.out.println("\nDetailed Report:");
            System.out.println("-".repeat(100));
            System.out.printf("%-36s %-20s %-25s %-15s %-10s%n", 
                             "Borrowing ID", "Member", "Book", "Borrow Date", "Status");
            System.out.println("-".repeat(100));
            
            for (BorrowingResponse borrowing : borrowings) {
                String memberName = borrowing.getMember() != null ? 
                                   borrowing.getMember().getFirstName() + " " + 
                                   borrowing.getMember().getLastName() : "Unknown";
                if (memberName.length() > 20) {
                    memberName = memberName.substring(0, 17) + "...";
                }
                
                String bookTitle = borrowing.getBook() != null ? 
                                  borrowing.getBook().getTitle() : "Unknown";
                if (bookTitle.length() > 25) {
                    bookTitle = bookTitle.substring(0, 22) + "...";
                }
                
                System.out.printf("%-36s %-20s %-25s %-15s %-10s%n",
                    borrowing.getId().toString(),
                    memberName,
                    bookTitle,
                    borrowing.getBorrowDate().format(DATE_FORMATTER),
                    borrowing.getStatus());
            }
            
            System.out.println("-".repeat(100));
            
            // Summary by book
            Map<String, Long> bookCounts = borrowings.stream()
                .filter(b -> b.getBook() != null)
                .collect(Collectors.groupingBy(
                    b -> b.getBook().getTitle(),
                    Collectors.counting()
                ));
            
            System.out.println("\nMost borrowed books in this period:");
            bookCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> 
                    System.out.println("• " + entry.getKey() + " (" + entry.getValue() + " times)")
                );
            
        } catch (Exception e) {
            System.out.println("❌ Error generating date range report: " + e.getMessage());
        }
    }
}
