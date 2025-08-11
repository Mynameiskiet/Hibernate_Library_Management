package com.tuankiet.cli;

import com.tuankiet.cli.helpers.InputHelper;
import com.tuankiet.cli.menus.BookMenu;
import com.tuankiet.cli.menus.BorrowingMenu;
import com.tuankiet.cli.menus.MemberMenu;
import com.tuankiet.cli.menus.ReportMenu;
import com.tuankiet.exceptions.CLIExceptionHandler;
import com.tuankiet.services.SampleDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Main CLI class for Library Management System.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class LibraryManagementCLI {

    private static final Logger logger = LoggerFactory.getLogger(LibraryManagementCLI.class);

    private final InputHelper inputHelper;
    private final BookMenu bookMenu;
    private final MemberMenu memberMenu;
    private final BorrowingMenu borrowingMenu;
    private final ReportMenu reportMenu;
    private final SampleDataService sampleDataService;
    private final CLIExceptionHandler exceptionHandler;

    @Autowired
    public LibraryManagementCLI(InputHelper inputHelper, 
                               BookMenu bookMenu, 
                               MemberMenu memberMenu, 
                               BorrowingMenu borrowingMenu, 
                               ReportMenu reportMenu, 
                               SampleDataService sampleDataService,
                               CLIExceptionHandler exceptionHandler) {
        this.inputHelper = inputHelper;
        this.bookMenu = bookMenu;
        this.memberMenu = memberMenu;
        this.borrowingMenu = borrowingMenu;
        this.reportMenu = reportMenu;
        this.sampleDataService = sampleDataService;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Start the CLI application.
     */
    public void start() {
        logger.info("Starting Library Management System CLI");
        
        displayWelcomeMessage();
        
        // Ask if user wants to load sample data
        try {
            boolean loadSample = inputHelper.readBoolean("Do you want to load sample data?");
            if (loadSample) {
                System.out.println("Loading sample data...");
                sampleDataService.generateSampleData();
                System.out.println("✅ Sample data loaded successfully!");
            }
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }

        displayMainMenu();
        
        logger.info("Library Management System CLI stopped");
    }

    /**
     * Display welcome message.
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    📚 LIBRARY MANAGEMENT SYSTEM 📚");
        System.out.println("=".repeat(60));
        System.out.println("Welcome to the Library Management System!");
        System.out.println("Manage your library's books, members, and borrowings efficiently.");
        System.out.println();
    }

    /**
     * Display and handle main menu.
     */
    private void displayMainMenu() {
        int choice;
        do {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("           MAIN MENU");
            System.out.println("=".repeat(40));
            System.out.println("1. 📖 Book Management");
            System.out.println("2. 👥 Member Management");
            System.out.println("3. 📋 Borrowing Management");
            System.out.println("4. 📊 Reports");
            System.out.println("5. 🔄 Generate Sample Data");
            System.out.println("6. 🗑️  Clear All Data");
            System.out.println("0. 🚪 Exit");
            System.out.println("=".repeat(40));

            choice = inputHelper.readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        logger.info("Accessing Book Management menu");
                        bookMenu.displayMenu();
                        break;
                    case 2:
                        logger.info("Accessing Member Management menu");
                        memberMenu.displayMenu();
                        break;
                    case 3:
                        logger.info("Accessing Borrowing Management menu");
                        borrowingMenu.displayMenu();
                        break;
                    case 4:
                        logger.info("Accessing Reports menu");
                        reportMenu.displayMenu();
                        break;
                    case 5:
                        generateSampleData();
                        break;
                    case 6:
                        clearAllData();
                        break;
                    case 0:
                        displayGoodbyeMessage();
                        logger.info("Library Management System CLI terminated by user");
                        break;
                    default:
                        System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                exceptionHandler.handleException(e);
            }
        } while (choice != 0);
    }

    /**
     * Generate sample data.
     */
    private void generateSampleData() {
        try {
            System.out.println("Generating sample data...");
            sampleDataService.generateSampleData();
            System.out.println("✅ Sample data generated successfully!");
            logger.info("Sample data generated via CLI");
        } catch (Exception e) {
            System.out.println("❌ Error generating sample data: " + e.getMessage());
            logger.error("Error generating sample data via CLI", e);
        }
    }

    /**
     * Clear all data.
     */
    private void clearAllData() {
        try {
            boolean confirm = inputHelper.readBoolean("⚠️  Are you sure you want to clear all data? This action cannot be undone");
            if (confirm) {
                System.out.println("Clearing all data...");
                sampleDataService.clearAllData();
                System.out.println("✅ All data cleared successfully!");
                logger.info("All data cleared via CLI");
            } else {
                System.out.println("Operation cancelled.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error clearing data: " + e.getMessage());
            logger.error("Error clearing data via CLI", e);
        }
    }

    /**
     * Display goodbye message.
     */
    private void displayGoodbyeMessage() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    👋 Thank you for using Library Management System!");
        System.out.println("                 Goodbye!");
        System.out.println("=".repeat(50));
    }
}
